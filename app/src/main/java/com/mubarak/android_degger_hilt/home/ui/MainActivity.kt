package com.mubarak.android_degger_hilt.home.ui

import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
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

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)

        lifecycleScope.launchWhenStarted {

            Handler(Looper.getMainLooper()).postDelayed({
                homeViewModel.fetchData()
            }, 3000)

            if (isCheckInternetConnection())
            {
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
                                recyclerView.adapter = HomeAdapter(list = listData) { click ->

                                    if (click != null) {
                                        Toast.makeText(this@MainActivity, click.title, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                        is ApiState.Failure -> {
                            Log.d(TAG, "@@@onCreate: Failed ${it.error}")
                            progress_circular.isVisible = false
                        }
                    }
                }
            }
            else{
                _binding!!.progressCircular.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Connection Not Available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isCheckInternetConnection(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }

    }

}