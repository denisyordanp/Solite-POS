package com.socialite.domain.domain.impl

import com.socialite.common.utility.di.IoDispatcher
import com.socialite.data.repository.CategoriesRepository
import com.socialite.domain.domain.GetCategories
import com.socialite.domain.helper.toDomain
import com.socialite.schema.ui.main.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetCategoriesImpl @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetCategories {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(status: Category.Status) =
        categoriesRepository.getCategories(com.socialite.schema.database.new_master.Category.getFilter(status.code))
            .mapLatest { categories ->
                categories.map { it.toDomain() }
            }.flowOn(dispatcher)
}