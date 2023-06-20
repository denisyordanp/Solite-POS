package com.socialite.solite_pos.utils.tools

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.socialite.solite_pos.data.source.domain.GetOrdersGeneralMenuBadge
import com.socialite.solite_pos.data.source.domain.MigrateToUUID
import com.socialite.solite_pos.data.source.domain.Synchronize
import com.socialite.solite_pos.data.source.repository.RemoteConfigRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection.provideGetOrdersGeneralMenuBadge
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection.provideMigrateToUUID
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection.provideSynchronize
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.provideRemoteConfigRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.provideSettingRepository
import com.socialite.solite_pos.view.settings.SettingViewModel

class LoggedInViewModelFactory private constructor(
    private val getOrdersGeneralMenuBadge: GetOrdersGeneralMenuBadge,
    private val settingRepository: SettingRepository,
    private val synchronize: Synchronize,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val migrateToUUID: MigrateToUUID,
) : NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: LoggedInViewModelFactory? = null

        fun getInstance(context: Context): LoggedInViewModelFactory {
            if (INSTANCE == null) {
                synchronized(LoggedInViewModelFactory::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = LoggedInViewModelFactory(
                            getOrdersGeneralMenuBadge = provideGetOrdersGeneralMenuBadge(context),
                            settingRepository = provideSettingRepository(context),
                            synchronize = provideSynchronize(context),
                            remoteConfigRepository = provideRemoteConfigRepository(context),
                            migrateToUUID = provideMigrateToUUID(context),
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

            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(
                    migrateToUUID = migrateToUUID,
                    synchronize = synchronize,
                    settingRepository = settingRepository,
                    remoteConfigRepository = remoteConfigRepository,
                    getOrdersGeneralMenuBadge = getOrdersGeneralMenuBadge
                ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}
