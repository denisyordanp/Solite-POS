package com.socialite.data.schema.response.helper

data class ApiResponse<T>(
    var message: ResponseMessage,
    val data: T?,
    val error: String?
)
