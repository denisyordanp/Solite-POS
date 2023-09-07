package com.socialite.domain.domain.impl

import com.socialite.data.repository.RemoteConfigRepository
import com.socialite.domain.domain.IsServerActive
import javax.inject.Inject

class IsServerActiveImpl @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository
) : IsServerActive {
    override fun invoke() = remoteConfigRepository.isServerActive()
}