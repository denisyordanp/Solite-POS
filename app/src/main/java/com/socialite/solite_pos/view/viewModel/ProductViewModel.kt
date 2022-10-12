package com.socialite.solite_pos.view.viewModel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.domain.GetProductVariantOptions
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.socialite.solite_pos.data.source.repository.CategoriesRepository
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.data.source.repository.VariantMixesRepository
import com.socialite.solite_pos.data.source.repository.VariantOptionsRepository
import com.socialite.solite_pos.data.source.repository.VariantsRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ProductViewModel(
    private val variantsRepository: VariantsRepository,
    private val variantOptionsRepository: VariantOptionsRepository,
    private val categoriesRepository: CategoriesRepository,
    private val productsRepository: ProductsRepository,
    private val productVariantsRepository: ProductVariantsRepository,
    private val getProductVariantOptions: GetProductVariantOptions,
    private val variantMixesRepository: VariantMixesRepository
) : ViewModel() {

    companion object : ViewModelFromFactory<ProductViewModel>() {
        fun getMainViewModel(activity: FragmentActivity): ProductViewModel {
            return buildViewModel(activity, ProductViewModel::class.java)
        }
    }

    fun getProducts(idCategory: Long) = productsRepository.getProductWithCategories(idCategory)

    suspend fun getProduct(idProduct: Long) = productsRepository.getProduct(idProduct)
    fun getAllProducts() = productsRepository
        .getAllProductWithCategories()
        .map { it.groupBy {product -> product.category } }

    fun getProductVariantOptions(idProduct: Long) = getProductVariantOptions.invoke(idProduct)

    fun getVariantProduct(
        idProduct: Long,
        idVariantOption: Long
    ) = productVariantsRepository.getVariantProduct(idProduct, idVariantOption)

    fun getVariantProductById(idProduct: Long) =
        productVariantsRepository.getVariantProductById(idProduct)

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

    fun getVariantMixProductById(
        idVariant: Long,
        idProduct: Long
    ) = variantMixesRepository.getVariantMixProductById(idVariant, idProduct)

    fun getVariantMixProduct(idVariant: Long) =
        variantMixesRepository.getVariantMixProduct(idVariant)

    fun insertVariantMix(data: VariantMix) {
        viewModelScope.launch {
            variantMixesRepository.insertVariantMix(data)
        }
    }

    fun removeVariantMix(data: VariantMix) {
        viewModelScope.launch {
            variantMixesRepository.removeVariantMix(data)
        }
    }

    fun getProductWithCategories(category: Long) =
        productsRepository.getProductWithCategories(category)

    fun insertProduct(data: Product) {
        viewModelScope.launch {
            productsRepository.insertProduct(data)
        }
    }

    fun updateProduct(data: Product) {
        viewModelScope.launch {
            productsRepository.updateProduct(data)
        }
    }

    fun getCategories(query: SimpleSQLiteQuery) = categoriesRepository.getCategories(query)

    fun insertCategory(data: Category) {
        viewModelScope.launch {
            categoriesRepository.insertCategory(data)
        }
    }

    fun updateCategory(data: Category) {
        viewModelScope.launch {
            categoriesRepository.updateCategory(data)
        }
    }

    val variants = variantsRepository.getVariants()

    fun insertVariants(data: Variant) {
        viewModelScope.launch {
            variantsRepository.insertVariant(data)
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
            variantOptionsRepository.insertVariantOption(data)
        }
    }

    fun updateVariantOption(data: VariantOption) {
        viewModelScope.launch {
            variantOptionsRepository.updateVariantOption(data)
        }
    }
}
