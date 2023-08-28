package com.socialite.solite_pos.view.screens.store.variant_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.data.schema.room.new_bridge.VariantProduct
import com.socialite.domain.domain.AddNewVariantProduct
import com.socialite.domain.domain.GetProductById
import com.socialite.domain.domain.GetVariantsProductById
import com.socialite.domain.domain.GetVariantsWithOptions
import com.socialite.domain.domain.RemoveVariantProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VariantProductViewModel @Inject constructor(
    private val getProductById: GetProductById,
    private val getVariantsProductById: GetVariantsProductById,
    private val getVariantsWithOptions: GetVariantsWithOptions,
    private val addNewVariantProduct: AddNewVariantProduct,
    private val removeVariantProduct: RemoveVariantProduct,
) : ViewModel() {

    private val _viewState = MutableStateFlow(VariantProductViewState.idle())
    val viewState = _viewState.asStateFlow()

    fun initialLoad(productId: String) {
        viewModelScope.launch {
            combine(
                getVariantsWithOptions(),
                getVariantsProductById(productId),
                getProductById(productId)
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
            addNewVariantProduct(data)
        }
    }

    fun removeVariantProduct(data: VariantProduct) {
        viewModelScope.launch {
            removeVariantProduct.invoke(data)
        }
    }
}
