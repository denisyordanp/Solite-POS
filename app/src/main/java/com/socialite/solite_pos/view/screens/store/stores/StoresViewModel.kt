package com.socialite.solite_pos.view.screens.store.stores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.data.schema.room.new_master.Store
import com.socialite.domain.domain.AddNewStore
import com.socialite.domain.domain.GetSelectedStore
import com.socialite.domain.domain.GetStores
import com.socialite.domain.domain.SelectStore
import com.socialite.domain.domain.UpdateStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoresViewModel @Inject constructor(
    private val addNewStore: AddNewStore,
    private val getStores: GetStores,
    private val updateStore: UpdateStore,
    private val getSelectedStore: GetSelectedStore,
    private val selectStore: SelectStore,
) : ViewModel() {

    fun getStores() = getStores.invoke()

    fun insertStore(store: Store) {
        viewModelScope.launch {
            addNewStore(store.asNewStore())
        }
    }

    fun updateStore(store: Store) {
        viewModelScope.launch {
            updateStore.invoke(store)
        }
    }

    val selectedStore get() = getSelectedStore()

    fun selectStore(id: String) {
        viewModelScope.launch {
            selectStore.invoke(id)
        }
    }
}
