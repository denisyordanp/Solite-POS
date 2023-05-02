package com.socialite.solite_pos.data.source.preference

import kotlinx.coroutines.flow.Flow

interface UserPreferences {
    suspend fun setUserToken(token: String)
    suspend fun getUserToken(): String?
}
