package com.gonzazago.nauta.orchestator.application.container

import com.gonzazago.nauta.orchestator.infra.publisher.Publisher
import io.vertx.core.impl.logging.LoggerFactory
import io.vertx.core.json.JsonObject

class ProcessContainerAction(
    private val orderPublisher: Publisher<JsonObject>,
) {
    private val log = LoggerFactory.getLogger(ProcessContainerAction::class.java)
    private val ORDER_CREATION_QUEUE = "container.creation.queue"

    suspend fun processContainer(containerMessage: JsonObject) {
        val orderId = containerMessage.getString("conatiner") ?: containerMessage.getString("id")
        log.info("ProcessContainerAction: Publishing container message to queue for ID: $orderId")
        orderPublisher.publish(ORDER_CREATION_QUEUE, containerMessage)
    }
}