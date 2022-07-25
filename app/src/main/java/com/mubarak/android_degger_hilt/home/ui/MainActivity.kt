package com.mubarak.android_degger_hilt.home.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.mubarak.android_degger_hilt.databinding.ActivityMainBinding
import com.mubarak.android_degger_hilt.home.adapter.HomeAdapter
import com.mubarak.android_degger_hilt.home.model.HomeDataClass
import com.mubarak.android_degger_hilt.home.viewmodel.HomeViewModel
import com.mubarak.android_degger_hilt.utils.ApiState
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
                    is ApiState.Loading -> {
                        progress_circular.isVisible = true
                    }
                    is ApiState.Empty -> {
                        Log.d(TAG, "@@@onCreate: Empty")
                    }
                    is ApiState.Success<*> -> {
                        progress_circular.isVisible = false
                        Log.d(TAG, "@@@onCreate: Success ${it.data}")

                        //success
                        if (it.data is List<*>) {
                            val listData: List<HomeDataClass> =
                                it.data.filterIsInstance<HomeDataClass>()
                            recyclerView.adapter = HomeAdapter(list = listData)
                        }
                    }
                    is ApiState.Failure -> {
                        Log.d(TAG, "@@@onCreate: Failed ${it.error}")
                        progress_circular.isVisible = false
                    }
                }
            }
        }

    }
}