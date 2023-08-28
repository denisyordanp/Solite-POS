package com.socialite.domain.domain.impl

import com.socialite.data.repository.CategoriesRepository
import com.socialite.data.schema.room.new_master.Category
import com.socialite.domain.domain.AddNewCategory
import javax.inject.Inject

class AddNewCategoryImpl @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
) : AddNewCategory {
    override suspend fun invoke(category: Category) {
        categoriesRepository.insertCategory(category)
    }
}