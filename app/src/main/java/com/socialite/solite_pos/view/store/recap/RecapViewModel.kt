package com.socialite.solite_pos.view.store.recap

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.GetOrderMenusWithAmount
import com.socialite.solite_pos.data.source.domain.GetRecapData
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Store
import com.socialite.solite_pos.data.source.repository.StoreRepository
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RecapViewModel(
    private val storeRepository: StoreRepository,
    private val getRecapData: GetRecapData,
    private val getOrderMenusWithAmount: GetOrderMenusWithAmount
) : ViewModel() {
    private val _viewState = MutableStateFlow(RecapViewState.idle())
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            storeRepository.getStores()
                .map {
                    _viewState.value.copy(
                        stores = it
                    )
                }.collect(_viewState)
        }

        _viewState.onEach {
            val parameters = it.getParameters()
            val recap = getRecapData(parameters).first()
            val menusWithAmount = getOrderMenusWithAmount(parameters).first()
            _viewState.emit(
                _viewState.value.copy(
                    recap = recap,
                    menus = menusWithAmount
                )
            )
        }.launchIn(viewModelScope)
    }

    fun selectDate(selectedDate: Pair<String, String>) {
        viewModelScope.launch {
            _viewState.emit(
                _viewState.value.copy(
                    selectedDate = selectedDate
                )
            )
        }
    }

    fun selectStore(store: Store) {
        viewModelScope.launch {
            _viewState.emit(
                _viewState.value.copy(
                    selectedStore = store
                )
            )
        }
    }

    companion object {
        fun getFactory(context: Context) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RecapViewModel(
                    storeRepository = LoggedInRepositoryInjection.provideStoreRepository(context),
                    getRecapData = LoggedInDomainInjection.provideGetIncomesRecapData(context),
                    getOrderMenusWithAmount = LoggedInDomainInjection.provideGetOrderMenusWithAmount(
                        context
                    )
                ) as T
            }
        }
    }
}