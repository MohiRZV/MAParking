package com.mohi.parkingappma.model.usecase

import com.mohi.parkingappma.model.domain.Entity
import com.mohi.parkingappma.model.repo.EntitiesRepository
import javax.inject.Inject

interface DeleteEntityUseCase {
    suspend operator fun invoke(entity: Entity): Entity
}

class BaseDeleteEntityUseCase @Inject constructor(
    private val repo: EntitiesRepository
) : DeleteEntityUseCase {
    override suspend fun invoke(entity: Entity): Entity {
        return repo.delete(entity)
    }
}