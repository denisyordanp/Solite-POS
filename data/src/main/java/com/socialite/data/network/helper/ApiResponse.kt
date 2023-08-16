package com.socialite.solite_pos.network.helper

data class ApiResponse<T>(
    var message: ResponseMessage,
    val data: T?,
    val error: String?
)
