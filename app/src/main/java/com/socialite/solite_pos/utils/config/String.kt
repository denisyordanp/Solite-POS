package com.socialite.solite_pos.utils.config

fun String.rupiahToK() = this.replace(",000", "K")

fun String.isNotValidEmail(): Boolean {
    return !android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isNotValidPassword(): Boolean {
    return when {
        this.length < 8 -> true
        else -> false
    }
}
