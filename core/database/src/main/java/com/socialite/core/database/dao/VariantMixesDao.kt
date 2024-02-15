package com.socialite.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.socialite.schema.database.bridge.VariantMix
import com.socialite.schema.database.helper.VariantWithVariantMix
import com.socialite.schema.database.master.Product
import com.socialite.schema.database.master.Variant
import kotlinx.coroutines.flow.Flow

@Dao
interface VariantMixesDao {

    @Transaction
    @Query("SELECT * FROM ${Variant.DB_NAME} WHERE ${Variant.ID} = :idVariant")
    fun getVariantMixProduct(idVariant: Long): Flow<VariantWithVariantMix>

    @Transaction
    @Query("SELECT * FROM ${VariantMix.DB_NAME} WHERE ${Variant.ID} = :idVariant AND ${Product.ID} = :idProduct")
    fun getVariantMixProductById(idVariant: Long, idProduct: Long): Flow<VariantMix?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVariantMix(data: VariantMix): Long

    @Query("DELETE FROM ${VariantMix.DB_NAME} WHERE ${VariantMix.ID} = :idVariantMix")
    fun removeVariantMix(idVariantMix: Long)
}
