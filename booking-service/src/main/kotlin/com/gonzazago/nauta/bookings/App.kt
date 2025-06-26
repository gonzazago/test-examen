package com.gonzazago.nauta.bookings

import com.gonzazago.nauta.bookings.modules.ModuleLoader
import io.vertx.core.Vertx
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


fun main( args: Array<String>) {
    val app = App()
    app.start()

}

class App : KoinComponent {

    private val server: Server by inject()
    val vertx: Vertx = Vertx.vertx()
    init {
        ModuleLoader.init(vertx)
    }

    fun start() {
        server.start()
    }
}