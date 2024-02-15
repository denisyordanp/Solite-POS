package com.socialite.solite_pos.schema

import com.socialite.schema.ui.utility.UserAuthority
import com.socialite.solite_pos.view.ui.DropdownItem

data class User(
    val id: String = "",
    override val name: String = "",
    val email: String = "",
    val authority: UserAuthority = UserAuthority.OWNER,
    val isUserActive: Boolean = false,
    val password: String = ""
) : DropdownItem {

    companion object {
        private const val ADD_ID = "add_id"
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
    }
}
