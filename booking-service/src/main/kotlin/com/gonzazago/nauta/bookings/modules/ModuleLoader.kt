package com.gonzazago.nauta.bookings.modules

import com.gonzazago.nauta.bookings.Server
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.vertx.core.json.JsonObject
import io.vertx.core.Vertx
import org.koin.core.context.startKoin
import org.koin.dsl.module

object ModuleLoader {
    private lateinit var vertxInstance: Vertx
    private lateinit var jsonConfig: JsonObject

    private val appModule = module(createdAtStart = true) {
        single { vertxInstance }
        single<Config> { ConfigFactory.load() }
        single { Server() }

        mapper()
    }

    fun init(vertx: Vertx) {
        vertxInstance = vertx

        startKoin {
            modules(appModule)
        }
    }

}