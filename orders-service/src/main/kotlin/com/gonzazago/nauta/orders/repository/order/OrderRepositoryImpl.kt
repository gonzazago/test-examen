package com.gonzazago.nauta.orders.repository.order

import com.gonzazago.nauta.orders.domain.model.Invoice
import com.gonzazago.nauta.orders.domain.model.Order
import com.gonzazago.nauta.orders.mapper.OrderMapper
import com.gonzazago.nauta.orders.repository.entity.OrderContainerEntity
import com.gonzazago.nauta.orders.repository.entity.OrderEntity
import io.vertx.core.impl.logging.LoggerFactory
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.coAwait
import io.vertx.sqlclient.*

class OrderRepositoryImpl(
    private val orderMapper: OrderMapper,
    private val dbClient: SqlClient
) : OrderRepository {

    private val log = LoggerFactory.getLogger(OrderRepositoryImpl::class.java)

    // *** ¡NUEVA FUNCIÓN AUXILIAR PARA GESTIONAR LA TRANSACCIÓN! ***
    private suspend fun <T> withTransaction(block: suspend (SqlConnection, Transaction) -> T): T {
        val poolClient = dbClient as Pool // Asegúrate de que dbClient es un Pool
        val connection: SqlConnection = poolClient.getConnection().coAwait() // Obtener una conexión
        val transaction: Transaction = connection.begin().coAwait() // Iniciar la transacción

        try {
            val result =
                block(connection, transaction) // Ejecuta el bloque de código, pasándole la conexión y la transacción
            transaction.commit().coAwait() // Confirma
            return result
        } catch (e: Exception) {
            log.error("Transaction failed, rolling back: ${e.message}", e)
            transaction.rollback().coAwait() // Hace rollback
            throw e // Relanza la excepción
        } finally {
            connection.close().coAwait() // Cierra la conexión
        }
    }


    override suspend fun save(order: Order): Order {
        log.info("OrderRepositoryImpl: Attempting to save order: ${order.purchase}")

        return withTransaction { connection, _ ->

            val orderEntity = orderMapper.toEntity(order)
            insertOrderPurchase(connection, orderEntity)
            insertOrderInvoices(connection, order.invoices)
            insertOrderContainer(connection, order)
            order
        }
    }

    private suspend fun insertOrderPurchase(connection: SqlConnection, order: OrderEntity) {
        if (!findPurchaseByID(connection, order.id)) {
            val insertOrderSql = """
            INSERT INTO purchase_orders (purchase_order_id, client_id, booking_id)
            VALUES (?, ?, ?)
        """.trimIndent()
            val orderParams = Tuple.of(order.id, order.clientId, order.bookingId)
            connection.preparedQuery(insertOrderSql).execute(orderParams).await()
            log.info("OrderRepositoryImpl: Saved purchase_order: ${order.id}")
        }

    }

    private suspend fun insertOrderInvoices(connection: SqlConnection, invoices: List<Invoice>) {
        invoices.forEach { invoice ->
            val invoiceEntity = orderMapper.toEntity(invoice)
            if (!findInvoiceByID(connection, invoiceEntity.id)) {
                val insertInvoiceSql = """
                INSERT INTO invoices (invoice_id, client_id, purchase_order_id)
                VALUES (?, ?, ?)
            """.trimIndent()
                val invoiceParams = Tuple.of(invoiceEntity.id, invoiceEntity.clientId, invoiceEntity.orderPurchaseId)
                connection.preparedQuery(insertInvoiceSql).execute(invoiceParams).await()
                log.info("OrderRepositoryImpl: Saved invoice: ${invoice.id} for order ${invoice.orderPurchaseId}")
            }

        }
    }

    private suspend fun insertOrderContainer(connection: SqlConnection, order: Order) {

        order.containerIds?.forEach { containerId ->
            val orderContainerEntity = OrderContainerEntity(
                orderPurchaseId = order.purchase,
                containerId = containerId,
                clientId = order.clientId
            )
            val insertOrderContainerSql = """
                    INSERT INTO order_containers (purchase_order_id, container_id, client_id)
                    VALUES (?, ?, ?)
                """.trimIndent()
            val orderContainerParams = Tuple.of(
                orderContainerEntity.orderPurchaseId,
                orderContainerEntity.containerId,
                orderContainerEntity.clientId
            )
            connection.preparedQuery(insertOrderContainerSql).execute(orderContainerParams).coAwait()
            log.info("OrderRepositoryImpl: Saved order_container relation: Order ${order.purchase} - Container ${containerId}")

        }
    }

    private suspend fun findPurchaseByID(connection: SqlConnection, invoice: String): Boolean {
        val findInvoiceQuery = "SELECT PURCHASE_ORDER_ID from PURCHASE_ORDERS WHERE PURCHASE_ORDER_ID = ?".trimIndent()
        val rows = connection.preparedQuery(findInvoiceQuery).execute(Tuple.of(invoice)).coAwait()
        return rows.size() == 1
    }

    private suspend fun findInvoiceByID(connection: SqlConnection, invoice: String): Boolean {
        val findInvoiceQuery = "SELECT INVOICE_ID from INVOICES WHERE INVOICE_ID = ?".trimIndent()
        val rows = connection.preparedQuery(findInvoiceQuery).execute(Tuple.of(invoice)).coAwait()
        return rows.size() == 1
    }
}