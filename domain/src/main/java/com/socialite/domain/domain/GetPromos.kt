package com.socialite.domain.domain

import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow
import com.socialite.data.schema.room.new_master.Promo

fun interface GetPromos {
    operator fun invoke(query: SupportSQLiteQuery): Flow<List<Promo>>
}