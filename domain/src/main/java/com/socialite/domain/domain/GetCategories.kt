package com.socialite.domain.domain

import com.socialite.schema.ui.main.Category
import kotlinx.coroutines.flow.Flow

fun interface GetCategories {
    operator fun invoke(status: Category.Status): Flow<List<Category>>
}