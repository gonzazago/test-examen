package com.gonzazago.nauta.orders.modules

import com.gonzazago.nauta.orders.mapper.ParserFactory
import org.koin.core.module.Module

fun Module.mapper(){
    single{ ParserFactory().parser() }
}