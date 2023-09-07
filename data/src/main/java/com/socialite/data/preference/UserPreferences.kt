package com.socialite.data.preference

interface UserPreferences {
    fun setUserToken(token: String)
    fun getUserToken(): String
}
