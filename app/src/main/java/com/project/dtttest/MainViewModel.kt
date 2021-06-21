package com.project.dtttest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.dtttest.model.HouseResponse
import com.project.dtttest.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel() {

    val myResponse: MutableLiveData<Response<List<HouseResponse>>> = MutableLiveData()

    fun getHouses() {
        viewModelScope.launch {
            val response = repository.getHouses()
            myResponse.value = response
        }
    }
}