package com.mubarak.android_degger_hilt.home.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mubarak.android_degger_hilt.databinding.ActivityMainBinding
import com.mubarak.android_degger_hilt.home.viewmodel.HomeViewModel
import com.mubarak.room_demo_kotlin.utils.ApiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null

    val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)

        lifecycleScope.launchWhenStarted {
            homeViewModel.fetchData()

            homeViewModel.mStatus.collectLatest {
                when (it) {
                    is ApiState.Empty -> {
                        Log.d(TAG, "@@@onCreate: Empty")
                    }
                    is ApiState.Success<*> -> {
                        Log.d(TAG, "@@@onCreate: Success ${it.data}")
                        txtHomeData.text = it.data.toString()
                    }
                    is ApiState.Failure -> {
                        Log.d(TAG, "@@@onCreate: Failed ${it.error}")
                    }
                    else -> {}
                }
            }
        }

    }
}