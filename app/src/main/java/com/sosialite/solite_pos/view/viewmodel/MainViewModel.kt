package com.sosialite.solite_pos.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.sosialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.sosialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.sosialite.solite_pos.data.source.local.entity.room.helper.DataProduct
import com.sosialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.sosialite.solite_pos.data.source.local.entity.room.helper.VariantWithVariantMix
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.sosialite.solite_pos.data.source.local.entity.room.master.*
import com.sosialite.solite_pos.data.source.remote.response.helper.ApiResponse
import com.sosialite.solite_pos.data.source.repository.SoliteRepository
import com.sosialite.solite_pos.vo.Resource

class MainViewModel(private val repository: SoliteRepository) : ViewModel(){

	fun getOrderDetail(status: Int, date: String): LiveData<Resource<List<OrderWithProduct>>> {
		return repository.getOrderDetail(status, date)
	}

	fun insertPaymentOrder(payment: OrderPayment, callback: (ApiResponse<LiveData<OrderWithProduct>>) -> Unit) {
		return repository.insertPaymentOrder(payment, callback)
	}

	fun newOrder(order: OrderWithProduct, callback: (ApiResponse<Boolean>) -> Unit){
		repository.newOrder(order, callback)
	}

	fun updateOrder(order: Order, callback: (ApiResponse<Boolean>) -> Unit) {
		repository.updateOrder(order, callback)
	}

	fun cancelOrder(order: OrderWithProduct, callback: (ApiResponse<Boolean>) -> Unit) {
		repository.cancelOrder(order, callback)
	}

	val purchases: LiveData<Resource<List<PurchaseWithProduct>>>
	get() = repository.purchases

	fun newPurchase(data: PurchaseWithProduct, callback: (ApiResponse<Boolean>) -> Unit){
		repository.newPurchase(data, callback)
	}

	fun getDataProduct(idCategory: Long): LiveData<Resource<List<DataProduct>>> {
		return repository.getDataProduct(idCategory)
	}

	fun getVariantProduct(idProduct: Long, idVariantOption: Long): LiveData<Resource<List<VariantProduct>>> {
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

//	fun getProducts(category: Long): LiveData<List<ProductWithCategory>>{
//		return repository.getProductWithCategories(category)
//	}

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

	val customers: LiveData<Resource<List<Customer>>>
	get() = repository.customers

	fun insertCustomers(data: Customer, callback: (ApiResponse<Long>) -> Unit){
		return repository.insertCustomer(data, callback)
	}

	fun updateCustomer(data: Customer, callback: (ApiResponse<Boolean>) -> Unit){
		repository.updateCustomer(data, callback)
	}

	val suppliers: LiveData<Resource<List<Supplier>>>
	get() = repository.suppliers

	fun insertSupplier(data: Supplier, callback: (ApiResponse<Long>) -> Unit){
		repository.insertSupplier(data, callback)
	}

	fun updateSupplier(data: Supplier, callback: (ApiResponse<Boolean>) -> Unit){
		repository.updateSupplier(data, callback)
	}

	val payments: LiveData<Resource<List<Payment>>>
	get() = repository.payments

	fun insertPayment(data: Payment, callback: (ApiResponse<Long>) -> Unit){
		repository.insertPayment(data, callback)
	}

	fun updatePayment(data: Payment, callback: (ApiResponse<Boolean>) -> Unit){
		repository.updatePayment(data, callback)
	}

	fun getOutcome(date: String): LiveData<Resource<List<Outcome>>> {
		return repository.getOutcomes(date)
	}

	fun insertOutcome(data: Outcome, callback: (ApiResponse<Long>) -> Unit) {
		repository.insertOutcome(data, callback)
	}

	fun updateOutcome(data: Outcome, callback: (ApiResponse<Boolean>) -> Unit) {
		repository.updateOutcome(data, callback)
	}

	fun fillData(){
		repository.fillData()
	}
}
