package com.socialite.solite_pos.view.viewModel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.domain.NewOutcome
import com.socialite.solite_pos.data.source.local.entity.room.master.Supplier
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Customer
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Outcome
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Payment
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Promo
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Store
import com.socialite.solite_pos.data.source.repository.CustomersRepository
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import com.socialite.solite_pos.data.source.repository.PromosRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.data.source.repository.StoreRepository
import com.socialite.solite_pos.data.source.repository.SuppliersRepository
import com.socialite.solite_pos.utils.config.CashAmounts
import com.socialite.solite_pos.utils.tools.helper.ReportsParameter
import com.socialite.solite_pos.view.factory.LoggedInViewModelFromFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(
    private val paymentRepository: PaymentsRepository,
    private val supplierRepository: SuppliersRepository,
    private val customersRepository: CustomersRepository,
    private val outcomesRepository: OutcomesRepository,
    private val storeRepository: StoreRepository,
    private val settingRepository: SettingRepository,
    private val promosRepository: PromosRepository,
    private val newOutcome: NewOutcome,
) : ViewModel() {

    companion object : LoggedInViewModelFromFactory<MainViewModel>() {
        fun getMainViewModel(activity: FragmentActivity): MainViewModel {
            return buildViewModel(activity, MainViewModel::class.java)
        }
    }

    private val _currentCashInput = MutableStateFlow(Pair(0L, 0L))

    @ExperimentalCoroutinesApi
    val cashSuggestions: Flow<List<Long>?> = _currentCashInput.flatMapLatest { input ->
        flow {
            if (input.first != 0L) {
                val cash = CashAmounts.generateCash(input.second).filter {
                    it.toString().startsWith(input.first.toString(), ignoreCase = true)
                            && it >= input.second
                }
                emit(cash)
            } else {
                emit(null)
            }
        }
    }

    fun addCashInput(cash: Long, total: Long) {
        _currentCashInput.value = Pair(cash, total)
    }

    private val customers = customersRepository.getCustomers()

    fun filterCustomer(keyword: String) = when {
        keyword.isNotEmpty() -> {
            customers
                .map {
                    it.filter { customer ->
                        customer.name.contains(keyword, ignoreCase = true)
                    }
                }
        }

        else -> customers
    }

    fun insertCustomers(data: Customer) {
        viewModelScope.launch {
            customersRepository.insertCustomer(data)
        }
    }

    fun insertSupplier(data: Supplier) {
        viewModelScope.launch {
            supplierRepository.insertSupplier(data)
        }
    }

    fun updateSupplier(data: Supplier) {
        viewModelScope.launch {
            supplierRepository.updateSupplier(data)
        }
    }

    fun getPayments(query: SupportSQLiteQuery) = paymentRepository.getPayments(query)

    fun insertPayment(data: Payment) {
        viewModelScope.launch {
            paymentRepository.insertPayment(data.asNewPayment())
        }
    }

    fun updatePayment(data: Payment) {
        viewModelScope.launch {
            paymentRepository.updatePayment(data)
        }
    }

    fun getPromos(status: Promo.Status): Flow<List<Promo>> {
        return promosRepository.getPromos(Promo.filter(status))
    }

    fun insertPromo(data: Promo) {
        viewModelScope.launch {
            promosRepository.insertPromo(data.asNewPromo())
        }
    }

    fun updatePromo(data: Promo) {
        viewModelScope.launch {
            promosRepository.updatePromo(data)
        }
    }

    fun getOutcome(parameters: ReportsParameter) = outcomesRepository.getOutcomes(parameters)

    fun addNewOutcome(outcome: Outcome) {
        viewModelScope.launch {
            newOutcome(outcome)
        }
    }

    fun getStores() = storeRepository.getStores()

    suspend fun getStore(): Store? {
        val selected = settingRepository.getNewSelectedStore().first()
        return storeRepository.getStore(selected)
    }

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
