package com.gonzazago.nauta.orchestator.delivery.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gonzazago.nauta.orchestator.application.container.ProcessContainerAction
import com.gonzazago.nauta.orchestator.application.order.ProcessOrderAction
import com.gonzazago.nauta.orchestator.delivery.dto.EmailIngestRequest
import com.gonzazago.nauta.orders.domain.model.Invoice
import io.vertx.core.impl.logging.LoggerFactory
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class IngestHandler(
    private val objectMapper: ObjectMapper,
    private val processOrderAction: ProcessOrderAction,
    private val processContainerAction: ProcessContainerAction,
    private val coroutineScope: CoroutineScope
) {

    val log = LoggerFactory.getLogger(IngestHandler::class.java)

    fun handleEmailIngestion(ctx: RoutingContext) {
        log.info("Central Ingester: Email ingestion request received at /api/email.")
        val rawJsonString = ctx.body().asString()
        log.info("Central Ingester: Raw JSON Body: ${rawJsonString.take(500)}...")

        try {
            val emailRequestDto = objectMapper.readValue<EmailIngestRequest>(rawJsonString)
            log.info("Central Ingester: Deserialized DTO: ${emailRequestDto.toString()}")

            val clientId =
                ctx.request().getHeader("X-Client-ID") ?: throw IllegalArgumentException("Client ID header missing")
            val bookingId = emailRequestDto.booking ?: throw IllegalArgumentException("Booking ID missing")

            ctx.response()
                .setStatusCode(202)
                .end("Ingestion request accepted. Processing asynchronously via Event Bus.")

            coroutineScope.launch {
                try {
                    if (emailRequestDto.orders != null) {
                        sendOrder(emailRequestDto, clientId, bookingId)
                    }
                    if (emailRequestDto.containers != null) {
                        sendContainer(emailRequestDto, clientId, bookingId)
                    }
                } catch (e: Exception) {
                    log.error("Central Ingester: Error during ingestion request: ${e.message}", e)
                }
            }
        } catch (e: Exception) {
            log.error("Central Ingester: Error during ingestion request: ${e.message}", e)
            if (!ctx.response().ended()) {
                ctx.response().setStatusCode(400).end("Invalid ingestion data: ${e.message}")
            }
        }
    }


    private suspend fun sendOrder(
        emailIngestRequest: EmailIngestRequest,
        clientId: String,
        bookingId: String
    ) {
        val containerIds: List<String> = emailIngestRequest.containers?.map { it.container } ?: emptyList()


        emailIngestRequest.orders?.forEach { orderInputDto ->
            val invoiceDTO = orderInputDto.invoices
            val invoices = invoiceDTO.map {
                Invoice(
                    id = it.invoice,
                    orderPurchaseId = orderInputDto.purchase,
                    clientId = clientId
                )
            }
            val orderJsonForQueue = JsonObject.mapFrom(orderInputDto)
                .put("client_id", clientId)
                .put("booking_id", bookingId)
                .put("invoices", invoices)

            if (containerIds.isNotEmpty()) {
                orderJsonForQueue.put("container_ids", containerIds)

            }
            processOrderAction.processOrder(orderJsonForQueue)
            log.info("Central Ingester: Order input DTO handed to ProcessOrderAction: ${orderInputDto.purchase}")
        }
    }

    private suspend fun sendContainer(
        emailIngestRequest: EmailIngestRequest,
        clientId: String,
        bookingId: String
    ) {
        val allOrderIdsInRequest: List<String> = emailIngestRequest.orders?.map { it.purchase } ?: emptyList()
        emailIngestRequest.containers?.forEach { containerInputDto ->
            val containerJsonForQueue = JsonObject.mapFrom(containerInputDto)
                .put("client_id", clientId)
                .put("booking_id", bookingId)

            if (allOrderIdsInRequest.isNotEmpty()) containerJsonForQueue.put(
                "associated_order_ids_json",
                allOrderIdsInRequest
            )

            processContainerAction.processContainer(containerJsonForQueue)
            log.info("Central Ingester: Published container input DTO to Event Bus: ${containerInputDto.container}")
        }
    }
}

