package com.socialite.solite_pos.view.opening

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.socialite.solite_pos.data.source.repository.RemoteConfigRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.di.RepositoryInjection

class OpeningViewModel(
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

    companion object {
        fun getFactory(activity: FragmentActivity) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return OpeningViewModel(
                    settingRepository = RepositoryInjection.provideSettingRepository(activity),
                    remoteConfigRepository = RepositoryInjection.provideRemoteConfigRepository(
                        activity
                    )
                ) as T
            }
        }
    }
}
