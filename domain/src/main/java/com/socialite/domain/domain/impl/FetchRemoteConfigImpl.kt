package com.socialite.domain.domain.impl

import com.socialite.data.repository.RemoteConfigRepository
import com.socialite.domain.domain.FetchRemoteConfig
import javax.inject.Inject

class FetchRemoteConfigImpl @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository
) : FetchRemoteConfig {
    override suspend fun invoke() = remoteConfigRepository.fetch()
}