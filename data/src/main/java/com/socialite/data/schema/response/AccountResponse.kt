package com.socialite.data.schema.response

import com.socialite.schema.database.master.User

data class AccountResponse(
    val id: String,
    val name: String,
    val email: String,
    val authority: String,
    val token: String,
    val isActive: Boolean
) {
    fun toUser(): User {
        return User(
            id = id,
            name = name,
            email = email,
            authority = authority,
            active = isActive
        )
    }
}
