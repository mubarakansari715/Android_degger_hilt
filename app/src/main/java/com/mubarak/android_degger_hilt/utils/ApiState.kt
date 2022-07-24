package com.mubarak.room_demo_kotlin.utils

sealed class ApiState() {
    object Empty: ApiState()
    object Loading: ApiState()
    class Success<T>(val data:T): ApiState()
    class Failure(val error:Throwable): ApiState()
}