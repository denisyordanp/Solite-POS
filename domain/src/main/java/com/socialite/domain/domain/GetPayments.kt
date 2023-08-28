package com.socialite.domain.domain

import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.data.schema.room.new_master.Payment
import kotlinx.coroutines.flow.Flow

fun interface GetPayments {
    operator fun invoke(query: SupportSQLiteQuery): Flow<List<Payment>>
}