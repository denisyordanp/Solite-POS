package com.socialite.solite_pos.view.screens.store.product_master

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.domain.domain.GetCategoryProductVariantCount
import com.socialite.domain.domain.IsUserStaff
import com.socialite.domain.domain.UpdateProduct
import com.socialite.domain.schema.main.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductMasterViewModel @Inject constructor(
    private val updateProduct: UpdateProduct,
    private val getCategoryProductVariantCount: GetCategoryProductVariantCount,
    private val isUserStaff: IsUserStaff
) : ViewModel() {

    fun getProductsWithCategory() = getCategoryProductVariantCount()

    fun isUserStaff() = isUserStaff.invoke()

    fun updateProduct(data: Product) {
        viewModelScope.launch {
            updateProduct.invoke(data)
        }
    }
}
