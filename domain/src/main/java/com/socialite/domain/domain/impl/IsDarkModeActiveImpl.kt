package com.socialite.domain.domain.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.data.repository.SettingRepository
import com.socialite.domain.domain.IsDarkModeActive
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn

class IsDarkModeActiveImpl constructor(
    private val settingRepository: SettingRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : IsDarkModeActive {
    override suspend fun invoke() = settingRepository.getIsDarkModeActive().flowOn(dispatcher)
}