package com.mubarak.android_degger_hilt.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mubarak.android_degger_hilt.home.model.HomeDataClass

@Database(
    entities = [HomeDataClass::class],
    version = 1,
)
abstract class HomeDatabase : RoomDatabase() {

    abstract fun homeDao(): HomeDao
}