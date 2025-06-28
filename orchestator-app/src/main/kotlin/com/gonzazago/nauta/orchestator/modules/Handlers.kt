package com.gonzazago.nauta.orchestator.modules

import com.gonzazago.nauta.container.delivery.rest.ContainerHandler
import com.gonzazago.nauta.orchestator.delivery.rest.IngestHandler
import com.gonzazago.nauta.orders.delivery.rest.orders.OrderHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module

fun Module.handlers() {

    single<CoroutineScope> {
        CoroutineScope(Dispatchers.IO + SupervisorJob())
    }

    //order-service-handlers
    single { OrderHandler(get(), get()) }
    //orchestrator-service-handlers
    single { IngestHandler(get(), get(), get(), get()) }
    //container-service-handlers
    single { ContainerHandler() }
}