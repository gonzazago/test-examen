package com.gonzazago.nauta.orders.modules

import com.gonzazago.nauta.orders.Server
import com.gonzazago.nauta.orders.delivery.rest.OrderHandler
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.vertx.core.json.JsonObject
import io.vertx.core.Vertx
import org.koin.core.context.startKoin
import org.koin.dsl.module

object ModuleLoader {
    private lateinit var vertxInstance: Vertx

    private val appModule = module(createdAtStart = true) {
        single { vertxInstance }
        single<Config> { ConfigFactory.load() }
        single { Server() }
        single { OrderHandler() }

        mapper()
    }

    fun init(vertx: Vertx) {
        vertxInstance = vertx

        startKoin {
            modules(appModule)
        }
    }


}