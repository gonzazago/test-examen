package com.gonzazago.nauta.orders.modules

import com.gonzazago.nauta.orders.Server
import com.gonzazago.nauta.orders.application.usecase.orders.CreateOrder
import com.gonzazago.nauta.orders.delivery.rest.orders.OrderHandler
import com.gonzazago.nauta.orders.domain.services.OrderService
import com.gonzazago.nauta.orders.routes.OrderRouter
import com.gonzazago.nauta.orders.routes.Router
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.vertx.core.Vertx
import org.koin.core.context.startKoin
import org.koin.dsl.module

object ModuleLoader {
    private lateinit var vertxInstance: Vertx

    private val appModule = module(createdAtStart = true) {
        single { vertxInstance }
        single<Config> { ConfigFactory.load() }
        single { Server(get()) }
        single { OrderRouter(get()) }
        single { Router(get(), get()) }
        single { OrderService() }

        handlers()
        useCases()
        mapper()
    }

    fun init(vertx: Vertx) {
        vertxInstance = vertx

        startKoin {
            modules(appModule)
        }
    }


}
