package com.mubarak.android_degger_hilt.home.repository

import androidx.room.withTransaction
import com.mubarak.android_degger_hilt.home.model.HomeDataClass
import com.mubarak.android_degger_hilt.network.ApiService
import com.mubarak.android_degger_hilt.room.HomeDatabase
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val apiService: ApiService,
    private val db: HomeDatabase
) {
    private val homeDao = db.homeDao()

    suspend fun getDataFromRepository(): List<HomeDataClass> {
        var dataFromApi = homeDao.getHomeDao()

        if (dataFromApi.isEmpty()) {
            dataFromApi = apiService.getData()

        }

        db.withTransaction {
            homeDao.insertData(dataFromApi)
        }

        return dataFromApi
    }
}