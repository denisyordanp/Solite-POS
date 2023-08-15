package com.socialite.solite_pos.data.repository

import com.socialite.solite_pos.data.source.local.entity.room.master.Supplier
import kotlinx.coroutines.flow.Flow

interface SuppliersRepository {

    fun getSuppliers(): Flow<List<Supplier>>
    suspend fun insertSupplier(data: Supplier)
    suspend fun updateSupplier(data: Supplier)
}
