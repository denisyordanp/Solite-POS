package com.socialite.data.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.socialite.common.utility.di.IoDispatcher
import com.socialite.data.datastore.DataStoreManager
import com.socialite.data.datastore.impl.DataStoreManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

private const val SETTING_DATASTORE = "setting_datastore"
private val Context.settingDataStore by preferencesDataStore(
    SETTING_DATASTORE
)

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    fun provideDataStoreManager(
        @ApplicationContext context: Context,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): DataStoreManager {
        return DataStoreManagerImpl(context.settingDataStore, dispatcher)
    }
}
