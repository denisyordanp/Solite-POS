package com.socialite.data.schema.response

import com.socialite.data.schema.room.master.User

data class LoginResponse(
    val id: String,
    val name: String,
    val email: String,
    val authority: String,
    val token: String
) {
    fun toUser(): User {
        return User(
            id = id,
            name = name,
            email = email,
            authority = authority,
            // TODO: Add user active on login response
            active = true
        )
    }
}
