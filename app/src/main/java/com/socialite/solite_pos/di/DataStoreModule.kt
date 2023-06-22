package com.socialite.solite_pos.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

private const val SETTING_DATASTORE = "setting_datastore"
private val Context.settingDataStore by preferencesDataStore(
    SETTING_DATASTORE
)

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    fun provideSettingDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.settingDataStore
    }
}
