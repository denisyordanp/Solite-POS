package com.socialite.solite_pos.data.repository.impl

import com.socialite.solite_pos.network.RemoteConfigManager
import com.socialite.solite_pos.data.repository.RemoteConfigRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RemoteConfigRepositoryImpl @Inject constructor(
    private val remoteConfig: RemoteConfigManager
) : RemoteConfigRepository {

    override suspend fun fetch(): Boolean {
        return remoteConfig.fetch().first()
    }

    override fun isServerActive(): Boolean {
        val key = RemoteConfigManager.RemoteKeys.IS_SERVER_ACTIVE.toString()
        val defaultValue = false
        val default = mapOf(Pair(key, defaultValue))

        return remoteConfig.getConfig()?.let {
            try {
                it.setDefaultsAsync(default)
                val remote = it.getBoolean(key)
                remote
            } catch (e: Exception) {
                e.printStackTrace()
                defaultValue
            }
        } ?: defaultValue
    }
}
