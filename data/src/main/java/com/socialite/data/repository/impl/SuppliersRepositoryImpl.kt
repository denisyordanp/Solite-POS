package com.socialite.data.repository.impl

import com.socialite.data.database.dao.SuppliersDao
import com.socialite.data.repository.SuppliersRepository
import com.socialite.data.schema.room.master.Supplier
import kotlinx.coroutines.flow.Flow

class SuppliersRepositoryImpl(
    private val dao: SuppliersDao
) : SuppliersRepository {

    companion object {
        @Volatile
        private var INSTANCE: SuppliersRepositoryImpl? = null

        fun getInstance(
            dao: SuppliersDao
        ): SuppliersRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(SuppliersRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = SuppliersRepositoryImpl(dao = dao)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getSuppliers(): Flow<List<Supplier>> = dao.getSuppliers()

    override suspend fun insertSupplier(data: Supplier) {
        dao.insertSuppliers(data)
    }

    override suspend fun updateSupplier(data: Supplier) {
        dao.updateSupplier(data)
    }
}
