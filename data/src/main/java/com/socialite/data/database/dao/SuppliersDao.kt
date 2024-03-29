package com.socialite.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.socialite.data.schema.room.master.Supplier
import kotlinx.coroutines.flow.Flow

@Dao
interface SuppliersDao {

    @Query("SELECT * FROM ${Supplier.DB_NAME}")
    fun getSuppliers(): Flow<List<Supplier>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSuppliers(data: Supplier)

    @Update
    fun updateSupplier(data: Supplier)
}
