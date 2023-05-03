package com.socialite.solite_pos.data.source.preference

interface UserPreferences {
    suspend fun setUserToken(token: String)
    suspend fun getUserToken(): String?
}
