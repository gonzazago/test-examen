package com.gonzazago.nauta.orders.repository.order

import com.gonzazago.nauta.orders.domain.model.Order

interface OrderRepository {
    suspend fun save(order: Order): Order
}