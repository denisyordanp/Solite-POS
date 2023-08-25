package com.socialite.domain.domain.impl

import com.socialite.data.repository.CustomersRepository
import com.socialite.data.schema.room.new_master.Customer
import com.socialite.domain.domain.NewCustomer
import javax.inject.Inject

class NewCustomerImpl @Inject constructor(
    private val customersRepository: CustomersRepository,
) : NewCustomer {
    override suspend fun invoke(customer: Customer) {
        customersRepository.insertCustomer(customer)
    }
}