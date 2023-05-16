package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.MigrateToUUID
import com.socialite.solite_pos.data.source.repository.CustomersRepository

class MigrateToUUIDImpl(
    private val customersRepository: CustomersRepository
) : MigrateToUUID {
    override suspend fun invoke() {
        customersRepository.migrateToUUID()
    }
}