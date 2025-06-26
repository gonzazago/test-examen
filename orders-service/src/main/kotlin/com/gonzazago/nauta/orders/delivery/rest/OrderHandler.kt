package com.gonzazago.nauta.orders.delivery.rest

import com.gonzazago.nauta.orders.domain.model.Order
import io.vertx.core.impl.logging.LoggerFactory
import io.vertx.ext.web.RoutingContext
import io.vertx.core.json.Json
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OrderHandler {

    val log = LoggerFactory.getLogger(OrderHandler::class.java)
    fun createOrder(ctx: RoutingContext) {
        log.info("Create Order ")
        val body = ctx.bodyAsJson
        log.info("Body :$body")
        val order = body.mapTo(Order::class.java)

        // Simular lógica de negocio
        GlobalScope.launch(ctx.vertx().dispatcher()) {
            println("📦 Order received: $order")

            ctx.response()
                .putHeader("content-type", "application/json")
                .setStatusCode(201)
                .end(Json.encodePrettily(order))
        }
    }
}