package com.mohi.parkingappma.model.usecase

import com.mohi.parkingappma.model.domain.Entity
import com.mohi.parkingappma.model.repo.EntitiesRepository
import javax.inject.Inject

interface TakeUseCase {
    suspend operator fun invoke(id: Int): Entity
}

class BaseTakeUseCase @Inject constructor(
    private val repo: EntitiesRepository
) : TakeUseCase {
    override suspend fun invoke(id: Int): Entity {
        return repo.take(id)
    }
}