package com.sosialite.solite_pos.data.source.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.sosialite.solite_pos.data.NetworkFunBound
import com.sosialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.sosialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.sosialite.solite_pos.data.source.local.entity.room.helper.DataProduct
import com.sosialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.sosialite.solite_pos.data.source.local.entity.room.helper.VariantWithVariantMix
import com.sosialite.solite_pos.data.source.local.entity.room.master.*
import com.sosialite.solite_pos.data.source.local.room.LocalDataSource
import com.sosialite.solite_pos.data.source.remote.RemoteDataSource
import com.sosialite.solite_pos.data.source.remote.response.helper.ApiResponse
import com.sosialite.solite_pos.utils.database.AppExecutors

class SoliteRepository private constructor(
		private val remoteDataSource: RemoteDataSource,
		private val localDataSource: LocalDataSource,
		private val appExecutors: AppExecutors
) : SoliteDataSource {

	companion object {
		@Volatile
		private var INSTANCE: SoliteRepository? = null

		fun getInstance(remoteData: RemoteDataSource, localDataSource: LocalDataSource, appExecutors: AppExecutors): SoliteRepository {
			if (INSTANCE == null) {
				synchronized(SoliteRepository::class.java) {
					if (INSTANCE == null) {
						INSTANCE = SoliteRepository(remoteData, localDataSource, appExecutors)
					}
				}
			}
			return INSTANCE!!
		}
	}

	override fun getOrderDetail(status: Int, date: String): List<OrderWithProduct>{
		return localDataSource.getOrderDetail(status, date)
	}

	override fun insertPaymentOrder(payment: OrderPayment): OrderWithProduct{
		return localDataSource.insertPaymentOrder(payment)
	}

	override fun newOrder(order: OrderWithProduct) {
		localDataSource.newOrder(order)
	}

	override fun updateOrder(order: Order) {
		localDataSource.updateOrder(order)
	}

	override fun cancelOrder(order: OrderWithProduct){
		localDataSource.cancelOrder(order)
	}

	override fun getPurchase(): List<PurchaseWithProduct> {
		return localDataSource.getPurchase()
	}

	override fun newPurchase(data: PurchaseWithProduct) {
		localDataSource.newPurchase(data)
	}

	override fun getDataProduct(idCategory: Int): LiveData<List<DataProduct>> {
		return localDataSource.getDataProduct(idCategory)
	}

	override fun getVariantProduct(idProduct: Int, idVariantOption: Int): List<VariantProduct>{
		return localDataSource.getVariantProduct(idProduct, idVariantOption)
	}

	override fun getVariantProductById(idProduct: Int): VariantProduct?{
		return localDataSource.getVariantProductById(idProduct)
	}

	override fun insertVariantProduct(data: VariantProduct, callback: (ApiResponse<Int>) -> Unit) {
		localDataSource.insertVariantProduct(data)
	}

	override fun removeVariantProduct(data: VariantProduct, callback: (ApiResponse<Int>) -> Unit) {
		localDataSource.removeVariantProduct(data)
	}

	override fun getLiveVariantMixProduct(idVariant: Int): LiveData<VariantWithVariantMix>{
		return localDataSource.getLiveVariantMixProduct(idVariant)
	}

	override fun getVariantMixProduct(idVariant: Int): VariantWithVariantMix {
		return localDataSource.getVariantMixProduct(idVariant)
	}

	override fun insertVariantMix(data: VariantMix, callback: (ApiResponse<Int>) -> Unit) {
		localDataSource.insertVariantMix(data)
	}

	override fun removeVariantMix(data: VariantMix, callback: (ApiResponse<Int>) -> Unit) {
		localDataSource.removeVariantMix(data)
	}

	override fun getProductWithCategories(category: Int): LiveData<List<ProductWithCategory>> {
		return localDataSource.getProductWithCategories(category)
	}

	override fun insertProduct(data: Product, callback: (ApiResponse<Int>) -> Unit) {
		localDataSource.insertProduct(data)
	}

	override fun updateProduct(data: Product, callback: (ApiResponse<Int>) -> Unit){
		localDataSource.updateProduct(data)
	}

	override fun getCategories(query: SimpleSQLiteQuery): LiveData<List<Category>>{
		return localDataSource.getCategories(query)
	}

	override fun insertCategory(data: Category, callback: (ApiResponse<Int>) -> Unit){
		localDataSource.insertCategory(data)
	}

	override fun updateCategory(data: Category, callback: (ApiResponse<Int>) -> Unit) {
		localDataSource.updateCategory(data)
	}

	override val variants: LiveData<List<Variant>>
		get() = localDataSource.variants

	override fun insertVariant(data: Variant, callback: (ApiResponse<Int>) -> Unit) {
		localDataSource.insertVariant(data)
	}

	override fun updateVariant(data: Variant, callback: (ApiResponse<Int>) -> Unit) {
		localDataSource.updateVariant(data)
	}

	override fun getVariantOptions(query: SupportSQLiteQuery): LiveData<List<VariantOption>>{
		return localDataSource.getVariantOptions(query)
	}

	override fun insertVariantOption(data: VariantOption, callback: (ApiResponse<Int>) -> Unit) {
		localDataSource.insertVariantOption(data)
	}

	override fun updateVariantOption(data: VariantOption, callback: (ApiResponse<Int>) -> Unit) {
		localDataSource.updateVariantOption(data)
	}

	override val customers: LiveData<List<Customer>>
		get() = localDataSource.customers

	override fun insertCustomer(data: Customer, callback: (ApiResponse<Int>) -> Unit){
		object : NetworkFunBound<Boolean, Customer>() {
			override fun dbOperation(): Customer{
				val id = localDataSource.insertCustomer(data)
				data.id = id.toInt()
				return data
			}

			override fun createCall(savedData: Customer, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.addCustomer(savedData, inCallback)
			}

			override fun successUpload(before: Customer) {
				before.isUploaded = true
				localDataSource.updateCustomer(before)
				callback.invoke(ApiResponse.success(before.id))
			}

			override fun failedUpload() {
				callback.invoke(ApiResponse.error(null))
			}

			override fun dbFinish(savedData: Customer) {
				callback.invoke(ApiResponse.finish(savedData.id))
			}
		}
	}

	override fun updateCustomer(data: Customer, callback: (ApiResponse<Boolean>) -> Unit) {
		localDataSource.updateCustomer(data)
	}

	override val suppliers: LiveData<List<Supplier>>
		get() = localDataSource.getSuppliers()

	override fun insertSupplier(data: Supplier, callback: (ApiResponse<Int>) -> Unit) {
		localDataSource.insertSupplier(data)
	}

	override fun updateSupplier(data: Supplier, callback: (ApiResponse<Int>) -> Unit) {
		localDataSource.updateSupplier(data)
	}

	override val payments: LiveData<List<Payment>>
		get() = localDataSource.payments

	override fun insertPayment(data: Payment, callback: (ApiResponse<Int>) -> Unit) {
		localDataSource.insertPayment(data)
	}

	override fun updatePayment(data: Payment, callback: (ApiResponse<Int>) -> Unit) {
		localDataSource.updatePayment(data)
	}

	override fun getOutcome(date: String): LiveData<List<Outcome>> {
		return localDataSource.getOutcome(date)
	}

	override fun insertOutcome(data: Outcome, callback: (ApiResponse<Int>) -> Unit) {
		localDataSource.insertOutcome(data)
	}

	override fun updateOutcome(data: Outcome, callback: (ApiResponse<Int>) -> Unit) {
		localDataSource.updateOutcome(data)
	}

	override fun fillData(){
		localDataSource.fillData()
	}
}
