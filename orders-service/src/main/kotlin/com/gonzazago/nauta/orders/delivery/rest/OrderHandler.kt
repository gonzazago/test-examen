package com.gonzazago.nauta.orders.delivery.rest

import com.gonzazago.nauta.orders.domain.model.Order
import com.gonzazago.nauta.orders.domain.model.Booking
import com.gonzazago.nauta.orders.utils.parse
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
        val body = ctx.body().asString()
        log.info("Body :$body")
        val order = body.parse<Booking>()

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
