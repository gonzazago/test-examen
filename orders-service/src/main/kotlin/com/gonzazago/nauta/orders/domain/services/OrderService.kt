package com.gonzazago.nauta.orders.domain.services

import com.gonzazago.nauta.orders.domain.model.Order
import com.gonzazago.nauta.orders.repository.order.OrderRepository

class OrderService(private val repository: OrderRepository) {


    suspend fun createOrder(order: Order) {
        repository.save(order)
    }
}