package com.socialite.solite_pos.view.store.product_detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.GetVariantOptions
import com.socialite.solite_pos.data.source.local.entity.helper.ProductVariantOptions
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Category
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Product
import com.socialite.solite_pos.data.source.repository.CategoriesRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val categoriesRepository: CategoriesRepository,
    private val productsRepository: ProductsRepository,
    private val getVariantOptions: GetVariantOptions
) : ViewModel() {

    private val _viewState = MutableStateFlow(ProductDetailViewState.idle())
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            categoriesRepository.getCategories(Category.getFilter(Category.ALL))
                .map {
                    _viewState.value.copy(
                        categories = it
                    )
                }.collect(_viewState)
        }
    }

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            productsRepository.getProductWithCategory(productId)
                .combine(getVariantOptions(productId)) { product, variants ->
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

    companion object {
        fun getFactory(context: Context) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProductDetailViewModel(
                    categoriesRepository = LoggedInRepositoryInjection.provideCategoriesRepository(context),
                    productsRepository = LoggedInRepositoryInjection.provideProductsRepository(context),
                    getVariantOptions = LoggedInDomainInjection.provideGetVariantOptions(context)
                ) as T
            }
        }
    }
}
