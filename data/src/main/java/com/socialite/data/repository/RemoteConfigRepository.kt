package com.socialite.data.repository

interface RemoteConfigRepository {
    suspend fun fetch(): Boolean
    fun isServerActive(): Boolean
}
