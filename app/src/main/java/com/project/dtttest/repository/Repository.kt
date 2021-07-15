package com.project.dtttest.repository

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.project.dtttest.api.RetrofitInstance
import com.project.dtttest.model.HouseResponse
import retrofit2.Response

class Repository {

    suspend fun getHouses(): Response<List<HouseResponse>> {
        return RetrofitInstance.api.getHouses()
    }
}