package com.gonzazago.nauta.orders.routes

import io.vertx.ext.web.Router
import io.vertx.core.Vertx

fun createRouter(vertx: Vertx): Router {
    val router = Router.router(vertx)

    // Ruta simple para verificar si el server está vivo
    router.get("/health").handler { ctx ->
        ctx.response()
            .putHeader("content-type", "text/plain")
            .end("OK")
    }
    router.mountSubRouter("/v1/orders", ordersRoutes(vertx))

    return router
}