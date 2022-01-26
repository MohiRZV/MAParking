package com.mohi.parkingappma.model.repo.localrepo

import androidx.room.*
import com.mohi.parkingappma.model.domain.Entity

@Dao
interface EntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(entity: Entity) : Long

    @Query("delete from entity where id= :id")
    fun delete(id: Int)

    @Query("select * from entity where not wasDeleted")
    fun getAll(): List<Entity>

    @Query("select * from entity where id= :id")
    fun findById(id: Int): Entity

    @Update
    fun update(entity: Entity)

    @Query("delete from entity")
    fun nuke()

}