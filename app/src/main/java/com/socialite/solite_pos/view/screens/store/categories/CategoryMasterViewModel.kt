package com.socialite.solite_pos.view.screens.store.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.domain.domain.AddNewCategory
import com.socialite.domain.domain.GetCategories
import com.socialite.domain.domain.IsUserStaff
import com.socialite.domain.domain.UpdateCategory
import com.socialite.domain.schema.main.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryMasterViewModel @Inject constructor(
    private val getCategories: GetCategories,
    private val addNewCategory: AddNewCategory,
    private val updateCategory: UpdateCategory,
    private val isUserStaff: IsUserStaff
) : ViewModel() {

    fun getAllCategories() = getCategories.invoke(Category.Status.ALL)

    fun isUserStaff() = isUserStaff.invoke()

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
