package com.mohi.parkingappma.model.repo.localrepo

import android.util.Log
import com.mohi.parkingappma.model.domain.Entity
import javax.inject.Inject

class LocalEntitiesRepository @Inject constructor(
    private val entityDao: EntityDao
){

    fun getAll(): List<Entity> {
        return entityDao.getAll()
    }

    fun save(entity: Entity): Entity {
        entityDao.save(entity)
        Log.d("Mohi","Saved locally $entity")
        return entity
    }

    fun update(entity: Entity): Entity {
        entity.wasDeleted = true
        entity.isLocal = true
        entityDao.update(entity)
        Log.d("Mohi","Marked $entity for deletion locally")
        return entity
    }

    fun delete(entity: Entity): Entity {
        entityDao.delete(entity.id)
        return entity
    }

    fun nuke() {
        entityDao.nuke()
    }
}