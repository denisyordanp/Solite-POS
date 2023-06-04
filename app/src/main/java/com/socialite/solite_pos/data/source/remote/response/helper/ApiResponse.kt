package com.socialite.solite_pos.data.source.remote.response.helper

data class ApiResponse<T>(
    var message: ResponseMessage,
    val data: T?,
    val error: String?
)
