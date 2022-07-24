package com.mubarak.android_degger_hilt.network

import com.mubarak.android_degger_hilt.home.model.HomeDataClass
import retrofit2.http.GET


interface ApiService {
    @GET("photos")
    suspend fun getData(): List<HomeDataClass>

}