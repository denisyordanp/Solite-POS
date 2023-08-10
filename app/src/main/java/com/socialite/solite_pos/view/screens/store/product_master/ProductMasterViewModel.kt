package com.socialite.solite_pos.view.screens.store.product_master

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.GetCategoryProductVariantCount
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Product
import com.socialite.solite_pos.data.source.repository.ProductsRepository
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