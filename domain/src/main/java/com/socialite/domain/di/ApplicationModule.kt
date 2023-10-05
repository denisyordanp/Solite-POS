package com.socialite.domain.di

import com.socialite.common.utility.di.IoDispatcher
import com.socialite.data.repository.SettingRepository
import com.socialite.domain.domain.IsDarkModeActive
import com.socialite.domain.domain.impl.IsDarkModeActiveImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher


@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    fun provideIsDarkModeActive(
        settingRepository: SettingRepository,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): IsDarkModeActive = IsDarkModeActiveImpl(settingRepository, dispatcher)
}