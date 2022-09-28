package com.socialite.solite_pos.data.source.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.helper.*
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.helper.PurchaseWithSupplier
import com.socialite.solite_pos.data.source.local.entity.room.helper.VariantWithVariantMix
import com.socialite.solite_pos.data.source.local.entity.room.master.*
import com.socialite.solite_pos.data.source.remote.response.helper.ApiResponse
import com.socialite.solite_pos.vo.Resource

internal interface SoliteDataSource{

	fun getOrderList(status: Int, date: String): LiveData<Resource<List<OrderData>>>
	fun getLocalOrders(status: Int, date: String): LiveData<List<OrderData>>
	fun getProductOrder(orderNo: String): LiveData<Resource<List<ProductOrderDetail>>>

	fun newOrder(order: OrderWithProduct)
	fun updateOrder(order: Order)
	fun replaceProductOrder(old: OrderWithProduct, new: OrderWithProduct)

	val purchases: LiveData<Resource<List<PurchaseWithSupplier>>>
	fun getPurchaseProducts(purchaseNo: String): LiveData<Resource<List<PurchaseProductWithProduct>>>
	fun newPurchase(data: PurchaseWithProduct, callback: (ApiResponse<Boolean>) -> Unit)

	fun getProductList(idCategory: Long): LiveData<Resource<List<Products>>>
	fun getProducts(idCategory: Long): LiveData<Resource<List<ProductWithCategory>>>

	fun getProductVariantOptions(idProduct: Long): LiveData<Resource<List<VariantWithOptions>?>>
	fun getVariantProduct(idProduct: Long, idVariantOption: Long): LiveData<Resource<VariantProduct?>>
	fun getVariantProductById(idProduct: Long): LiveData<Resource<VariantProduct?>>
	fun insertVariantProduct(data: VariantProduct, callback: (ApiResponse<Long>) -> Unit)
	fun removeVariantProduct(data: VariantProduct, callback: (ApiResponse<Boolean>) -> Unit)

	fun getVariantMixProductById(idVariant: Long, idProduct: Long): LiveData<Resource<VariantMix?>>
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

    fun getOutcomes(date: String): LiveData<Resource<List<Outcome>>>
    fun insertOutcome(data: Outcome, callback: (ApiResponse<Long>) -> Unit)
    fun updateOutcome(data: Outcome, callback: (ApiResponse<Boolean>) -> Unit)

    fun getUsers(userId: String): LiveData<Resource<User?>>
}
