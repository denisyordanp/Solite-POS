package com.socialite.data.repository.impl

import androidx.room.withTransaction
import com.socialite.common.utility.di.IoDispatcher
import com.socialite.data.database.AppDatabase
import com.socialite.data.database.dao.CustomersDao
import com.socialite.data.repository.CustomersRepository
import com.socialite.data.repository.SyncRepository
import com.socialite.data.schema.helper.UpdateSynchronizations
import com.socialite.data.schema.room.EntityData
import com.socialite.data.schema.room.new_master.Customer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import java.util.UUID
import javax.inject.Inject

class CustomersRepositoryImpl @Inject constructor(
    private val dao: CustomersDao,
    private val db: AppDatabase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : CustomersRepository {

    override fun getCustomers() = dao.getNewCustomers().flowOn(dispatcher)
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
