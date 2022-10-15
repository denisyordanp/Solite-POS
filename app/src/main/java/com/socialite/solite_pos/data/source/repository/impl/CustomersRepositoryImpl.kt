package com.socialite.solite_pos.data.source.repository.impl

import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.data.source.local.room.CustomersDao
import com.socialite.solite_pos.data.source.repository.CustomersRepository

class CustomersRepositoryImpl(
    private val dao: CustomersDao
) : CustomersRepository {

    companion object {
        @Volatile
        private var INSTANCE: CustomersRepositoryImpl? = null

        fun getInstance(
            dao: CustomersDao
        ): CustomersRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(CustomersRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = CustomersRepositoryImpl(dao = dao)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getCustomers() = dao.getCustomers()

    override suspend fun insertCustomer(data: Customer) = dao.insertCustomer(data)
}
