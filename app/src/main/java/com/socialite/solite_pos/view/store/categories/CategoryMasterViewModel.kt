package com.socialite.solite_pos.view.store.categories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Category
import com.socialite.solite_pos.data.source.repository.CategoriesRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection
import kotlinx.coroutines.launch

class CategoryMasterViewModel(
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

    companion object {
        fun getFactory(context: Context) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CategoryMasterViewModel(
                    categoriesRepository = LoggedInRepositoryInjection.provideCategoriesRepository(context)
                ) as T
            }
        }
    }
}
