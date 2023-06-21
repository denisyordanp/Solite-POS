package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.master.Promo
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Promo as NewPromo
import kotlinx.coroutines.flow.Flow

@Dao
interface PromosDao {

    @RawQuery(observedEntities = [Promo::class])
    fun getPromos(query: SupportSQLiteQuery): Flow<List<Promo>>

    @Query("SELECT * FROM ${NewPromo.DB_NAME} WHERE ${AppDatabase.UPLOAD} = 0")
    suspend fun getNeedUploadPromos(): List<NewPromo>

    @Query("SELECT * FROM ${NewPromo.DB_NAME}")
    suspend fun getPromos(): List<NewPromo>

    @RawQuery(observedEntities = [NewPromo::class])
    fun getNewPromos(query: SupportSQLiteQuery): Flow<List<NewPromo>>

    @Query("SELECT * FROM '${Promo.DB_NAME}' WHERE ${Promo.ID} = :promoId LIMIT 1")
    suspend fun getPromoById(promoId: Long): Promo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewPromo(data: NewPromo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPromos(data: List<NewPromo>)

    @Update
    suspend fun updatePromo(data: Promo)

    @Update
    suspend fun updateNewPromo(data: NewPromo)

    @Update
    suspend fun updateNewPromos(data: List<NewPromo>)

    @Query("DELETE FROM '${Promo.DB_NAME}'")
    suspend fun deleteAllOldPromos()

    @Query("DELETE FROM '${NewPromo.DB_NAME}'")
    suspend fun deleteAllNewPromos()
}
