package com.socialite.domain.domain

fun interface SetDarkMode {
    suspend operator fun invoke(isActive: Boolean)
}