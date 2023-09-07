package com.socialite.domain.domain

fun interface FetchRemoteConfig {
    suspend operator fun invoke(): Boolean
}