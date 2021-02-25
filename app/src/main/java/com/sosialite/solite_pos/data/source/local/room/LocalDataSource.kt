package com.sosialite.solite_pos.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.sosialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.sosialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.*
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

//	fun getOrderDetail(status: Int, date: String): List<OrderWithProduct>{
//		return soliteDao.getListOrderDetail(status, date)
//	}
//
//	fun insertPaymentOrder(payment: OrderPayment): OrderWithProduct{
//		return soliteDao.insertAndGetPaymentOrder(payment)
//	}
//
//	fun insertOrder(order: Order){
//		soliteDao.insertOrder(order)
//	}
//
//	fun insertDetailOrder(detail: OrderDetail): Long {
//		return soliteDao.insertDetailOrder(detail)
//	}
//
//	fun newOrder(order: OrderWithProduct){
//		soliteDao.newOrder(order)
//	}
//
//	fun updateOrder(order: Order){
//		soliteDao.updateOrder(order)
//	}
//
//	fun cancelOrder(order: OrderWithProduct){
//		soliteDao.cancelOrder(order)
//	}
//
//	fun getPurchase(): List<PurchaseWithProduct>{
//		return soliteDao.getPurchaseData()
//	}
//
//	fun newPurchase(data: PurchaseWithProduct){
//		soliteDao.newPurchase(data)
//	}
//
//	fun getProductWithCategories(category: Long): LiveData<List<ProductWithCategory>>{
//		return soliteDao.getProductWithCategories(category)
//	}
//
//	fun insertProduct(data: Product): Long {
//		return soliteDao.insertProduct(data)
//	}
//
//	fun updateProduct(data: Product){
//		soliteDao.updateProduct(data)
//	}
//
//	fun decreaseProductStock(idProduct: Long, amount: Int){
//		soliteDao.decreaseProductStock(idProduct, amount)
//	}
//
//	fun insertVariantMixOrder(variants: OrderProductVariantMix): Long{
//		return soliteDao.insertVariantMixOrder(variants)
//	}
//
//	fun insertMixVariantOrder(variants: OrderMixProductVariant){
//		soliteDao.insertMixVariantOrder(variants)
//	}
//
//	fun insertVariantOrder(variants: OrderProductVariant){
//		soliteDao.insertVariantOrder(variants)
//	}
//
//	fun getDataProduct(idCategory: Long): LiveData<List<DataProduct>>{
//		return soliteDao.getDataProduct(idCategory)
//	}
//
//	fun getVariantProduct(idProduct: Long, idVariantOption: Long): List<VariantProduct>{
//		return soliteDao.getVariantProduct(idProduct, idVariantOption)
//	}
//
//	fun getVariantProductById(idProduct: Long): VariantProduct?{
//		return soliteDao.getVariantProductById(idProduct)
//	}
//
//	fun insertVariantProduct(data: VariantProduct): Long{
//		return soliteDao.insertVariantProduct(data)
//	}
//
//	fun updateVariantProduct(data: VariantProduct){
//		soliteDao.updateVariantProduct(data)
//	}
//
//	fun removeVariantProduct(data: VariantProduct){
//		soliteDao.removeVariantProduct(data.idVariantOption, data.idProduct)
//	}
//
//
//	fun getLiveVariantMixProduct(idVariant: Long): LiveData<VariantWithVariantMix>{
//		return soliteDao.getLiveVariantMixProduct(idVariant)
//	}
//
//	fun getVariantMixProduct(idVariant: Long): VariantWithVariantMix {
//		return soliteDao.getVariantMixProduct(idVariant)
//	}
//
//	fun insertVariantMix(data: VariantMix): Long {
//		return soliteDao.insertVariantMix(data)
//	}
//
//	fun updateVariantMix(data: VariantMix) {
//		soliteDao.updateVariantMix(data)
//	}
//
//	fun removeVariantMix(data: VariantMix){
//		soliteDao.removeVariantMix(data.idVariant, data.idProduct)
//	}
//
//	fun getCategories(query: SimpleSQLiteQuery): LiveData<List<Category>>{
//		return soliteDao.getCategories(query)
//	}
//
//	fun insertCategory(data: Category): Long {
//		return soliteDao.insertCategory(data)
//	}
//
//	fun updateCategory(data: Category){
//		soliteDao.updateCategory(data)
//	}
//
//	val variants: List<Variant>
//		get() = soliteDao.getVariants()
//
//	fun insertVariants(data: List<Variant>) {
//		return soliteDao.insertVariants(data)
//	}
//
//	fun insertVariant(data: Variant): Long {
//		return soliteDao.insertVariant(data)
//	}
//
//	fun updateVariant(data: Variant){
//		soliteDao.updateVariant(data)
//	}
//
//	fun getVariantOptions(query: SupportSQLiteQuery): LiveData<List<VariantOption>>{
//		return soliteDao.getVariantOptions(query)
//	}
//
//	fun insertVariantOption(data: VariantOption): Long {
//		return soliteDao.insertVariantOption(data)
//	}
//
//	fun updateVariantOption(data: VariantOption){
//		soliteDao.updateVariantOption(data)
//	}
//
//	val customers: List<Customer>
//		get() = soliteDao.getCustomers()
//
//	fun insertCustomers(data: List<Customer>){
//		return soliteDao.insertCustomers(data)
//	}
//
//	fun insertCustomer(data: Customer): Long{
//		return soliteDao.insertCustomer(data)
//	}
//
//	fun updateCustomer(data: Customer){
//		soliteDao.updateCustomer(data)
//	}
//
//	val suppliers: List<Supplier>
//	get() = soliteDao.getSuppliers()
//
//	fun insertSuppliers(data: List<Supplier>){
//		soliteDao.insertSuppliers(data)
//	}
//
//	fun insertSupplier(data: Supplier): Long {
//		return soliteDao.insertSupplier(data)
//	}
//
//	fun updateSupplier(data: Supplier){
//		soliteDao.updateSupplier(data)
//	}
//
//	val payments: List<Payment>
//		get() = soliteDao.getPayments()
//
//	fun insertPayments(data: List<Payment>){
//		return soliteDao.insertPayments(data)
//	}
//
//	fun insertPayment(data: Payment): Long {
//		return soliteDao.insertPayment(data)
//	}
//
//	fun updatePayment(data: Payment){
//		soliteDao.updatePayment(data)
//	}
//
//	fun getOutcome(date: String): List<Outcome>{
//		return soliteDao.getOutcome(date)
//	}
//
//	fun insertOutcomes(data: List<Outcome>){
//		return soliteDao.insertOutcomes(data)
//	}
//
//	fun insertOutcome(data: Outcome): Long{
//		return soliteDao.insertOutcome(data)
//	}
//
//	fun updateOutcome(data: Outcome){
//		soliteDao.updateOutcome(data)
//	}
//
//	fun fillData(){
//		soliteDao.fillData()
//	}
}
