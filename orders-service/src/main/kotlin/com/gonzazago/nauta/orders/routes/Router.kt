package com.gonzazago.nauta.orders.routes

import io.vertx.core.Vertx
import io.vertx.ext.web.Router

class Router(
    private val vertx: Vertx,
    private val ordersRouter: OrderRouter
) {
    fun createRouter(vertx: Vertx): Router {
        val router = Router.router(vertx)

        // Ruta simple para verificar si el server está vivo
        router.get("/health").handler { ctx ->
            ctx.response()
                .putHeader("content-type", "text/plain")
                .end("OK")
        }
        router.mountSubRouter("/v1/orders", ordersRouter.ordersRoutes(vertx))

        return router
    }
}
