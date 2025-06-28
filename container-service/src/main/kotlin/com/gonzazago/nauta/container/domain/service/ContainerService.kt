package com.gonzazago.nauta.container.domain.service

import com.gonzazago.nauta.container.domain.model.Container
import com.gonzazago.nauta.container.repository.container.ContainerRepository

class ContainerService(private val repository: ContainerRepository) {

    suspend fun createBooking(container: Container){
        repository.createContainer(container)
    }
}