package com.mubarak.android_degger_hilt.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mubarak.android_degger_hilt.home.model.HomeDataClass


@Dao
interface HomeDao {

    @Query("select * from HomeDataClass")
    fun getHomeDao(): List<HomeDataClass>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: List<HomeDataClass>)


}