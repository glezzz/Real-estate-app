package com.project.dtttest.api

import com.project.dtttest.model.HouseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface APIService {

    @Headers("Access-Key: 98bww4ezuzfePCYFxJEWyszbUXc7dxRx")
    @GET("api/house")
    suspend fun getHouses(): Response<List<HouseResponse>>
}