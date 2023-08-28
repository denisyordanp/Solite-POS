package com.socialite.domain.domain.impl

import com.socialite.data.repository.CategoriesRepository
import com.socialite.data.schema.room.new_master.Category
import com.socialite.domain.domain.UpdateCategory
import javax.inject.Inject

class UpdateCategoryImpl @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
) : UpdateCategory {
    override suspend fun invoke(category: Category) {
        categoriesRepository.updateCategory(category)
    }
}