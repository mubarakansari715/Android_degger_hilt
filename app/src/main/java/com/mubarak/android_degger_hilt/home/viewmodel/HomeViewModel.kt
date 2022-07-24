package com.mubarak.android_degger_hilt.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mubarak.android_degger_hilt.home.repository.HomeRepository
import com.mubarak.room_demo_kotlin.utils.ApiState
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private val _mHomeData = MutableStateFlow<ApiState>(ApiState.Empty)
    val mStatus: StateFlow<ApiState> = _mHomeData

    fun fetchData() = viewModelScope.launch {
        _mHomeData.value = ApiState.Loading
        _mHomeData.value = ApiState.Success(repository.getDataFromRepository())
    }
}