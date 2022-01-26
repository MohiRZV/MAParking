package com.mohi.parkingappma.model.usecase

import com.mohi.parkingappma.model.domain.Entity
import com.mohi.parkingappma.model.repo.EntitiesRepository
import javax.inject.Inject

interface SyncUseCase {
    suspend operator fun invoke(): List<Entity>
}

class BaseSyncUseCase @Inject constructor(
    private val repo: EntitiesRepository
) : SyncUseCase {
    override suspend fun invoke(): List<Entity> {
        return repo.backOnline()
    }
}