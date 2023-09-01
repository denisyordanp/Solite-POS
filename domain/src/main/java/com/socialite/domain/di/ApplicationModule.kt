package com.socialite.domain.di

import com.socialite.data.repository.SettingRepository
import com.socialite.domain.domain.IsDarkModeActive
import com.socialite.domain.domain.impl.IsDarkModeActiveImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    fun provideIsDarkModeActive(
        settingRepository: SettingRepository
    ): IsDarkModeActive = IsDarkModeActiveImpl(settingRepository)
}