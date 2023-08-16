package com.socialite.solite_pos.view.screens.store.product_master

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.domain.GetCategoryProductVariantCount
import com.socialite.solite_pos.data.schema.room.new_master.Product
import com.socialite.solite_pos.data.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductMasterViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val getCategoryProductVariantCount: GetCategoryProductVariantCount,
) : ViewModel() {

    fun getProductsWithCategory() = getCategoryProductVariantCount()

    fun updateProduct(data: Product) {
        viewModelScope.launch {
            productsRepository.updateProduct(data)
        }
    }
}
