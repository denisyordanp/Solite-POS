package com.socialite.domain.domain.impl

import com.socialite.data.repository.CustomersRepository
import com.socialite.domain.domain.GetCustomers
import com.socialite.domain.helper.toDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetCustomersImpl @Inject constructor(
    private val customersRepository: CustomersRepository,
) : GetCustomers {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke() = customersRepository.getCustomers()
        .mapLatest { customers -> customers.map { it.toDomain() } }
}