package com.project.dtttest.repository

import com.project.dtttest.api.RetrofitInstance
import com.project.dtttest.model.HouseResponse

class Repository {

    suspend fun getHouses(): List<HouseResponse> {
        return RetrofitInstance.api.getHouses()
    }
}