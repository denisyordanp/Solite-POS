package com.socialite.solite_pos.data.source.repository

interface RemoteConfigRepository {
    suspend fun fetch(): Boolean
    fun isServerActive(): Boolean
}