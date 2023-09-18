package com.socialite.data.network

import com.socialite.common.network.response.ApiResponse
import com.socialite.data.schema.response.LoginResponse
import com.socialite.data.schema.response.SynchronizeParams
import com.socialite.data.schema.response.SynchronizeResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SoliteServices {
    @GET("v2/login")
    suspend fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): ApiResponse<LoginResponse>

    @FormUrlEncoded
    @POST("v2/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("store") storeName: String
    ): ApiResponse<LoginResponse>

    @FormUrlEncoded
    @POST("v1/user_on_store")
    suspend fun addUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("authority") authority: String
    ): ApiResponse<LoginResponse>

    @POST("v1/synchronize")
    suspend fun synchronize(
        @Body synchronize: SynchronizeParams
    ): ApiResponse<SynchronizeResponse>
}
