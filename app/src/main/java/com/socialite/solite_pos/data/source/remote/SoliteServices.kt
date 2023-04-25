package com.socialite.solite_pos.data.source.remote

import com.socialite.solite_pos.data.source.remote.response.entity.TokenResponse
import com.socialite.solite_pos.data.source.remote.response.helper.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SoliteServices {
    @GET("v1/login")
    suspend fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): ApiResponse<TokenResponse>

    @GET("v1/register")
    suspend fun register(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("store") storeName: String
    ): ApiResponse<TokenResponse>
}
