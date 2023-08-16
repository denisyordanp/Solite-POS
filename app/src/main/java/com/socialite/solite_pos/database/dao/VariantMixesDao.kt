package com.socialite.solite_pos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.socialite.solite_pos.data.schema.room.bridge.VariantMix
import com.socialite.solite_pos.data.schema.room.helper.VariantWithVariantMix
import com.socialite.solite_pos.data.schema.room.master.Product
import com.socialite.solite_pos.data.schema.room.master.Variant
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
