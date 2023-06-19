package com.socialite.solite_pos.view.viewModel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.domain.GetProductVariantOptions
import com.socialite.solite_pos.data.source.domain.GetProductWithCategories
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.VariantProduct
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Category
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Product
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.new_master.VariantOption
import com.socialite.solite_pos.data.source.repository.CategoriesRepository
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.data.source.repository.VariantOptionsRepository
import com.socialite.solite_pos.data.source.repository.VariantsRepository
import com.socialite.solite_pos.view.factory.LoggedInViewModelFromFactory
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ProductViewModel(
    private val variantsRepository: VariantsRepository,
    private val variantOptionsRepository: VariantOptionsRepository,
    private val categoriesRepository: CategoriesRepository,
    private val productsRepository: ProductsRepository,
    private val productVariantsRepository: ProductVariantsRepository,
    private val getProductVariantOptions: GetProductVariantOptions,
    private val getProductWithCategories: GetProductWithCategories
) : ViewModel() {

    companion object : LoggedInViewModelFromFactory<ProductViewModel>() {
        fun getMainViewModel(activity: FragmentActivity): ProductViewModel {
            return buildViewModel(activity, ProductViewModel::class.java)
        }
    }

    fun getProduct(idProduct: String) = productsRepository.getProductById(idProduct)
    fun getProductWithCategory(idProduct: String) =
        productsRepository.getProductWithCategory(idProduct)

    fun getAllProducts() = getProductWithCategories()

    suspend fun isProductHasVariant(idProduct: String) = productVariantsRepository
        .isProductHasVariants(idProduct)

    fun getProductVariantOptions(idProduct: String) = getProductVariantOptions.invoke(idProduct)

    fun getProductVariantCount(idProduct: String) = getProductVariantOptions.invoke(idProduct).map {
        it?.sumOf { variant ->
            variant.options.size
        } ?: 0
    }

    fun getVariantsProductById(idProduct: String) =
        productVariantsRepository.getVariantsProductById(idProduct)

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

    fun getProductWithCategories(category: String) =
        productsRepository.getProductWithCategories(category)

    suspend fun insertProduct(data: Product) = productsRepository.insertProduct(data)

    fun updateProduct(data: Product) {
        viewModelScope.launch {
            productsRepository.updateProduct(data)
        }
    }

    fun getCategories(query: SimpleSQLiteQuery) = categoriesRepository.getCategories(query)

    fun insertCategory(data: Category) {
        viewModelScope.launch {
            categoriesRepository.insertCategory(data.asNewCategory())
        }
    }

    fun updateCategory(data: Category) {
        viewModelScope.launch {
            categoriesRepository.updateCategory(data)
        }
    }

    val variants = variantsRepository.getVariants()

    fun insertVariant(data: Variant) {
        viewModelScope.launch {
            variantsRepository.insertVariant(data.asNewVariant())
        }
    }

    fun updateVariant(data: Variant) {
        viewModelScope.launch {
            variantsRepository.updateVariant(data)
        }
    }

    fun getVariantOptions(query: SupportSQLiteQuery) =
        variantOptionsRepository.getVariantOptions(query)

    fun insertVariantOption(data: VariantOption) {
        viewModelScope.launch {
            variantOptionsRepository.insertVariantOption(data.asNewVariantOption())
        }
    }

    fun updateVariantOption(data: VariantOption) {
        viewModelScope.launch {
            variantOptionsRepository.updateVariantOption(data)
        }
    }
}
