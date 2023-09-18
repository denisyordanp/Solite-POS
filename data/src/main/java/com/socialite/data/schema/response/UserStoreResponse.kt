package com.socialite.data.schema.response

data class UserStoreResponse(
    val id: Int,
    val user: UserResponse,
    val isActive: Boolean
)
