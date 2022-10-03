package com.mubarak.android_degger_hilt.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mubarak.android_degger_hilt.home.model.HomeDataClass

@Database(
    entities = [HomeDataClass::class],
    version = 1,
)
abstract class HomeDatabase : RoomDatabase() {

    abstract fun homeDao(): HomeDao

    companion object{

        var migration_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE HomeDataClass ADD COLUMN thumbnailUrl STRING")
            }

        }
    }


}