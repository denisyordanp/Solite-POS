package com.socialite.solite_pos.view.viewModel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.data.source.local.entity.room.master.Outcome
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import com.socialite.solite_pos.data.source.local.entity.room.master.Supplier
import com.socialite.solite_pos.data.source.repository.CustomersRepository
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import com.socialite.solite_pos.data.source.repository.PurchasesRepository
import com.socialite.solite_pos.data.source.repository.SuppliersRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val paymentRepository: PaymentsRepository,
    private val supplierRepository: SuppliersRepository,
    private val customersRepository: CustomersRepository,
    private val outcomesRepository: OutcomesRepository,
    private val purchasesRepository: PurchasesRepository,
) : ViewModel() {

    companion object : ViewModelFromFactory<MainViewModel>() {
        fun getMainViewModel(activity: FragmentActivity): MainViewModel {
            return buildViewModel(activity, MainViewModel::class.java)
        }
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

    fun insertCustomers(data: Customer) {
        viewModelScope.launch {
            customersRepository.insertCustomer(data)
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
}
