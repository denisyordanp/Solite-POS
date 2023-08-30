package com.socialite.solite_pos.network.helper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.socialite.solite_pos.data.schema.response.ApiResponse
import retrofit2.HttpException

object ResponseHandler {

    suspend fun <T> handleErrorMessage(fetch: suspend () -> ApiResponse<T>): ApiResponse<T> {
        return try {
            fetch()
        } catch (e: HttpException) {
            val response = e.response()
            val body = response?.errorBody()

            body?.let {
                val type = object : TypeToken<ApiResponse<T>>() {}.type
                return Gson().fromJson(it.string(), type)
            }

            throw e
        }
    }
}
