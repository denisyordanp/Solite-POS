package com.socialite.domain.domain.impl

import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.data.repository.CategoriesRepository
import com.socialite.domain.domain.GetCategories
import javax.inject.Inject

class GetCategoriesImpl @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
) : GetCategories {
    override fun invoke(query: SimpleSQLiteQuery) = categoriesRepository.getCategories(query)
}