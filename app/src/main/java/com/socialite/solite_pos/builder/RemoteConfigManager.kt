package com.socialite.solite_pos.builder

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale
import javax.inject.Inject

class RemoteConfigManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        fun getInstance(context: Context) = RemoteConfigManager(context)
    }

    enum class RemoteKeys {
        IS_SERVER_ACTIVE;

        override fun toString(): String {
            return super.toString().lowercase(Locale.US)
        }
    }

    fun getConfig(): FirebaseRemoteConfig? {
        if (FirebaseApp.getApps(context).isEmpty()) return null

        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        return remoteConfig
    }

    fun fetch(): Flow<Boolean> {
        return callbackFlow {
            getConfig()?.fetchAndActivate()
                ?.addOnCompleteListener {
                    trySend(it.isSuccessful)
                    channel.close()
                }
            awaitClose()
        }
    }
}
