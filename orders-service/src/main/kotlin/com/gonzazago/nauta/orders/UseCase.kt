package com.gonzazago.nauta.orders.modules

import com.gonzazago.nauta.orders.application.usecase.orders.CreateOrder
import org.koin.core.module.Module

fun Module.useCases(){
    single { CreateOrder(get()) }
}
