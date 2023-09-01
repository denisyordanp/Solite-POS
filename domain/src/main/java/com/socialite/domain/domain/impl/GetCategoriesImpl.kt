package com.socialite.domain.domain.impl

import com.socialite.data.repository.CategoriesRepository
import com.socialite.domain.domain.GetCategories
import com.socialite.domain.helper.toDomain
import com.socialite.domain.schema.main.Category
import com.socialite.data.schema.room.new_master.Category as DataCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetCategoriesImpl @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
) : GetCategories {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(status: Category.Status) =
        categoriesRepository.getCategories(DataCategory.getFilter(status.code)).mapLatest { categories ->
            categories.map { it.toDomain() }
        }
}