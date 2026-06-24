package com.ekanurulfalah0018.asessment3.network

import com.ekanurulfalah0018.asessment3.model.Sepatu
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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
    suspend fun getSepatu(
    ): List<Sepatu>


    @POST("asessment3/produksepatu")
    suspend fun postSepatu(
        @Body sepatu: Sepatu
    ): Sepatu

    @DELETE("asessment3/produksepatu/{id}")
    suspend fun deleteSepatu(
        @Path("id") id: String
    )

    @PUT("asessment3/produksepatu/{id}")
    suspend fun updateSepatu(
        @Path("id") id: String,
        @Body sepatu: Sepatu
    ): Sepatu
}

object SepatuApi {
    val service: SepatuApiService by lazy {
        retrofit.create(SepatuApiService::class.java)
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }