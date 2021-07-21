package com.project.dtttest.ui.viewmodels

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.dtttest.model.HouseResponse
import com.project.dtttest.repository.HouseRepository
import kotlinx.coroutines.launch

class HouseViewModel(private val houseRepository: HouseRepository) : ViewModel() {

    enum class NetworkState {
        LOADING,
        SUCCESS,
        FAILED
    }

    val networkStatus: MutableLiveData<NetworkState> = MutableLiveData(NetworkState.LOADING)
    val error: MutableLiveData<String?> = MutableLiveData()

    val allHouses: MutableLiveData<List<HouseResponse>> = MutableLiveData()

    var userLocation: Location? = null
        set(value) {
            if (DEBUG ) {
                Log.d(TAG, "setUserLocation() location: $value")
            }
            field = value
            allHouses.value?.let { list ->
                list.forEach { house ->
                    house.userLocation = value
                }

                allHouses.postValue(ArrayList(list))
            }
        }

    fun getHouses() {
        viewModelScope.launch {
            networkStatus.value = NetworkState.LOADING

            val response = houseRepository.getHouses()

            if (response.isSuccessful) {
                networkStatus.value = NetworkState.SUCCESS

                // Location already set? network connection could be slow and the location could be already cached in the system
                if (userLocation != null) {
                    response.body()?.forEach { house ->
                        house.userLocation = userLocation
                    }
                }
                allHouses.value = response.body()
            } else {
                networkStatus.value = NetworkState.FAILED
                error.value = response.errorBody()?.string()?:"Undetermined error"
            }
        }
    }

    companion object {
        private val TAG = "MainViewModel"
        private const val DEBUG = true
    }
}