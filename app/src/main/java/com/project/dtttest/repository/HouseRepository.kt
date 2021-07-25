package com.project.dtttest.repository

import com.project.dtttest.api.RetrofitInstance
import com.project.dtttest.model.HouseResponse
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class HouseRepository {

    suspend fun getHouses(): Response<List<HouseResponse>> {
        return try {
            RetrofitInstance.api.getHouses()
        } catch (e: Exception) {

            // Error handling for example when setting Airplane mode
            e.printStackTrace()
            Response.error(
                500,
                e.message?.toResponseBody() ?: "Undetermined error".toResponseBody()
            )
        }
    }
}