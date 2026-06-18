package com.ekanurulfalah0018.asessment3.network

import com.ekanurulfalah0018.asessment3.model.Sepatu
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://6a343c418248ee962fa5436e.mockapi.io/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface SepatuApiService {
    @GET("asessment3/produksepatu")
    suspend fun getSepatu(): List<Sepatu>
}

object SepatuApi {
    val service: SepatuApiService by lazy {
        retrofit.create(SepatuApiService::class.java)
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }