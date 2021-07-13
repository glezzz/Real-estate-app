package com.project.dtttest.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.dtttest.repository.Repository
import com.project.dtttest.ui.viewmodels.MainViewModel

class MainViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}