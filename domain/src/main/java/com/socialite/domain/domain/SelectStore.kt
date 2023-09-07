package com.socialite.domain.domain

fun interface SelectStore {
    suspend operator fun invoke(storeId: String)
}