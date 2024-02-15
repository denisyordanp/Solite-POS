package com.socialite.domain.domain.impl

import com.socialite.common.utility.di.IoDispatcher
import com.socialite.data.repository.SettingRepository
import com.socialite.domain.domain.GetSelectedStoreId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSelectedStoreIdImpl @Inject constructor(
    private val settingRepository: SettingRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetSelectedStoreId {
    override fun invoke() = settingRepository.getNewSelectedStore().flowOn(dispatcher)
}