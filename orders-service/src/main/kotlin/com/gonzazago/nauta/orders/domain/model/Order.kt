package com.gonzazago.nauta.orders.domain.model

data class Order(
    val purchase: String,
    val invoices: List<Invoice>
) {
}


data class Invoice(
    val invoice: String
)