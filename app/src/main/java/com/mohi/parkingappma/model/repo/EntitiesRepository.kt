package com.mohi.parkingappma.model.repo

import com.mohi.parkingappma.model.domain.Entity
import com.mohi.parkingappma.model.service.EntitiesService
import javax.inject.Inject

interface EntitiesRepository {
    suspend fun getAll(): List<Entity>
    suspend fun delete(id: Int): Entity
    suspend fun add(entity: Entity): Entity
}

class BaseEntitiesRepository @Inject constructor(
    private val service: EntitiesService
) : EntitiesRepository {
    override suspend fun getAll(): List<Entity> {
        return service.getAll()
    }

    override suspend fun delete(id: Int): Entity {
        return service.delete(id)
    }

    override suspend fun add(entity: Entity): Entity {
        return service.add(entity)
    }
}