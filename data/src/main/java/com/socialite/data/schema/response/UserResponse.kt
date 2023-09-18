package com.socialite.data.schema.response

import com.socialite.data.schema.room.master.User

data class UserResponse(
    val id: String,
    val name: String,
    val email: String,
    val authority: String,
) {
    fun toUser(): User {
        return User(
            id = id,
            name = name,
            email = email,
            authority = authority
        )
    }
}
