package com.socialite.solite_pos.data.source.repository

interface SynchronizationRepository {
    suspend fun synchronize(): Boolean
}
