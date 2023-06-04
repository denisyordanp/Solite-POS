package com.socialite.solite_pos.data.source.preference

interface UserPreferences {
    fun setUserToken(token: String)
    fun getUserToken(): String
}
