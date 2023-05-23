package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Customer
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.CustomersDao
import com.socialite.solite_pos.data.source.repository.CustomersRepository
import kotlinx.coroutines.flow.firstOrNull
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

    override fun getCustomers() = dao.getNewCustomers()
    override suspend fun getNeedUploadCustomers() = dao.getNeedUploadCustomers()

    override suspend fun insertCustomer(data: Customer) = dao.insertNewCustomer(data)
    override suspend fun insertCustomers(customers: List<Customer>) = dao.insertCustomers(customers)

    override suspend fun migrateToUUID() {
        val customers = dao.getCustomers().firstOrNull()
        if (!customers.isNullOrEmpty()) {
            db.withTransaction {
                for (customer in customers) {
                    val uuid = customer.new_id.ifEmpty {
                        val updatedCustomer = customer.copy(
                            new_id = UUID.randomUUID().toString()
                        )
                        dao.updateCustomer(updatedCustomer)
                        updatedCustomer.new_id
                    }

                    val newCustomer = Customer(
                        id = uuid,
                        name = customer.name,
                        isUploaded = customer.isUploaded
                    )
                    dao.insertNewCustomer(newCustomer)
                }
            }
        }
    }

    override suspend fun deleteAllOldCustomers() {
        dao.deleteAllOldCustomers()
    }

    override suspend fun deleteAllNewCustomers() {
        dao.deleteAllNewCustomers()
    }
}
