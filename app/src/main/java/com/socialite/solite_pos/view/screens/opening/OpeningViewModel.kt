package com.socialite.solite_pos.view.screens.opening

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.repository.RemoteConfigRepository
import com.socialite.solite_pos.data.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OpeningViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
    private val remoteConfigRepository: RemoteConfigRepository
) : ViewModel() {

    fun fetchRemoteConfig(onDone: () -> Unit) {
        viewModelScope.launch {
            remoteConfigRepository.fetch()
            Handler(Looper.getMainLooper()).postDelayed({ onDone() }, OPENING_SCREEN_DELAYED)
        }
    }

    fun isServerActive(): Boolean {
        return remoteConfigRepository.isServerActive()
    }

    fun isLoggedIn(): Boolean {
        return settingRepository.getToken().isNotEmpty()
    }

    companion object {
        private const val OPENING_SCREEN_DELAYED = 1000L
    }
}
