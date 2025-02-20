package com.bytespectra.paginationinjetpackcompose.data.remote

import com.bytespectra.paginationinjetpackcompose.data.model.remote.PixabayImageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/")
    suspend fun getImages(
        @Query("key") apiKey: String = "48917453-8d324fe52178fd37539af4d27",
        @Query("q") query: String,
        @Query("page") page: Int,
    ): PixabayImageResponse

}