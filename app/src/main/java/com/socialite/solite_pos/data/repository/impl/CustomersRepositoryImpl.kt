package com.socialite.solite_pos.data.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.entity.helper.EntityData
import com.socialite.solite_pos.data.schema.room.new_master.Customer
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.CustomersDao
import com.socialite.solite_pos.data.repository.CustomersRepository
import com.socialite.solite_pos.data.repository.SyncRepository
import com.socialite.solite_pos.utils.tools.UpdateSynchronizations
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject

class CustomersRepositoryImpl @Inject constructor(
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
    override suspend fun getItems() = dao.getNewCustomers().first()

    override suspend fun updateItems(items: List<Customer>) {
        dao.updateCustomers(items)
    }

    override suspend fun insertItems(items: List<Customer>) {
        dao.insertCustomers(items)
    }

    override suspend fun insertCustomer(data: Customer) = dao.insertNewCustomer(data)

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

    @Suppress("UNCHECKED_CAST")
    override suspend fun updateSynchronization(missingItems: List<Customer>?) {
        val update = UpdateSynchronizations(this as SyncRepository<EntityData>)
        update.updates(missingItems)
    }
}
