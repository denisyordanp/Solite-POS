package com.socialite.solite_pos.utils.tools

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.socialite.solite_pos.data.source.domain.LoginUser
import com.socialite.solite_pos.data.source.domain.RegisterUser
import com.socialite.solite_pos.data.source.repository.RemoteConfigRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.di.DomainInjection.provideLoginUser
import com.socialite.solite_pos.di.DomainInjection.provideRegisterUser
import com.socialite.solite_pos.di.RepositoryInjection.provideRemoteConfigRepository
import com.socialite.solite_pos.di.RepositoryInjection.provideSettingRepository
import com.socialite.solite_pos.view.login.LoginViewModel
import com.socialite.solite_pos.view.opening.OpeningViewModel

class ViewModelFactory private constructor(
    private val loginUser: LoginUser,
    private val registerUser: RegisterUser,
    private val settingRepository: SettingRepository,
    private val remoteConfigRepository: RemoteConfigRepository
) : NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = ViewModelFactory(
                            loginUser = provideLoginUser(context),
                            registerUser = provideRegisterUser(context),
                            settingRepository = provideSettingRepository(context),
                            remoteConfigRepository = provideRemoteConfigRepository(context)
                        )
                    }
                }
            }
            return INSTANCE!!
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(
                    loginUser = loginUser,
                    registerUser = registerUser
                ) as T
            }

            modelClass.isAssignableFrom(OpeningViewModel::class.java) -> {
                OpeningViewModel(
                    settingRepository = settingRepository,
                    remoteConfigRepository = remoteConfigRepository
                ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}
