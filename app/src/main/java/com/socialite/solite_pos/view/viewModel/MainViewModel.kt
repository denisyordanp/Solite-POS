package com.socialite.solite_pos.view.viewModel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.NewOutcome
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.data.source.local.entity.room.master.Outcome
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import com.socialite.solite_pos.data.source.local.entity.room.master.Store
import com.socialite.solite_pos.data.source.local.entity.room.master.Supplier
import com.socialite.solite_pos.data.source.repository.CustomersRepository
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import com.socialite.solite_pos.data.source.repository.PurchasesRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.data.source.repository.StoreRepository
import com.socialite.solite_pos.data.source.repository.SuppliersRepository
import com.socialite.solite_pos.utils.config.CashAmounts
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(
    private val paymentRepository: PaymentsRepository,
    private val supplierRepository: SuppliersRepository,
    private val customersRepository: CustomersRepository,
    private val outcomesRepository: OutcomesRepository,
    private val purchasesRepository: PurchasesRepository,
    private val storeRepository: StoreRepository,
    private val settingRepository: SettingRepository,
    private val newOutcome: NewOutcome
) : ViewModel() {

    companion object : ViewModelFromFactory<MainViewModel>() {
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

    val purchases = purchasesRepository.getPurchases()

    fun getPurchaseProducts(purchaseNo: String) =
        purchasesRepository.getPurchaseProducts(purchaseNo)

    fun newPurchase(data: PurchaseWithProduct) {
        viewModelScope.launch {
            purchasesRepository.newPurchase(data)
        }
    }

    val customers = customersRepository.getCustomers()

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

    fun insertCustomers(data: Customer, onSaved: (id: Long) -> Unit) {
        viewModelScope.launch {
            val id = customersRepository.insertCustomer(data)
            onSaved(id)
        }
    }

    val suppliers = supplierRepository.getSuppliers()

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

    val payments = paymentRepository.getPayments()

    fun insertPayment(data: Payment) {
        viewModelScope.launch {
            paymentRepository.insertPayment(data)
        }
    }

    fun updatePayment(data: Payment) {
        viewModelScope.launch {
            paymentRepository.updatePayment(data)
        }
    }

    fun getOutcome(date: String) = outcomesRepository.getOutcomes(date)

    fun addNewOutcome(outcome: Outcome) {
        viewModelScope.launch {
            newOutcome(outcome)
        }
    }
    fun insertOutcome(data: Outcome) {
        viewModelScope.launch {
            outcomesRepository.insertOutcome(data)
        }
    }

    fun updateOutcome(data: Outcome) {
        viewModelScope.launch {
            outcomesRepository.updateOutcome(data)
        }
    }

    fun getStores() = storeRepository.getStores()

    fun insertStore(store: Store) {
        viewModelScope.launch {
            storeRepository.insertStore(store)
        }
    }

    val selectedStore = settingRepository.getSelectedStore()

    fun selectStore(id: Long) {
        viewModelScope.launch {
            settingRepository.selectStore(id)
        }
    }
}
