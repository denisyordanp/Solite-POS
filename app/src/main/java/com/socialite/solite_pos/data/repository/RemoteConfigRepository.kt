package com.socialite.solite_pos.data.repository

interface RemoteConfigRepository {
    suspend fun fetch(): Boolean
    fun isServerActive(): Boolean
}
