package com.socialite.solite_pos.view.screens.store.stores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.data.schema.room.new_master.Store
import com.socialite.data.repository.SettingRepository
import com.socialite.data.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoresViewModel @Inject constructor(
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
}
