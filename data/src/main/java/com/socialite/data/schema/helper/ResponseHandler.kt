package com.socialite.data.schema.helper

import com.google.gson.reflect.TypeToken
import com.socialite.core.network.NetworkConfig
import com.socialite.core.network.response.ApiResponse
import com.socialite.core.network.response.ResponseMessage
import retrofit2.HttpException
import java.net.UnknownHostException

object ResponseHandler {

    suspend fun <T> handleErrorMessage(fetch: suspend () -> ApiResponse<T>): ApiResponse<T> {
        return try {
            fetch()
        } catch (e: HttpException) {
            val response = e.response()
            val body = response?.errorBody()

            body?.let {
                val type = object : TypeToken<ApiResponse<T>>() {}.type
                val gson = NetworkConfig.gson()
                return gson.fromJson(it.string(), type)
            }

            throw e
        } catch (e: UnknownHostException) {
            return ApiResponse(
                message = ResponseMessage.Failed,
                data = null,
                error = "Connection error"
            )
        }
    }
}
