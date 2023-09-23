package com.socialite.domain.menu

enum class UserAuthority {
    OWNER, ADMIN, STAFF
}

fun String.toAuthority(): UserAuthority {
    return when (this.lowercase()) {
        UserAuthority.OWNER.name.lowercase() -> UserAuthority.OWNER
        UserAuthority.ADMIN.name.lowercase() -> UserAuthority.ADMIN
        UserAuthority.STAFF.name.lowercase() -> UserAuthority.STAFF
        else -> UserAuthority.OWNER
    }
}