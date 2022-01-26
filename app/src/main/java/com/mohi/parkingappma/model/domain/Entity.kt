package com.mohi.parkingappma.model.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entity")
data class Entity(
    @PrimaryKey
    var id: Int = 0,
    @ColumnInfo
    val number: String = "",
    @ColumnInfo
    val address: String = "",
    @ColumnInfo
    val status: String = "",
    @ColumnInfo
    val count: String = "",
    @ColumnInfo
    var isLocal: Boolean = false,
    @ColumnInfo
    var wasDeleted: Boolean = false,
)
