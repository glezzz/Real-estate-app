package com.project.dtttest.api

import com.project.dtttest.utils.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        val logging = buildLoggingInterceptor()
        val client = buildClient(logging)
        buildRetrofit(client)
    }

    val api: APIService by lazy {
        retrofit.create(APIService::class.java)
    }

    /**
     * Logs Retrofit responses
     */
    private fun buildLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()

        // Log the actual response
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return logging
    }

    /**
     * Creates network client
     */
    private fun buildClient(logging: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    /**
     * Builds a new Retrofit
     */
    private fun buildRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)

            // Add converter factory for serialization and deserialization of objects
            .addConverterFactory(GsonConverterFactory.create())

            // The HTTP client used for requests
            .client(client)
            .build()
    }
}