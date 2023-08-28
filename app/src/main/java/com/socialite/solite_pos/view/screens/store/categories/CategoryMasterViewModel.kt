package com.socialite.solite_pos.view.screens.store.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.data.schema.room.new_master.Category
import com.socialite.domain.domain.AddNewCategory
import com.socialite.domain.domain.GetCategories
import com.socialite.domain.domain.UpdateCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryMasterViewModel @Inject constructor(
    private val getCategories: GetCategories,
    private val addNewCategory: AddNewCategory,
    private val updateCategory: UpdateCategory,
) : ViewModel() {

    fun getCategories(query: SimpleSQLiteQuery) = getCategories.invoke(query)

    fun insertCategory(data: Category) {
        viewModelScope.launch {
            addNewCategory(data.asNewCategory())
        }
    }

    fun updateCategory(data: Category) {
        viewModelScope.launch {
            updateCategory.invoke(data)
        }
    }
}
