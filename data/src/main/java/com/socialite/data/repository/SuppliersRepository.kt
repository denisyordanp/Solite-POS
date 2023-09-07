package com.socialite.data.repository

import com.socialite.data.schema.room.master.Supplier
import kotlinx.coroutines.flow.Flow

interface SuppliersRepository {

    fun getSuppliers(): Flow<List<Supplier>>
    suspend fun insertSupplier(data: Supplier)
    suspend fun updateSupplier(data: Supplier)
}
