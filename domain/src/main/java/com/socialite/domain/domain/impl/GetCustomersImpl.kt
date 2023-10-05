package com.socialite.domain.domain.impl

import com.socialite.common.utility.di.IoDispatcher
import com.socialite.data.repository.CustomersRepository
import com.socialite.domain.domain.GetCustomers
import com.socialite.domain.helper.toDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetCustomersImpl @Inject constructor(
    private val customersRepository: CustomersRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetCustomers {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke() = customersRepository.getCustomers()
        .mapLatest { customers -> customers.map { it.toDomain() } }
        .flowOn(dispatcher)
}