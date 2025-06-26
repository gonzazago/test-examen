package com.gonzazago.nauta.bookings.modules

import com.gonzazago.nauta.bookings.mapper.ParserFactory
import org.koin.core.module.Module

fun Module.mapper(){
    single{ ParserFactory().parser() }
}