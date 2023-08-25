package com.socialite.domain.domain.impl

import com.socialite.data.repository.CustomersRepository
import com.socialite.domain.domain.GetCustomers
import javax.inject.Inject

class GetCustomersImpl @Inject constructor(
    private val customersRepository: CustomersRepository,
) : GetCustomers {
    override fun invoke() = customersRepository.getCustomers()
}