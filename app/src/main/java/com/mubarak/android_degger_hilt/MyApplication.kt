package com.mubarak.android_degger_hilt

import android.app.Application
import com.mubarak.android_degger_hilt.utils.MyPreference
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class MyApplication:Application() {

    @Inject
    lateinit var mPref: MyPreference

    companion object {
        private var instance: MyApplication? = null
        fun applicationContext(): MyApplication {
            return instance as MyApplication
        }
    }

    init {
        instance = this
    }
}