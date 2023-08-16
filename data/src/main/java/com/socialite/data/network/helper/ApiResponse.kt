package com.socialite.data.network.helper

data class ApiResponse<T>(
    var message: ResponseMessage,
    val data: T?,
    val error: String?
)
