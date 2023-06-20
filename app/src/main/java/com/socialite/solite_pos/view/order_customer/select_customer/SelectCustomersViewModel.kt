package com.socialite.solite_pos.view.order_customer.select_customer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Customer
import com.socialite.solite_pos.data.source.repository.CustomersRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SelectCustomersViewModel(
    private val customersRepository: CustomersRepository,
) : ViewModel() {

    private val _viewState = MutableStateFlow(SelectCustomersViewState.idle())
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            customersRepository.getCustomers()
                .map {
                    _viewState.value.copy(
                        customers = it
                    )
                }
                .collect(_viewState)
        }
    }

    fun searchCustomer(keyword: String) {
        viewModelScope.launch {
            _viewState.emit(
                _viewState.value.copy(
                    keyword = keyword
                )
            )
        }
    }

    fun newCustomer(data: Customer) {
        viewModelScope.launch {
            customersRepository.insertCustomer(data)
        }
    }

    companion object {
        fun getFactory(context: Context) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SelectCustomersViewModel(
                    customersRepository = LoggedInRepositoryInjection.provideCustomersRepository(
                        context
                    )
                ) as T
            }
        }
    }
}
