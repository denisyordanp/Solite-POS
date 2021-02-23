package com.sosialite.solite_pos.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.sosialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.sosialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.sosialite.solite_pos.data.source.local.entity.room.helper.DataProduct
import com.sosialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.sosialite.solite_pos.data.source.local.entity.room.helper.VariantWithVariantMix
import com.sosialite.solite_pos.data.source.local.entity.room.master.*

class LocalDataSource private constructor(private val soliteDao: SoliteDao) {

	companion object {
		private var INSTANCE: LocalDataSource? = null

		fun getInstance(soliteDao: SoliteDao): LocalDataSource {
			if (INSTANCE == null) {
				INSTANCE = LocalDataSource(soliteDao)
			}
			return INSTANCE!!
		}
	}

	fun getOrderDetail(status: Int, date: String): List<OrderWithProduct>{
		return soliteDao.getListOrderDetail(status, date)
	}

	fun insertPaymentOrder(payment: OrderPayment): OrderWithProduct{
		return soliteDao.insertAndGetPaymentOrder(payment)
	}

	fun newOrder(order: OrderWithProduct){
		soliteDao.newOrder(order)
	}

	fun updateOrder(order: Order){
		soliteDao.updateOrder(order)
	}

	fun cancelOrder(order: OrderWithProduct){
		soliteDao.cancelOrder(order)
	}

	fun getPurchase(): List<PurchaseWithProduct>{
		return soliteDao.getPurchaseData()
	}

	fun newPurchase(data: PurchaseWithProduct){
		soliteDao.newPurchase(data)
	}

	fun getProductWithCategories(category: Int): LiveData<List<ProductWithCategory>>{
		return soliteDao.getProductWithCategories(category)
	}

	fun insertProduct(data: Product): Long{
		return soliteDao.insertProduct(data)
	}

	fun updateProduct(data: Product){
		soliteDao.updateProduct(data)
	}

	fun getDataProduct(idCategory: Int): LiveData<List<DataProduct>>{
		return soliteDao.getDataProduct(idCategory)
	}

	fun getVariantProduct(idProduct: Int, idVariantOption: Int): List<VariantProduct>{
		return soliteDao.getVariantProduct(idProduct, idVariantOption)
	}

	fun getVariantProductById(idProduct: Int): VariantProduct?{
		return soliteDao.getVariantProductById(idProduct)
	}

	fun insertVariantProduct(data: VariantProduct){
		soliteDao.insertVariantProduct(data)
	}

	fun removeVariantProduct(data: VariantProduct){
		soliteDao.removeVariantProduct(data.idVariantOption, data.idProduct)
	}


	fun getLiveVariantMixProduct(idVariant: Int): LiveData<VariantWithVariantMix>{
		return soliteDao.getLiveVariantMixProduct(idVariant)
	}

	fun getVariantMixProduct(idVariant: Int): VariantWithVariantMix {
		return soliteDao.getVariantMixProduct(idVariant)
	}

	fun insertVariantMix(data: VariantMix){
		soliteDao.insertVariantMix(data)
	}

	fun removeVariantMix(data: VariantMix){
		soliteDao.removeVariantMix(data.idVariant, data.idProduct)
	}

	fun getCategories(query: SimpleSQLiteQuery): LiveData<List<Category>>{
		return soliteDao.getCategories(query)
	}

	fun insertCategory(data: Category): Long{
		return soliteDao.insertCategory(data)
	}

	fun updateCategory(data: Category){
		soliteDao.updateCategory(data)
	}

	val variants: LiveData<List<Variant>>
		get() = soliteDao.getVariants()

	fun insertVariant(data: Variant){
		soliteDao.insertVariant(data)
	}

	fun updateVariant(data: Variant){
		soliteDao.updateVariant(data)
	}

	fun getVariantOptions(query: SupportSQLiteQuery): LiveData<List<VariantOption>>{
		return soliteDao.getVariantOptions(query)
	}

	fun insertVariantOption(data: VariantOption){
		soliteDao.insertVariantOption(data)
	}

	fun updateVariantOption(data: VariantOption){
		soliteDao.updateVariantOption(data)
	}

	val customers: LiveData<List<Customer>>
		get() = soliteDao.getCustomers()

	fun insertCustomer(data: Customer): Long{
		return soliteDao.insertCustomer(data)
	}

	fun updateCustomer(data: Customer){
		soliteDao.updateCustomer(data)
	}

	fun getSuppliers(): LiveData<List<Supplier>>{
		return soliteDao.getSuppliers()
	}

	fun insertSupplier(data: Supplier): Long{
		return soliteDao.insertSupplier(data)
	}

	fun updateSupplier(data: Supplier){
		soliteDao.updateSupplier(data)
	}

	val payments: LiveData<List<Payment>>
		get() = soliteDao.getPayments()

	fun insertPayment(data: Payment){
		soliteDao.insertPayment(data)
	}

	fun updatePayment(data: Payment){
		soliteDao.updatePayment(data)
	}

	fun getOutcome(date: String): LiveData<List<Outcome>>{
		return soliteDao.getOutcome(date)
	}

	fun insertOutcome(data: Outcome){
		soliteDao.insertOutcome(data)
	}

	fun updateOutcome(data: Outcome){
		soliteDao.updateOutcome(data)
	}

	fun fillData(){
		soliteDao.fillData()
	}
}
