package com.socialite.solite_pos.data.domain

import kotlinx.coroutines.flow.Flow

fun interface IsShouldSelectStore {
    operator fun invoke(): Flow<Boolean>
}