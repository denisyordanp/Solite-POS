package com.sosialite.solite_pos.data.source.repository

import androidx.lifecycle.LiveData
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

internal interface SoliteDataSource{

	fun getOrderDetail(status: Int, date: String): List<OrderWithProduct>
	fun insertPaymentOrder(payment: OrderPayment): OrderWithProduct
	fun newOrder(order: OrderWithProduct)
	fun updateOrder(order: Order)
	fun cancelOrder(order: OrderWithProduct)

	fun getPurchase(): List<PurchaseWithProduct>
	fun newPurchase(data: PurchaseWithProduct)

	fun getDataProduct(idCategory: Int): LiveData<List<DataProduct>>

	fun getVariantProduct(idProduct: Int, idVariantOption: Int): List<VariantProduct>
	fun getVariantProductById(idProduct: Int): VariantProduct?
	fun insertVariantProduct(data: VariantProduct, callback: (ApiResponse<Int>) -> Unit)
	fun removeVariantProduct(data: VariantProduct, callback: (ApiResponse<Int>) -> Unit)

	fun getLiveVariantMixProduct(idVariant: Int): LiveData<VariantWithVariantMix>
	fun getVariantMixProduct(idVariant: Int): VariantWithVariantMix
	fun insertVariantMix(data: VariantMix, callback: (ApiResponse<Int>) -> Unit)
	fun removeVariantMix(data: VariantMix, callback: (ApiResponse<Int>) -> Unit)

	fun getProductWithCategories(category: Int): LiveData<List<ProductWithCategory>>
	fun insertProduct(data: Product, callback: (ApiResponse<Int>) -> Unit)
	fun updateProduct(data: Product, callback: (ApiResponse<Int>) -> Unit)

	fun getCategories(query: SimpleSQLiteQuery): LiveData<List<Category>>
	fun insertCategory(data: Category, callback: (ApiResponse<Int>) -> Unit)
	fun updateCategory(data: Category, callback: (ApiResponse<Int>) -> Unit)

	val variants: LiveData<List<Variant>>
	fun insertVariant(data: Variant, callback: (ApiResponse<Int>) -> Unit)
	fun updateVariant(data: Variant, callback: (ApiResponse<Int>) -> Unit)

	fun getVariantOptions(query: SupportSQLiteQuery): LiveData<List<VariantOption>>
	fun insertVariantOption(data: VariantOption, callback: (ApiResponse<Int>) -> Unit)
	fun updateVariantOption(data: VariantOption, callback: (ApiResponse<Int>) -> Unit)

	val customers: LiveData<List<Customer>>
	fun insertCustomer(data: Customer, callback: (ApiResponse<Int>) -> Unit)
	fun updateCustomer(data: Customer, callback: (ApiResponse<Boolean>) -> Unit)

	val suppliers: LiveData<List<Supplier>>
	fun insertSupplier(data: Supplier, callback: (ApiResponse<Int>) -> Unit)
	fun updateSupplier(data: Supplier, callback: (ApiResponse<Int>) -> Unit)

	val payments: LiveData<List<Payment>>
	fun insertPayment(data: Payment, callback: (ApiResponse<Int>) -> Unit)
	fun updatePayment(data: Payment, callback: (ApiResponse<Int>) -> Unit)

	fun getOutcome(date: String): LiveData<List<Outcome>>
	fun insertOutcome(data: Outcome, callback: (ApiResponse<Int>) -> Unit)
	fun updateOutcome(data: Outcome, callback: (ApiResponse<Int>) -> Unit)
	fun fillData()
}
