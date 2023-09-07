package com.socialite.domain.domain

fun interface IsServerActive {
    operator fun invoke(): Boolean
}