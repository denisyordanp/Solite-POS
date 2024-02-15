package com.socialite.solite_pos.view.screens.store.stores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.domain.domain.AddNewStore
import com.socialite.domain.domain.GetSelectedStoreId
import com.socialite.domain.domain.GetStores
import com.socialite.domain.domain.IsUserStaff
import com.socialite.domain.domain.SelectStore
import com.socialite.domain.domain.UpdateStore
import com.socialite.schema.ui.main.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoresViewModel @Inject constructor(
    private val addNewStore: AddNewStore,
    private val getStores: GetStores,
    private val updateStore: UpdateStore,
    private val getSelectedStoreId: GetSelectedStoreId,
    private val selectStore: SelectStore,
    private val isUserStaff: IsUserStaff,
) : ViewModel() {

    fun isUserStaff() = isUserStaff.invoke()

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

    val selectedStore get() = getSelectedStoreId()

    fun selectStore(id: String) {
        viewModelScope.launch {
            selectStore.invoke(id)
        }
    }
}
