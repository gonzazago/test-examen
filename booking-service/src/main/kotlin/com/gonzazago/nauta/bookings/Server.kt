package com.gonzazago.nauta.bookings

import com.typesafe.config.Config
import io.vertx.core.Vertx

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class Server : KoinComponent {
    private val config: Config by inject()
    private val vertx: Vertx by inject()
    fun start() {
        val portConfig: Int = config.getInt("server.port")
        val host = config.getString("server.host")
        val router = createRouter(vertx)

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(portConfig, host) { res ->
                if (res.succeeded()) {
                    println("Booking Service started at http://$host:$portConfig")
                } else {
                    println("Failed to start server: ${res.cause().message}")
                }
            }
    }
}