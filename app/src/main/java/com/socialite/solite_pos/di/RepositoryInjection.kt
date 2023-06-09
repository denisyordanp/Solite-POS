package com.socialite.solite_pos.di

import android.content.Context
import com.socialite.solite_pos.builder.RemoteConfigManager
import com.socialite.solite_pos.data.source.preference.impl.UserPreferencesImpl
import com.socialite.solite_pos.data.source.repository.AccountRepository
import com.socialite.solite_pos.data.source.repository.RemoteConfigRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.data.source.repository.impl.AccountRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.RemoteConfigRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.SettingRepositoryImpl

object RepositoryInjection {

    fun provideUserRepository(): AccountRepository {
        val service = NetworkInjector.provideSoliteServices()
        return AccountRepositoryImpl(service)
    }

    fun provideRemoteConfigRepository(context: Context): RemoteConfigRepository {
        return RemoteConfigRepositoryImpl(RemoteConfigManager.getInstance(context))
    }

    fun provideSettingRepository(context: Context): SettingRepository {
        val userPreferences = UserPreferencesImpl.getInstance(context)
        return SettingRepositoryImpl.getDataStoreInstance(context, userPreferences)
    }
}
