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
import com.sosialite.solite_pos.vo.Resource

internal interface SoliteDataSource{

	fun getOrderDetail(status: Int, date: String): List<OrderWithProduct>
	fun insertPaymentOrder(payment: OrderPayment): OrderWithProduct
	fun newOrder(order: OrderWithProduct, callback: (ApiResponse<Boolean>) -> Unit)
	fun updateOrder(order: Order, callback: (ApiResponse<Boolean>) -> Unit)
	fun cancelOrder(order: OrderWithProduct)

	fun getPurchase(): List<PurchaseWithProduct>
	fun newPurchase(data: PurchaseWithProduct)

	fun getDataProduct(idCategory: Long): LiveData<Resource<List<DataProduct>>>

	fun getVariantProduct(idProduct: Long, idVariantOption: Long): List<VariantProduct>
	fun getVariantProductById(idProduct: Long): VariantProduct?
	fun insertVariantProduct(data: VariantProduct, callback: (ApiResponse<Long>) -> Unit)
	fun removeVariantProduct(data: VariantProduct, callback: (ApiResponse<Boolean>) -> Unit)

	fun getLiveVariantMixProduct(idVariant: Long): LiveData<Resource<VariantWithVariantMix>>
	fun getVariantMixProduct(idVariant: Long): VariantWithVariantMix
	fun insertVariantMix(data: VariantMix, callback: (ApiResponse<Long>) -> Unit)
	fun removeVariantMix(data: VariantMix, callback: (ApiResponse<Boolean>) -> Unit)

	fun getProductWithCategories(category: Long): LiveData<Resource<List<ProductWithCategory>>>
	fun insertProduct(data: Product, callback: (ApiResponse<Long>) -> Unit)
	fun updateProduct(data: Product, callback: (ApiResponse<Boolean>) -> Unit)

	fun getCategories(query: SimpleSQLiteQuery): LiveData<Resource<List<Category>>>
	fun insertCategory(data: Category, callback: (ApiResponse<Long>) -> Unit)
	fun updateCategory(data: Category, callback: (ApiResponse<Boolean>) -> Unit)

	fun getVariants(callback: (ApiResponse<List<Variant>>) -> Unit)
	fun insertVariant(data: Variant, callback: (ApiResponse<Long>) -> Unit)
	fun updateVariant(data: Variant, callback: (ApiResponse<Boolean>) -> Unit)

	fun getVariantOptions(query: SupportSQLiteQuery): LiveData<Resource<List<VariantOption>>>
	fun insertVariantOption(data: VariantOption, callback: (ApiResponse<Long>) -> Unit)
	fun updateVariantOption(data: VariantOption, callback: (ApiResponse<Boolean>) -> Unit)

	fun getCustomers(callback: (ApiResponse<List<Customer>>) -> Unit)
	fun insertCustomer(data: Customer, callback: (ApiResponse<Long>) -> Unit)
	fun updateCustomer(data: Customer, callback: (ApiResponse<Boolean>) -> Unit)

	fun getSuppliers(callback: (ApiResponse<List<Supplier>>) -> Unit)
	fun insertSupplier(data: Supplier, callback: (ApiResponse<Long>) -> Unit)
	fun updateSupplier(data: Supplier, callback: (ApiResponse<Boolean>) -> Unit)

	fun getPayments(callback: (ApiResponse<List<Payment>>) -> Unit)
	fun insertPayment(data: Payment, callback: (ApiResponse<Long>) -> Unit)
	fun updatePayment(data: Payment, callback: (ApiResponse<Boolean>) -> Unit)

	fun getOutcomes(date: String, callback: (ApiResponse<List<Outcome>>) -> Unit)
	fun insertOutcome(data: Outcome, callback: (ApiResponse<Long>) -> Unit)
	fun updateOutcome(data: Outcome, callback: (ApiResponse<Boolean>) -> Unit)
	fun fillData()
}
