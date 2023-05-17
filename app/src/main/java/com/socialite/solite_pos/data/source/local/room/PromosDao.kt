package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.master.Promo
import kotlinx.coroutines.flow.Flow

@Dao
interface PromosDao {

    @RawQuery(observedEntities = [Promo::class])
    fun getPromos(query: SupportSQLiteQuery): Flow<List<Promo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPromo(data: Promo)

    @Update
    suspend fun updatePromo(data: Promo)
}
