package com.project.dtttest.api

import com.project.dtttest.model.HouseResponse
import com.project.dtttest.utils.Constants.Companion.ACCESS_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface APIService {

    @Headers("Access-Key: $ACCESS_KEY")
    @GET("api/house")
    suspend fun getHouses(): Response<List<HouseResponse>>
}