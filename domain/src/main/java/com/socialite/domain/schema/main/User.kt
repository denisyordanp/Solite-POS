package com.socialite.domain.schema.main

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val authority: String = "",
    val isUserActive: Boolean = false,
    val password: String = ""
) {
    companion object {
        fun add(
            name: String,
            email: String,
            authority: String,
            password: String
        ) = User(
            id = "",
            name = name,
            email = email,
            authority = authority,
            password = password
        )
    }
}
