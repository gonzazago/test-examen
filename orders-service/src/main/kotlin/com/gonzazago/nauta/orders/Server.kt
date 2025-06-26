package com.gonzazago.nauta.orders

import com.gonzazago.nauta.orders.routes.Router
import com.typesafe.config.Config
import io.vertx.core.Vertx
import io.vertx.core.impl.logging.LoggerFactory

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class Server (    private val mainAppRouter:Router) : KoinComponent {
    private val config: Config by inject()
    private val vertx: Vertx by inject()
    private val log = LoggerFactory.getLogger(Server::class.java)
    fun start() {
        val portConfig: Int = config.getInt("server.port")
        val host = config.getString("server.host")
        val router = mainAppRouter.createRouter(vertx)

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(portConfig, host) { res ->
                if (res.succeeded()) {
                    log.info("Server started at http://$host:$portConfig")
                } else {
                    log.error("Failed to start server: ${res.cause().message}")
                }
            }
    }
}
