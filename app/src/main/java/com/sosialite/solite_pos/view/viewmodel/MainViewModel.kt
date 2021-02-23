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

class MainViewModel(private val repository: SoliteRepository) : ViewModel(){

	fun getOrderDetail(status: Int, date: String): List<OrderWithProduct>{
		return repository.getOrderDetail(status, date)
	}

	fun insertPaymentOrder(payment: OrderPayment): OrderWithProduct{
		return repository.insertPaymentOrder(payment)
	}

	fun newOrder(order: OrderWithProduct){
		repository.newOrder(order)
	}

	fun updateOrder(order: Order) {
		repository.updateOrder(order)
	}

	fun cancelOrder(order: OrderWithProduct) {
		repository.cancelOrder(order)
	}

	fun getPurchase(): List<PurchaseWithProduct>{
		return repository.getPurchase()
	}

	fun newPurchase(data: PurchaseWithProduct){
		repository.newPurchase(data)
	}

	fun getDataProduct(idCategory: Int): LiveData<List<DataProduct>> {
		return repository.getDataProduct(idCategory)
	}

	fun getVariantProduct(idProduct: Int, idVariantOption: Int): List<VariantProduct>{
		return repository.getVariantProduct(idProduct, idVariantOption)
	}

	fun getVariantProductById(idProduct: Int): VariantProduct?{
		return repository.getVariantProductById(idProduct)
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

	fun insertCustomers(data: Customer, callback: (ApiResponse<Int>) -> Unit){
		return repository.insertCustomer(data, callback)
	}

	fun updateCustomer(data: Customer, callback: (ApiResponse<Boolean>) -> Unit){
		repository.updateCustomer(data, callback)
	}

	val suppliers: LiveData<List<Supplier>>
	get() = repository.suppliers

	fun insertSupplier(data: Supplier): Long{
		return repository.insertSupplier(data)
	}

	fun updateSupplier(data: Supplier){
		repository.updateSupplier(data)
	}

	val payments: LiveData<List<Payment>>
		get() = repository.payments

	fun insertPayment(data: Payment){
		repository.insertPayment(data)
	}

	fun updatePayment(data: Payment){
		repository.updatePayment(data)
	}

	fun getOutcome(date: String): LiveData<List<Outcome>> {
		return repository.getOutcome(date)
	}

	fun insertOutcome(data: Outcome) {
		repository.insertOutcome(data)
	}

	fun updateOutcome(data: Outcome) {
		repository.updateOutcome(data)
	}

	fun fillData(){
		repository.fillData()
	}
}
