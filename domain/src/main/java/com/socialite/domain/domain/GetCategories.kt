package com.socialite.domain.domain

import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.data.schema.room.new_master.Category
import kotlinx.coroutines.flow.Flow

fun interface GetCategories {
    operator fun invoke(query: SimpleSQLiteQuery): Flow<List<Category>>
}