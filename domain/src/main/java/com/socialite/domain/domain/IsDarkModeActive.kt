package com.socialite.domain.domain

import kotlinx.coroutines.flow.Flow

fun interface IsDarkModeActive {
    suspend operator fun invoke(): Flow<Boolean>
}