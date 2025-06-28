package com.gonzazago.nauta.container.repository.container

import com.gonzazago.nauta.container.domain.model.Container
import io.vertx.core.impl.logging.LoggerFactory
import io.vertx.sqlclient.SqlClient

class ContainerRepositoryImpl(sqlClient: SqlClient) : ContainerRepository {

    private val log = LoggerFactory.getLogger(ContainerRepositoryImpl::class.java)


    override suspend fun createContainer(container: Container) {
        log.info("Dummy: Saving container: ${container.container}")
    }
}