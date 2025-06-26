package com.gonzazago.nauta.bookings

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

    // Ejemplo: podrías agregar un sub-router así
    // router.mountSubRouter("/orders", ordersRouter(vertx))

    return router
}