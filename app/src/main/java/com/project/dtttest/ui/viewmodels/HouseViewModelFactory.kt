package com.project.dtttest.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.dtttest.repository.HouseRepository

class HouseViewModelFactory(
    private val houseRepository: HouseRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HouseViewModel(houseRepository) as T
    }
}