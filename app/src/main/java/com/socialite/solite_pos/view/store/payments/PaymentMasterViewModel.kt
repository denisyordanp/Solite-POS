package com.socialite.solite_pos.view.store.payments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Payment
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentMasterViewModel @Inject constructor(
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
}
