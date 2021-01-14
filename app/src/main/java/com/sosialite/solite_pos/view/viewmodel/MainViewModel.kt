package com.sosialite.solite_pos.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.sosialite.solite_pos.data.source.local.entity.room.helper.DataProduct
import com.sosialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.sosialite.solite_pos.data.source.local.entity.room.helper.VariantWithVariantMix
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.sosialite.solite_pos.data.source.local.entity.room.master.*
import com.sosialite.solite_pos.data.source.repository.SoliteRepository

class MainViewModel(private val repository: SoliteRepository) : ViewModel(){

//	fun getProducts(category: Int?): LiveData<List<DetailProduct>>? {
//		return if (category != null){
//			repository.getProducts(category)
//		}else{
//			null
//		}
//	}
//
//	fun getDetailOrders(orderNo: String): LiveData<List<DetailOrder>>{
//		return repository.getDetailOrders(orderNo)
//	}
//
//	fun getOrders(): LiveData<List<OrderWithProduct>> {
//		return repository.getOrders()
//	}

	fun getDataProduct(idCategory: Int): LiveData<List<DataProduct>> {
		return repository.getDataProduct(idCategory)
	}

	fun getVariantProduct(idProduct: Int, idVariantOption: Int): List<VariantProduct>{
		return repository.getVariantProduct(idProduct, idVariantOption)
	}

	fun insertVariantProduct(data: VariantProduct) {
		repository.insertVariantProduct(data)
	}

	fun removeVariantProduct(data: VariantProduct) {
		repository.removeVariantProduct(data)
	}

	fun getProducts(category: Int): LiveData<List<ProductWithCategory>>{
		return repository.getProductWithCategories(category)
	}

	fun getLiveVariantMixProduct(idVariant: Int): LiveData<VariantWithVariantMix>{
		return repository.getLiveVariantMixProduct(idVariant)
	}

	fun getVariantMixProduct(idVariant: Int): VariantWithVariantMix {
		return repository.getVariantMixProduct(idVariant)
	}

	fun insertVariantMix(data: VariantMix){
		repository.insertVariantMix(data)
	}

	fun removeVariantMix(data: VariantMix){
		repository.removeVariantMix(data)
	}

	fun getProductWithCategories(category: Int): LiveData<List<ProductWithCategory>> {
		return repository.getProductWithCategories(category)
	}

	fun insertProduct(data: Product): Long{
		return repository.insertProduct(data)
	}

	fun updateProduct(data: Product){
		repository.updateProduct(data)
	}

	fun getCategories(query: SimpleSQLiteQuery): LiveData<List<Category>>{
		return repository.getCategories(query)
	}

	fun insertCategory(data: Category){
		repository.insertCategory(data)
	}

	fun updateCategory(data: Category){
		repository.updateCategory(data)
	}

	val variants: LiveData<List<Variant>>
		get() = repository.variants

	fun insertVariants(data: Variant){
		repository.insertVariant(data)
	}

	fun updateVariant(data: Variant){
		repository.updateVariant(data)
	}

	fun getVariantOptions(query: SupportSQLiteQuery): LiveData<List<VariantOption>>{
		return repository.getVariantOptions(query)
	}

	fun insertVariantOption(data: VariantOption){
		repository.insertVariantOption(data)
	}

	fun updateVariantOption(data: VariantOption){
		repository.updateVariantOption(data)
	}

	val customers: LiveData<List<Customer>>
		get() = repository.customers

	fun insertCustomers(data: Customer){
		repository.insertCustomer(data)
	}

	fun updateCustomer(data: Customer){
		repository.updateCustomer(data)
	}

	val payments: LiveData<List<Payment>>
		get() = repository.payments

	fun insertPayment(data: Payment){
		repository.insertPayment(data)
	}

	fun updatePayment(data: Payment){
		repository.updatePayment(data)
	}
}
