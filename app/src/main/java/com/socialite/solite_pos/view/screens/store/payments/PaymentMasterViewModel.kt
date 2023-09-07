package com.socialite.solite_pos.view.screens.store.payments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.domain.domain.AddNewPayment
import com.socialite.domain.domain.GetPayments
import com.socialite.domain.domain.UpdatePayment
import com.socialite.domain.schema.main.Payment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentMasterViewModel @Inject constructor(
    private val getPayments: GetPayments,
    private val addNewPayment: AddNewPayment,
    private val updatePayment: UpdatePayment,
) : ViewModel() {

    fun getAllPayments() = getPayments.invoke(Payment.Status.ALL)

    fun insertPayment(data: Payment) {
        viewModelScope.launch {
            addNewPayment(data.asNewPayment())
        }
    }

    fun updatePayment(data: Payment) {
        viewModelScope.launch {
            updatePayment.invoke(data)
        }
    }
}
