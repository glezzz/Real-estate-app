package com.project.dtttest.repository

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.project.dtttest.api.RetrofitInstance
import com.project.dtttest.model.HouseResponse
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.lang.Exception

class Repository {

    suspend fun getHouses(): Response<List<HouseResponse>> {
        try {
            return RetrofitInstance.api.getHouses()
        } catch (e: Exception) {
            Log.e("Repository", "Exception")
            e.printStackTrace()

            return Response.error(500, e.message?.toResponseBody()?:"Undetermined error".toResponseBody())
        }
    }
}