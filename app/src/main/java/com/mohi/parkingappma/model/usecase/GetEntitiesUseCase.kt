package com.mohi.parkingappma.model.usecase

import com.mohi.parkingappma.model.domain.Entity
import com.mohi.parkingappma.model.repo.EntitiesRepository
import javax.inject.Inject

interface GetEntitiesUseCase {
    suspend operator fun invoke(): List<Entity>
}

class BaseGetEntitiesUseCase @Inject constructor(
    private val repo: EntitiesRepository
) : GetEntitiesUseCase {
    override suspend fun invoke(): List<Entity> {
        return repo.getAll()
    }
}