package com.gonzazago.nauta.container.repository.container

import com.gonzazago.nauta.container.domain.model.Container

interface ContainerRepository {
    suspend fun createContainer(container: Container)
}