package com.socialite.solite_pos.view.store.product_master

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.GetCategoryProductVariantCount
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Product
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection
import kotlinx.coroutines.launch

class ProductMasterViewModel(
    private val productsRepository: ProductsRepository,
    private val getCategoryProductVariantCount: GetCategoryProductVariantCount,
) : ViewModel() {

    fun getProductsWithCategory() = getCategoryProductVariantCount()

    fun updateProduct(data: Product) {
        viewModelScope.launch {
            productsRepository.updateProduct(data)
        }
    }

    companion object {
        fun getFactory(context: Context) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProductMasterViewModel(
                    productsRepository = LoggedInRepositoryInjection.provideProductsRepository(
                        context
                    ),
                    getCategoryProductVariantCount = LoggedInDomainInjection.provideGetCategoryProductItemViewData(
                        context
                    )
                ) as T
            }
        }
    }
}
