package com.socialite.solite_pos.view.screens.store.product_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.domain.domain.GetProductVariantOptions
import com.socialite.data.schema.room.new_master.Category
import com.socialite.data.schema.room.new_master.Product
import com.socialite.data.repository.CategoriesRepository
import com.socialite.data.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
    private val productsRepository: ProductsRepository,
    private val getProductVariantOptions: GetProductVariantOptions
) : ViewModel() {

    private val _viewState = MutableStateFlow(ProductDetailViewState.idle())
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            categoriesRepository.getCategories(Category.getFilter(Category.ALL))
                .map {categories ->
                    _viewState.value.copy(
                        categories = categories.map { com.socialite.solite_pos.schema.Category.fromData(it) }
                    )
                }.collect(_viewState)
        }
    }

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            productsRepository.getProductWithCategory(productId)
                .combine(getProductVariantOptions(productId)) { product, variants ->
                    product?.let {
                        ProductVariantOptions(
                            it,
                            variants
                        )
                    }
                }.map {
                    _viewState.value.copy(
                        product = it
                    )
                }
                .collect(_viewState)
        }
    }

    fun updateProduct(data: Product) {
        viewModelScope.launch {
            productsRepository.updateProduct(data)
        }
    }

    fun insertProduct(data: Product) {
        viewModelScope.launch {
            productsRepository.insertProduct(data)
        }
    }
}
