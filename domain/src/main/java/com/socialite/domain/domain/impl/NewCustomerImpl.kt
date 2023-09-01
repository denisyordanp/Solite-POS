package com.socialite.domain.domain.impl

import com.socialite.data.repository.CustomersRepository
import com.socialite.domain.domain.NewCustomer
import com.socialite.domain.helper.toData
import com.socialite.domain.schema.main.Customer
import javax.inject.Inject

class NewCustomerImpl @Inject constructor(
    private val customersRepository: CustomersRepository,
) : NewCustomer {
    override suspend fun invoke(customer: Customer) {
        customersRepository.insertCustomer(customer.toData())
    }
}