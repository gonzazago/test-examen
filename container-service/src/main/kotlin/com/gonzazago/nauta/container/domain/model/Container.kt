package com.gonzazago.nauta.container.domain.model

class Container(
    val container: String,
    val clientId: String,
    val bookingId: String?,
    val associatedOrderIds: List<String>? = null,
)