package com.socialite.domain.domain.impl

import com.socialite.data.repository.CategoriesRepository
import com.socialite.domain.domain.AddNewCategory
import com.socialite.domain.helper.toData
import com.socialite.domain.schema.main.Category
import javax.inject.Inject

class AddNewCategoryImpl @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
) : AddNewCategory {
    override suspend fun invoke(category: Category) {
        categoriesRepository.insertCategory(category.toData())
    }
}