package com.socialite.solite_pos.utils.tools

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.socialite.solite_pos.data.source.domain.GetOrdersGeneralMenuBadge
import com.socialite.solite_pos.data.source.domain.MigrateToUUID
import com.socialite.solite_pos.data.source.domain.NewOutcome
import com.socialite.solite_pos.data.source.domain.Synchronize
import com.socialite.solite_pos.data.source.repository.CustomersRepository
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import com.socialite.solite_pos.data.source.repository.PromosRepository
import com.socialite.solite_pos.data.source.repository.RemoteConfigRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.data.source.repository.StoreRepository
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection.provideGetOrdersGeneralMenuBadge
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection.provideMigrateToUUID
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection.provideNewOutcome
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection.provideSynchronize
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.provideCustomersRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.provideOutcomesRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.providePaymentsRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.providePromosRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.provideRemoteConfigRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.provideSettingRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.provideStoreRepository
import com.socialite.solite_pos.view.settings.SettingViewModel
import com.socialite.solite_pos.view.viewModel.MainViewModel

class LoggedInViewModelFactory private constructor(
    private val paymentsRepository: PaymentsRepository,
    private val customersRepository: CustomersRepository,
    private val outcomesRepository: OutcomesRepository,
    private val getOrdersGeneralMenuBadge: GetOrdersGeneralMenuBadge,
    private val storeRepository: StoreRepository,
    private val settingRepository: SettingRepository,
    private val newOutcome: NewOutcome,
    private val promosRepository: PromosRepository,
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
                            paymentsRepository = providePaymentsRepository(context),
                            customersRepository = provideCustomersRepository(context),
                            outcomesRepository = provideOutcomesRepository(context),
                            getOrdersGeneralMenuBadge = provideGetOrdersGeneralMenuBadge(context),
                            storeRepository = provideStoreRepository(context),
                            settingRepository = provideSettingRepository(context),
                            newOutcome = provideNewOutcome(context),
                            promosRepository = providePromosRepository(context),
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
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(
                    paymentRepository = paymentsRepository,
                    customersRepository = customersRepository,
                    outcomesRepository = outcomesRepository,
                    storeRepository = storeRepository,
                    settingRepository = settingRepository,
                    newOutcome = newOutcome,
                    promosRepository = promosRepository,
                ) as T
            }

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
