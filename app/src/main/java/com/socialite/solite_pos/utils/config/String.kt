package com.socialite.solite_pos.utils.config

import java.util.regex.Pattern

fun String.rupiahToK() = this.replace(",000", "K")

fun String.isNotValidEmail(): Boolean {
    return !android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isNotValidPassword(): PasswordStatus {
    val upperLowerPattern = Pattern.compile("[a-zA-Z]")
    val symbolPattern = Pattern.compile("[^a-zA-Z0-9]")
    return when {
        this.length < 8 || this.length > 15 -> PasswordStatus.LENGTH
        upperLowerPattern.matcher(this).find().not() -> PasswordStatus.UPPER_LOWER_CASE
        symbolPattern.matcher(this).find().not() -> PasswordStatus.SYMBOL
        else -> PasswordStatus.SUCCESS
    }
}
