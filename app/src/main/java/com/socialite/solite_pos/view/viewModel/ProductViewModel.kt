package com.socialite.solite_pos.view.viewModel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.helper.*
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.helper.VariantWithVariantMix
import com.socialite.solite_pos.data.source.local.entity.room.master.*
import com.socialite.solite_pos.data.source.remote.response.helper.ApiResponse
import com.socialite.solite_pos.data.source.repository.SoliteRepository
import com.socialite.solite_pos.vo.Resource

class ProductViewModel(private val repository: SoliteRepository) : ViewModel() {

	companion object : ViewModelFromFactory<ProductViewModel>(){
		fun getMainViewModel(activity: FragmentActivity): ProductViewModel {
			return buildViewModel(activity, ProductViewModel::class.java)
		}
	}

	fun getProducts(idCategory: Long): LiveData<Resource<List<ProductWithCategory>>>{
		return repository.getProducts(idCategory)
	}

	fun getProductVariantOptions(idProduct: Long): LiveData<Resource<List<VariantWithOptions>?>> {
		return repository.getProductVariantOptions(idProduct)
	}

	fun getVariantProduct(idProduct: Long, idVariantOption: Long): LiveData<Resource<VariantProduct?>> {
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

	fun getVariantMixProductById(idVariant: Long, idProduct: Long): LiveData<Resource<VariantMix?>> {
		return repository.getVariantMixProductById(idVariant, idProduct)
	}

	fun getVariantMixProduct(idVariant: Long): LiveData<Resource<VariantWithVariantMix>>{
		return repository.getVariantMixProduct(idVariant)
	}

	fun insertVariantMix(data: VariantMix, callback: (ApiResponse<Long>) -> Unit){
		repository.insertVariantMix(data, callback)
	}

	fun removeVariantMix(data: VariantMix, callback: (ApiResponse<Boolean>) -> Unit){
		repository.removeVariantMix(data, callback)
	}

	fun getProductWithCategories(category: Long): LiveData<Resource<List<ProductWithCategory>>> {
		return repository.getProductWithCategories(category)
	}

	fun insertProduct(data: Product, callback: (ApiResponse<Long>) -> Unit){
		repository.insertProduct(data, callback)
	}

	fun updateProduct(data: Product, callback: (ApiResponse<Boolean>) -> Unit){
		repository.updateProduct(data, callback)
	}

	fun getCategories(query: SimpleSQLiteQuery): LiveData<Resource<List<Category>>> {
		return repository.getCategories(query)
	}

	fun insertCategory(data: Category, callback: (ApiResponse<Long>) -> Unit){
		repository.insertCategory(data, callback)
	}

	fun updateCategory(data: Category, callback: (ApiResponse<Boolean>) -> Unit){
		repository.updateCategory(data, callback)
	}

	val variants: LiveData<Resource<List<Variant>>>
		get() = repository.variants

	fun insertVariants(data: Variant, callback: (ApiResponse<Long>) -> Unit){
		repository.insertVariant(data, callback)
	}

	fun updateVariant(data: Variant, callback: (ApiResponse<Boolean>) -> Unit){
		repository.updateVariant(data, callback)
	}

	fun getVariantOptions(query: SupportSQLiteQuery): LiveData<Resource<List<VariantOption>>>{
		return repository.getVariantOptions(query)
	}

	fun insertVariantOption(data: VariantOption, callback: (ApiResponse<Long>) -> Unit){
		repository.insertVariantOption(data, callback)
	}

	fun updateVariantOption(data: VariantOption, callback: (ApiResponse<Boolean>) -> Unit){
		repository.updateVariantOption(data, callback)
	}
}
