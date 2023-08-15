package com.socialite.solite_pos.data.preference

interface UserPreferences {
    fun setUserToken(token: String)
    fun getUserToken(): String
}
