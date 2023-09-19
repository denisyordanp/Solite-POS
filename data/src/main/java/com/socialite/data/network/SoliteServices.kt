package com.socialite.data.network

import com.socialite.common.network.response.ApiResponse
import com.socialite.data.schema.response.LoginResponse
import com.socialite.data.schema.response.SynchronizeParams
import com.socialite.data.schema.response.SynchronizeResponse
import com.socialite.data.schema.response.UserResponse
import com.socialite.data.schema.response.UserStoreResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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
    ): ApiResponse<UserResponse>

    @FormUrlEncoded
    @PUT("v1/user_on_store")
    suspend fun updateUser(
        @Field("id") id: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("authority") authority: String,
        @Field("is_active") isActive: String
    ): ApiResponse<String?>

    @GET("v1/user_store")
    suspend fun getUsers(): ApiResponse<List<UserStoreResponse>>

    @GET("v1/user")
    suspend fun getUser(): ApiResponse<UserStoreResponse>

    @POST("v1/synchronize")
    suspend fun synchronize(
        @Body synchronize: SynchronizeParams
    ): ApiResponse<SynchronizeResponse>
}
