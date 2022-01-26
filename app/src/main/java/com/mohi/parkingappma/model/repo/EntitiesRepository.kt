package com.mohi.parkingappma.model.repo

import android.util.Log
import com.mohi.parkingappma.MainActivity
import com.mohi.parkingappma.model.domain.Entity
import com.mohi.parkingappma.model.repo.localrepo.LocalDatabase
import com.mohi.parkingappma.model.repo.localrepo.LocalEntitiesRepository
import com.mohi.parkingappma.model.service.EntitiesService
import com.mohi.parkingappma.utils.InternetStatus
import com.mohi.parkingappma.utils.InternetStatusLive
import retrofit2.HttpException
import java.net.ConnectException
import javax.inject.Inject

interface EntitiesRepository {
    suspend fun getAll(): List<Entity>
    suspend fun delete(entity: Entity): Entity
    suspend fun add(entity: Entity): Entity
    suspend fun take(id: Int): Entity
    suspend fun free(): List<Entity>
    suspend fun backOnline(): List<Entity>
}

class BaseEntitiesRepository @Inject constructor(
    private val service: EntitiesService
) : EntitiesRepository {
    private var retrieved = true
    private val localRepo = LocalEntitiesRepository(LocalDatabase.getDatabase(MainActivity.instance.applicationContext).entityDao())
    override suspend fun getAll(): List<Entity> {
        return if(InternetStatusLive.status.value?.equals(InternetStatus.OFFLINE) == true || retrieved) {
            retrieved = false
            localRepo.getAll()
        } else {
            localRepo.getAll().forEach { entity ->
                if(entity.isLocal) {
                    try {
                        if (entity.wasDeleted)
                            service.delete(entity.id)
                        else
                            service.add(entity)
                    } catch (ex: HttpException) {
                        //not relevant here
                    }
                }
            }
            var entities : List<Entity> = listOf()
            try {
                entities = service.getAll()
                localRepo.nuke()
                entities.forEach { localRepo.save(it) }
                retrieved = true
            } catch (ex: ConnectException) {
                Log.d("ConnectException", ex.message.toString())
            }
            entities
        }
    }

    override suspend fun delete(entity: Entity): Entity {
        return if(InternetStatusLive.status.value?.equals(InternetStatus.OFFLINE) == true) {
            Log.d("Mohi","Marking entity as deleted locally")
            localRepo.update(entity)
        } else {
            service.delete(entity.id)
        }
    }

    override suspend fun add(entity: Entity): Entity {
        return if(InternetStatusLive.status.value?.equals(InternetStatus.OFFLINE) == true) {
            entity.isLocal = true
            Log.d("Mohi","Saving $entity locally")
            localRepo.save(entity)
        } else {
            Log.d("Mohi","Saving $entity")
            val saved = service.add(entity)
            entity.id = saved.id
            localRepo.save(entity)
            saved
        }
    }

    override suspend fun take(id: Int): Entity {
        if(InternetStatusLive.status.value?.equals(InternetStatus.OFFLINE) == true)
            throw ConnectException("No internet connection")
        return service.take(id)
    }

    override suspend fun free(): List<Entity> {
        if(InternetStatusLive.status.value?.equals(InternetStatus.OFFLINE) == true)
            throw ConnectException("No internet connection")
        return service.free()
    }

    override suspend fun backOnline(): List<Entity> {
        localRepo.getAll().forEach { entity ->
            if(entity.isLocal) {
                try {
                    if (entity.wasDeleted)
                        service.delete(entity.id)
                    else
                        service.add(entity)
                } catch (ex: HttpException) {
                    //not relevant here
                }
            }
        }
        var entities = listOf<Entity>()
        try {
            localRepo.nuke()
            entities = service.getAll()
            entities.forEach { localRepo.save(it) }
        } catch (ex: ConnectException) {
            Log.d("ConnectException", ex.message.toString())
        }
        return entities
    }
}