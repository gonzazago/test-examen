package com.gonzazago.nauta.orchestator.routes

import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import com.gonzazago.nauta.orders.routes.OrderRouter


class Router(
    private val vertx: Vertx,
    private val ingestRouter: IngestRouter
) {
    fun createRouter(vertx: Vertx): Router {
        val router = Router.router(vertx)

        router.get("/health").handler { ctx ->
            ctx.response()
                .putHeader("content-type", "text/plain")
                .end("OK")
        }
        router.mountSubRouter("/v1/api", ingestRouter.ingestRoute(vertx))

        return router
    }
}
