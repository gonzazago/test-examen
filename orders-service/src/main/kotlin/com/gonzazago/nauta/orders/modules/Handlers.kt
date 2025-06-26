package com.gonzazago.nauta.orders.modules

import com.gonzazago.nauta.orders.application.usecase.orders.CreateOrder
import com.gonzazago.nauta.orders.delivery.rest.orders.OrderHandler
import org.koin.core.module.Module

fun Module.handlers(){
    single { OrderHandler(get()) }
}