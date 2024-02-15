package com.socialite.data.schema.response

import com.socialite.schema.database.master.User

data class UserStoreResponse(
    val id: Int,
    val user: UserResponse,
    val isActive: Boolean
) {
    fun toEntity(): User {
        return User(
            id = id.toString(),
            name = user.name,
            email = user.email,
            authority = user.authority,
            active = isActive
        )
    }
}
