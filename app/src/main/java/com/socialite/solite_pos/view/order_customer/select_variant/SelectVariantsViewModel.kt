package com.socialite.solite_pos.view.order_customer.select_variant

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.GetProductWithVariantOptions
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.new_master.VariantOption
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SelectVariantsViewModel(
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

    companion object {
        fun getFactory(context: Context) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SelectVariantsViewModel(
                    getProductWithVariantOptions = LoggedInDomainInjection.provideGetProductWithVariantOptions(
                        context
                    )
                ) as T
            }
        }
    }
}
