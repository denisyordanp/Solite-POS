package com.socialite.solite_pos.view.viewModel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.helper.VariantWithOptions
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.socialite.solite_pos.data.source.local.entity.room.helper.VariantWithVariantMix
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.socialite.solite_pos.data.source.remote.response.helper.ApiResponse
import com.socialite.solite_pos.data.source.repository.CategoriesRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.data.source.repository.SoliteRepository
import com.socialite.solite_pos.data.source.repository.VariantOptionsRepository
import com.socialite.solite_pos.data.source.repository.VariantsRepository
import com.socialite.solite_pos.vo.Resource
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: SoliteRepository,
    private val variantsRepository: VariantsRepository,
    private val variantOptionsRepository: VariantOptionsRepository,
    private val categoriesRepository: CategoriesRepository,
    private val productsRepository: ProductsRepository
) : ViewModel() {

    companion object : ViewModelFromFactory<ProductViewModel>() {
        fun getMainViewModel(activity: FragmentActivity): ProductViewModel {
            return buildViewModel(activity, ProductViewModel::class.java)
        }
    }

    fun getProducts(idCategory: Long) = productsRepository.getProductWithCategories(idCategory)

    fun getProductVariantOptions(idProduct: Long): LiveData<Resource<List<VariantWithOptions>?>> {
        return repository.getProductVariantOptions(idProduct)
    }

    fun getVariantProduct(
        idProduct: Long,
        idVariantOption: Long
    ): LiveData<Resource<VariantProduct?>> {
        return repository.getVariantProduct(idProduct, idVariantOption)
    }

    fun getVariantProductById(idProduct: Long): LiveData<Resource<VariantProduct?>> {
        return repository.getVariantProductById(idProduct)
    }

    fun insertVariantProduct(data: VariantProduct, callback: (ApiResponse<Long>) -> Unit) {
        repository.insertVariantProduct(data, callback)
    }

    fun removeVariantProduct(data: VariantProduct, callback: (ApiResponse<Boolean>) -> Unit) {
        repository.removeVariantProduct(data, callback)
    }

    fun getVariantMixProductById(
        idVariant: Long,
        idProduct: Long
    ): LiveData<Resource<VariantMix?>> {
        return repository.getVariantMixProductById(idVariant, idProduct)
    }

    fun getVariantMixProduct(idVariant: Long): LiveData<Resource<VariantWithVariantMix>> {
        return repository.getVariantMixProduct(idVariant)
    }

    fun insertVariantMix(data: VariantMix, callback: (ApiResponse<Long>) -> Unit) {
        repository.insertVariantMix(data, callback)
    }

    fun removeVariantMix(data: VariantMix, callback: (ApiResponse<Boolean>) -> Unit) {
        repository.removeVariantMix(data, callback)
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
