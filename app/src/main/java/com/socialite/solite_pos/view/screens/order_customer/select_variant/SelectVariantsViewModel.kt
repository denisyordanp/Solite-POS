package com.socialite.solite_pos.view.screens.order_customer.select_variant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.domain.domain.GetProductWithVariantOptions
import com.socialite.data.schema.room.new_master.VariantOption
import com.socialite.domain.schema.helper.ProductOrderDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectVariantsViewModel @Inject constructor(
    private val getProductWithVariantOptions: GetProductWithVariantOptions,
) : ViewModel() {

    private val _viewState = MutableStateFlow(SelectVariantsViewState.idle())
    val viewState = _viewState.asStateFlow()

    fun loadProductForSelectingVariants(productId: String) {
        viewModelScope.launch {
            getProductWithVariantOptions(productId)
                .map {
                    _viewState.value.copy(
                        selectedProductVariantOptions = it.second,
                        productOrderDetail = ProductOrderDetail.productNoVariant(it.first)
                    )
                }
                .collect(_viewState)
        }
    }

    fun optionSelected(prevOption: VariantOption?, option: VariantOption, isSelected: Boolean) {
        viewModelScope.launch {
            val productDetailState = _viewState.value.productOrderDetail
            _viewState.emit(
                _viewState.value.copy(
                    productOrderDetail = productDetailState.copy(
                        variants = if (isSelected) {
                            productDetailState.addOption(option, prevOption)
                        } else {
                            productDetailState.removeOption(option)
                        }
                    )
                )
            )
        }
    }

    fun onAmountClicked(isAdd: Boolean) {
        viewModelScope.launch {
            val productDetailState = _viewState.value.productOrderDetail
            val newAmount = productDetailState.amount.run {
                return@run if (isAdd) {
                    this + 1
                } else {
                    if (this == 1) this else this - 1
                }
            }

            _viewState.emit(
                _viewState.value.copy(
                    productOrderDetail = productDetailState.copy(
                        amount = newAmount
                    )
                )
            )
        }
    }
}
