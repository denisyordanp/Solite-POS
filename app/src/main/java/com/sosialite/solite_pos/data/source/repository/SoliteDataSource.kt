package com.sosialite.solite_pos.data.source.repository

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
import com.sosialite.solite_pos.data.source.remote.response.helper.ApiResponse
import com.sosialite.solite_pos.vo.Resource

internal interface SoliteDataSource{

	fun getOrderDetail(status: Int, date: String): LiveData<Resource<List<OrderWithProduct>>>
	fun insertPaymentOrder(payment: OrderPayment, callback: (ApiResponse<LiveData<OrderWithProduct>>) -> Unit)
	fun newOrder(order: OrderWithProduct, callback: (ApiResponse<Boolean>) -> Unit)
	fun updateOrder(order: Order, callback: (ApiResponse<Boolean>) -> Unit)
	fun cancelOrder(order: OrderWithProduct, callback: (ApiResponse<Boolean>) -> Unit)

	val purchases: LiveData<Resource<List<PurchaseWithProduct>>>
	fun newPurchase(data: PurchaseWithProduct, callback: (ApiResponse<Boolean>) -> Unit)

	fun getDataProduct(idCategory: Long): LiveData<Resource<List<DataProduct>>>

	fun getVariantProduct(idProduct: Long, idVariantOption: Long): LiveData<Resource<List<VariantProduct>>>
	fun getVariantProductById(idProduct: Long): LiveData<Resource<VariantProduct?>>
	fun insertVariantProduct(data: VariantProduct, callback: (ApiResponse<Long>) -> Unit)
	fun removeVariantProduct(data: VariantProduct, callback: (ApiResponse<Boolean>) -> Unit)

	fun getVariantMixProduct(idVariant: Long): LiveData<Resource<VariantWithVariantMix>>
	fun insertVariantMix(data: VariantMix, callback: (ApiResponse<Long>) -> Unit)
	fun removeVariantMix(data: VariantMix, callback: (ApiResponse<Boolean>) -> Unit)

	fun getProductWithCategories(category: Long): LiveData<Resource<List<ProductWithCategory>>>
	fun insertProduct(data: Product, callback: (ApiResponse<Long>) -> Unit)
	fun updateProduct(data: Product, callback: (ApiResponse<Boolean>) -> Unit)

	fun getCategories(query: SimpleSQLiteQuery): LiveData<Resource<List<Category>>>
	fun insertCategory(data: Category, callback: (ApiResponse<Long>) -> Unit)
	fun updateCategory(data: Category, callback: (ApiResponse<Boolean>) -> Unit)

	val variants: LiveData<Resource<List<Variant>>>
	fun insertVariant(data: Variant, callback: (ApiResponse<Long>) -> Unit)
	fun updateVariant(data: Variant, callback: (ApiResponse<Boolean>) -> Unit)

	fun getVariantOptions(query: SupportSQLiteQuery): LiveData<Resource<List<VariantOption>>>
	fun insertVariantOption(data: VariantOption, callback: (ApiResponse<Long>) -> Unit)
	fun updateVariantOption(data: VariantOption, callback: (ApiResponse<Boolean>) -> Unit)

	val customers: LiveData<Resource<List<Customer>>>
	fun insertCustomer(data: Customer, callback: (ApiResponse<Long>) -> Unit)
	fun updateCustomer(data: Customer, callback: (ApiResponse<Boolean>) -> Unit)

	val suppliers: LiveData<Resource<List<Supplier>>>
	fun insertSupplier(data: Supplier, callback: (ApiResponse<Long>) -> Unit)
	fun updateSupplier(data: Supplier, callback: (ApiResponse<Boolean>) -> Unit)

	val payments: LiveData<Resource<List<Payment>>>
	fun insertPayment(data: Payment, callback: (ApiResponse<Long>) -> Unit)
	fun updatePayment(data: Payment, callback: (ApiResponse<Boolean>) -> Unit)

	fun getOutcomes(date: String): LiveData<Resource<List<Outcome>>>
	fun insertOutcome(data: Outcome, callback: (ApiResponse<Long>) -> Unit)
	fun updateOutcome(data: Outcome, callback: (ApiResponse<Boolean>) -> Unit)
}
