package com.socialite.domain.domain.impl

import com.socialite.data.repository.CategoriesRepository
import com.socialite.domain.domain.UpdateCategory
import com.socialite.domain.helper.toData
import com.socialite.schema.ui.main.Category
import javax.inject.Inject

class UpdateCategoryImpl @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
) : UpdateCategory {
    override suspend fun invoke(category: Category) {
        categoriesRepository.updateCategory(category.toData())
    }
}