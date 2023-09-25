package com.socialite.domain.schema.main

import com.socialite.domain.menu.UserAuthority

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val authority: UserAuthority = UserAuthority.OWNER,
    val isUserActive: Boolean = false,
    val password: String = ""
) {

    val isNewUser get() = id == ADD_ID

    companion object {
        private const val ADD_ID = "add_id"
        const val ADD_OPTION_ID = -1L

        fun add(
            name: String,
            email: String,
            authority: UserAuthority,
            password: String
        ) = User(
            id = ADD_ID,
            name = name,
            email = email,
            authority = authority,
            password = password,
            isUserActive = true
        )

        fun addOption() = User(
            id = ADD_OPTION_ID.toString(),
            name = "Semua pengguna"
        )
    }
}
