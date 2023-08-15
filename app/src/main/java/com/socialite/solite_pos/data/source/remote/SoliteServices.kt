package com.socialite.solite_pos.data.source.remote

import com.socialite.solite_pos.data.schema.response.SynchronizeParams
import com.socialite.solite_pos.data.schema.response.SynchronizeResponse
import com.socialite.solite_pos.data.schema.response.TokenResponse
import com.socialite.solite_pos.data.source.remote.response.helper.ApiResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SoliteServices {
    @GET("v1/login")
    suspend fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): ApiResponse<TokenResponse>

    @FormUrlEncoded
    @POST("v1/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("store") storeName: String
    ): ApiResponse<TokenResponse>

    @POST("v1/synchronize")
    suspend fun synchronize(
        @Body synchronize: SynchronizeParams
    ): ApiResponse<SynchronizeResponse>
}
