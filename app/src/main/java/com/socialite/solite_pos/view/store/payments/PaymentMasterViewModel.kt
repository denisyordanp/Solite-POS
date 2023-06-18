package com.socialite.solite_pos.view.store.payments

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Payment
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection
import kotlinx.coroutines.launch

class PaymentMasterViewModel(
    private val paymentRepository: PaymentsRepository,
) : ViewModel() {

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

    companion object {
        fun getFactory(context: Context) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PaymentMasterViewModel(
                    paymentRepository = LoggedInRepositoryInjection.providePaymentsRepository(context)
                ) as T
            }
        }
    }
}
