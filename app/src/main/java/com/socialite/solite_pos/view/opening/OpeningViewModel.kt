package com.socialite.solite_pos.view.opening

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.socialite.solite_pos.data.source.repository.RemoteConfigRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.view.factory.ViewModelFromFactory

class OpeningViewModel(
    private val settingRepository: SettingRepository,
    private val remoteConfigRepository: RemoteConfigRepository
) : ViewModel() {
    companion object : ViewModelFromFactory<OpeningViewModel>() {
        fun getMainViewModel(activity: FragmentActivity): OpeningViewModel {
            return buildViewModel(activity, OpeningViewModel::class.java)
        }
    }



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