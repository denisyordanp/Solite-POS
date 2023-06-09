package com.socialite.solite_pos.data.source.repository.impl

import android.util.Log
import com.socialite.solite_pos.builder.RemoteConfigManager
import com.socialite.solite_pos.data.source.repository.RemoteConfigRepository
import kotlinx.coroutines.flow.first

class RemoteConfigRepositoryImpl(
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
                Log.d("TESTING", "key: $key remote: $remote")
                remote
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TESTING", "exception: ${e.message}")
                defaultValue
            }
        } ?: defaultValue
    }
}