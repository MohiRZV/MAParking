package com.mohi.parkingappma.model.usecase

import com.mohi.parkingappma.model.domain.Entity
import com.mohi.parkingappma.model.repo.EntitiesRepository
import javax.inject.Inject

interface AddEntityUseCase {
    suspend operator fun invoke(entity: Entity): Entity
}

class BaseAddEntityUseCase @Inject constructor(
    private val repo: EntitiesRepository
) : AddEntityUseCase {
    override suspend fun invoke(entity: Entity): Entity {
        return repo.add(entity)
    }
}