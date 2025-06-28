package com.gonzazago.nauta.container.application.usecase

import com.gonzazago.nauta.container.domain.model.Container
import com.gonzazago.nauta.container.domain.service.ContainerService
import io.vertx.core.impl.logging.LoggerFactory

class CreateContainer(containerService: ContainerService) {
    private val log = LoggerFactory.getLogger(CreateContainer::class.java)

    suspend fun createContainer(container: Container){
        log.info("")
    }
}