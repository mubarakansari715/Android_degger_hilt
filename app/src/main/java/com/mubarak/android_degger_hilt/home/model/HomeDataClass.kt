package com.mubarak.android_degger_hilt.home.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "HomeDataClass")
data class HomeDataClass(
    @PrimaryKey val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl : String

)