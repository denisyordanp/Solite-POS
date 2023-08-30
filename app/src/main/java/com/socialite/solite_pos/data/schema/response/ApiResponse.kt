package com.socialite.solite_pos.data.schema.response

data class ApiResponse<T>(
    var message: ResponseMessage,
    val data: T?,
    val error: String?
)
