package com.socialite.solite_pos.view.store.stores

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Store
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.data.source.repository.StoreRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection
import kotlinx.coroutines.launch

class StoresViewModel(
    private val storeRepository: StoreRepository,
    private val settingRepository: SettingRepository,
) : ViewModel() {

    fun getStores() = storeRepository.getStores()

    fun insertStore(store: Store) {
        viewModelScope.launch {
            storeRepository.insertStore(store.asNewStore())
        }
    }

    fun updateStore(store: Store) {
        viewModelScope.launch {
            storeRepository.updateStore(store)
        }
    }

    val selectedStore = settingRepository.getNewSelectedStore()

    fun selectStore(id: String) {
        viewModelScope.launch {
            settingRepository.selectNewStore(id)
        }
    }

    companion object {
        fun getFactory(context: Context) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return StoresViewModel(
                    storeRepository = LoggedInRepositoryInjection.provideStoreRepository(context),
                    settingRepository = LoggedInRepositoryInjection.provideSettingRepository(context)
                ) as T
            }
        }
    }
}
