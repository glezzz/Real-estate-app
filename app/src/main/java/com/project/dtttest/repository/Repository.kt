package com.project.dtttest.repository

import com.project.dtttest.api.RetrofitInstance
import com.project.dtttest.model.HouseResponse
import retrofit2.Response

class Repository {

    suspend fun getHouses(): Response<List<HouseResponse>> {
        return RetrofitInstance.api.getHouses()
    }
}