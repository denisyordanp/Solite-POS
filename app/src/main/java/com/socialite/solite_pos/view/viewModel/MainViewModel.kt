package com.socialite.solite_pos.view.viewModel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseProductWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.helper.PurchaseWithSupplier
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.data.source.local.entity.room.master.Outcome
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import com.socialite.solite_pos.data.source.local.entity.room.master.Supplier
import com.socialite.solite_pos.data.source.remote.response.helper.ApiResponse
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import com.socialite.solite_pos.data.source.repository.SoliteRepository
import com.socialite.solite_pos.data.source.repository.SuppliersRepository
import com.socialite.solite_pos.vo.Resource
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: SoliteRepository,
    private val paymentRepository: PaymentsRepository,
    private val supplierRepository: SuppliersRepository
) : ViewModel() {

    companion object : ViewModelFromFactory<MainViewModel>() {
        fun getMainViewModel(activity: FragmentActivity): MainViewModel {
            return buildViewModel(activity, MainViewModel::class.java)
        }
    }

    val purchases: LiveData<Resource<List<PurchaseWithSupplier>>>
        get() = repository.purchases

    fun getPurchaseProducts(purchaseNo: String): LiveData<Resource<List<PurchaseProductWithProduct>>> {
        return repository.getPurchaseProducts(purchaseNo)
    }

    fun newPurchase(data: PurchaseWithProduct, callback: (ApiResponse<Boolean>) -> Unit) {
        repository.newPurchase(data, callback)
    }

    val customers: LiveData<Resource<List<Customer>>>
        get() = repository.customers

    fun insertCustomers(data: Customer, callback: (ApiResponse<Long>) -> Unit) {
        return repository.insertCustomer(data, callback)
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

    fun getOutcome(date: String): LiveData<Resource<List<Outcome>>> {
        return repository.getOutcomes(date)
    }

    fun insertOutcome(data: Outcome, callback: (ApiResponse<Long>) -> Unit) {
        repository.insertOutcome(data, callback)
    }

    fun updateOutcome(data: Outcome, callback: (ApiResponse<Boolean>) -> Unit) {
        repository.updateOutcome(data, callback)
    }
}
