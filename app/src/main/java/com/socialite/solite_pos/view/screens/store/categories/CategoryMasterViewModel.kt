package com.socialite.solite_pos.view.screens.store.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Category
import com.socialite.solite_pos.data.repository.CategoriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryMasterViewModel @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
) : ViewModel() {

    fun getCategories(query: SimpleSQLiteQuery) = categoriesRepository.getCategories(query)

    fun insertCategory(data: Category) {
        viewModelScope.launch {
            categoriesRepository.insertCategory(data.asNewCategory())
        }
    }

    fun updateCategory(data: Category) {
        viewModelScope.launch {
            categoriesRepository.updateCategory(data)
        }
    }
}
