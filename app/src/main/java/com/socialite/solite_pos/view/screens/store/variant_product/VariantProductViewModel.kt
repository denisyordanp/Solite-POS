package com.socialite.solite_pos.view.screens.store.variant_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.domain.GetVariantsWithOptions
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.VariantProduct
import com.socialite.solite_pos.data.repository.ProductVariantsRepository
import com.socialite.solite_pos.data.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VariantProductViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val productVariantsRepository: ProductVariantsRepository,
    private val getVariantsWithOptions: GetVariantsWithOptions
) : ViewModel() {

    private val _viewState = MutableStateFlow(VariantProductViewState.idle())
    val viewState = _viewState.asStateFlow()

    fun initialLoad(productId: String) {
        viewModelScope.launch {
            combine(
                getVariantsWithOptions(),
                productVariantsRepository.getVariantsProductById(productId),
                productsRepository.getProductById(productId)
            ) { variants, selectedOptions, product ->
                _viewState.value.copy(
                    variants = variants,
                    selectedProductOptions = selectedOptions,
                    product = product
                )
            }.collect(_viewState)
        }
    }

    fun insertVariantProduct(data: VariantProduct) {
        viewModelScope.launch {
            productVariantsRepository.insertVariantProduct(data)
        }
    }

    fun removeVariantProduct(data: VariantProduct) {
        viewModelScope.launch {
            productVariantsRepository.removeVariantProduct(data)
        }
    }
}
