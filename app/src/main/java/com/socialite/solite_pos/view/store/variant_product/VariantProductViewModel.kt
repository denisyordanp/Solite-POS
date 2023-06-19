package com.socialite.solite_pos.view.store.variant_product

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.GetVariantsWithOptions
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.VariantProduct
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class VariantProductViewModel(
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

    companion object {
        fun getFactory(context: Context) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return VariantProductViewModel(
                    productsRepository = LoggedInRepositoryInjection.provideProductsRepository(
                        context
                    ),
                    productVariantsRepository = LoggedInRepositoryInjection.provideProductVariantsRepository(
                        context
                    ),
                    getVariantsWithOptions = LoggedInDomainInjection.provideGetVariantsWithOptions(
                        context
                    )
                ) as T
            }
        }
    }
}
