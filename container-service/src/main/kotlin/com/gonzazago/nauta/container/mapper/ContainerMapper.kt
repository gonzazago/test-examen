package com.gonzazago.nauta.container.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gonzazago.nauta.container.delivery.dto.ContainerRequestDto
import com.gonzazago.nauta.container.delivery.dto.ContainerResponseDto
import com.gonzazago.nauta.container.domain.model.Container
import com.gonzazago.nauta.container.repository.entity.ContainerEntity
import io.vertx.core.impl.logging.LoggerFactory

class ContainerMapper(
    // Se inyecta el ObjectMapper general de la aplicación
    private val objectMapper: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(ContainerMapper::class.java)

    // --- Mapper de DTO de Entrada (ContainerInputDto) a Modelo de Dominio (Container) ---
    // Usado por el consumidor de cola (ContainerQueueConsumerVerticle) o un handler REST
    fun toDomainModel(dto: ContainerRequestDto, clientId: String, bookingId: String?): Container {
        return Container(
            container = dto.id,
            clientId = clientId,
            bookingId = bookingId,
            associatedOrderIds = dto.associatedOrderIds
        )
    }

    // --- Mapper de Modelo de Dominio (Container) a Entidad de Persistencia (ContainerEntity) ---
    // Usado por la capa de repositorio antes de guardar en la DB
    fun toEntity(model: Container): ContainerEntity {
        // Serializa la lista de IDs de órdenes a un JSON string para guardar en la DB
        val associatedOrderIdsJson = model.associatedOrderIds?.let {
            if (it.isNotEmpty()) objectMapper.writeValueAsString(it) else null
        }

        return ContainerEntity(
            id = model.container,
            clientId = model.clientId,
            bookingId = model.bookingId,
            associatedOrderIdsJson = associatedOrderIdsJson
        )
    }

    fun toDomainModel(entity: ContainerEntity): Container {
        val associatedOrderIds = entity.associatedOrderIdsJson?.let {
            if (it.isNotBlank()) objectMapper.readValue<List<String>>(it) else null
        }

        return Container(
            container = entity.id,
            clientId = entity.clientId,
            bookingId = entity.bookingId,
            associatedOrderIds = associatedOrderIds
        )
    }

    // --- Mapper de Modelo de Dominio (Container) a DTO de Salida (ContainerResponseDto) ---
    // Usado por la capa de entrega (GetContainersHandler)
    fun toResponseDto(model: Container): ContainerResponseDto {
        return ContainerResponseDto(
            id = model.container,
            clientId = model.clientId,
            bookingId = model.bookingId,
            associatedOrderIds = model.associatedOrderIds,
        )
    }
}