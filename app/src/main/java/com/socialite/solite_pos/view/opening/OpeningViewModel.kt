package com.socialite.solite_pos.view.opening

import androidx.lifecycle.ViewModel
import com.socialite.solite_pos.data.source.repository.RemoteConfigRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OpeningViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
    private val remoteConfigRepository: RemoteConfigRepository
) : ViewModel() {

    suspend fun fetchRemoteConfig() {
        remoteConfigRepository.fetch()
    }

    fun isServerActive(): Boolean {
        return remoteConfigRepository.isServerActive()
    }

    fun isLoggedIn(): Boolean {
        return settingRepository.getToken().isNotEmpty()
    }
}
