package com.socialite.solite_pos.view.screens.opening

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.domain.domain.FetchRemoteConfig
import com.socialite.domain.domain.GetToken
import com.socialite.domain.domain.IsServerActive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OpeningViewModel @Inject constructor(
    private val getToken: GetToken,
    private val fetchRemoteConfig: FetchRemoteConfig,
    private val isServerActive: IsServerActive
) : ViewModel() {

    fun fetchRemoteConfig(onDone: () -> Unit) {
        viewModelScope.launch {
            fetchRemoteConfig.invoke()
            Handler(Looper.getMainLooper()).postDelayed({ onDone() }, OPENING_SCREEN_DELAYED)
        }
    }

    fun isServerActive() = isServerActive.invoke()

    fun isLoggedIn(): Boolean {
        return getToken().isNotEmpty()
    }

    companion object {
        private const val OPENING_SCREEN_DELAYED = 1000L
    }
}
