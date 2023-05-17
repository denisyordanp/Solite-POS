package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.CustomersDao
import com.socialite.solite_pos.data.source.repository.CustomersRepository
import kotlinx.coroutines.flow.first
import java.util.UUID

class CustomersRepositoryImpl(
    private val dao: CustomersDao,
    private val db: AppDatabase
) : CustomersRepository {

    companion object {
        @Volatile
        private var INSTANCE: CustomersRepositoryImpl? = null

        fun getInstance(
            dao: CustomersDao,
            db: AppDatabase
        ): CustomersRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(CustomersRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = CustomersRepositoryImpl(dao = dao, db = db)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getCustomers() = dao.getCustomers()

    override suspend fun insertCustomer(data: Customer) = dao.insertCustomer(data)

    override suspend fun migrateToUUID() {
        val customers = dao.getCustomers().first()
        db.withTransaction {
            for (customer in customers) {
                dao.updateCustomer(customer.copy(
                    new_id = UUID.randomUUID().toString()
                ))
            }
        }
    }
}
