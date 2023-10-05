package com.socialite.core.network.response

data class ApiResponse<T>(
    val message: ResponseMessage,
    val data: T?,
    val error: String?
)
