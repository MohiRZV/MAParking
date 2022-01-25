package com.mohi.parkingappma.model.service

import com.mohi.parkingappma.model.domain.Entity
import retrofit2.http.*

interface EntitiesService {

    @GET("/spaces")
    suspend fun getAll(): List<Entity>

    @DELETE("/space/{id}")
    suspend fun delete(@Path("id")id: Int): Entity

    @POST("/space")
    suspend fun add(@Body entity: Entity): Entity
}