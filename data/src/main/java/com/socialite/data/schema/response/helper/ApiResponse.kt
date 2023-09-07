package com.socialite.data.schema.response.helper

data class ApiResponse<T>(
    val message: ResponseMessage,
    val data: T?,
    val error: String?
)
