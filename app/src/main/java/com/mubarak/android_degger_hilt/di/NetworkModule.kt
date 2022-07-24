package com.mubarak.android_degger_hilt.di

import android.app.Application
import androidx.room.Room
import com.mubarak.android_degger_hilt.network.ApiService
import com.mubarak.android_degger_hilt.room.HomeDatabase
import com.mubarak.room_demo_kotlin.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providesApiObj(): ApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    /***
     * Room data base
     */

    @Singleton
    @Provides
    fun provideDatabase(app: Application): HomeDatabase =
        Room.databaseBuilder(app, HomeDatabase::class.java, "local_database")
            .allowMainThreadQueries()
            .build()


}