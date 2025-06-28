package com.gonzazago.nauta.container.repository.entity

data class ContainerEntity(
    val id: String,
    val clientId: String,
    val bookingId: String?,
    val associatedOrderIdsJson: String? = null,
)