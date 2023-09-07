package com.socialite.domain.domain.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.data.repository.SettingRepository
import com.socialite.domain.domain.GetSelectedStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSelectedStoreImpl @Inject constructor(
    private val settingRepository: SettingRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetSelectedStore {
    override fun invoke() = settingRepository.getNewSelectedStore().flowOn(dispatcher)
}