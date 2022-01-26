package com.mohi.parkingappma.model.usecase

import com.mohi.parkingappma.model.domain.Entity
import com.mohi.parkingappma.model.repo.EntitiesRepository
import javax.inject.Inject

interface GetFreeUseCase {
    suspend operator fun invoke(): List<Entity>
}

class BaseGetFreeUseCase @Inject constructor(
    private val repo: EntitiesRepository
) : GetFreeUseCase {
    override suspend fun invoke(): List<Entity> {
        return repo.free()
    }
}