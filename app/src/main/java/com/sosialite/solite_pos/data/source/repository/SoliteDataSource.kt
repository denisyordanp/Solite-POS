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

internal interface SoliteDataSource{

	fun getOrderDetail(status: Int): List<OrderWithProduct>
	fun insertPaymentOrder(payment: OrderPayment): OrderWithProduct
	fun newOrder(order: OrderWithProduct)
	fun updateOrder(order: Order)
	fun cancelOrder(order: OrderWithProduct)

	fun getPurchase(): List<PurchaseWithProduct>
	fun newPurchase(data: PurchaseWithProduct)

	fun getDataProduct(idCategory: Int): LiveData<List<DataProduct>>

	fun getVariantProduct(idProduct: Int, idVariantOption: Int): List<VariantProduct>
	fun insertVariantProduct(data: VariantProduct)
	fun removeVariantProduct(data: VariantProduct)

	fun getLiveVariantMixProduct(idVariant: Int): LiveData<VariantWithVariantMix>
	fun getVariantMixProduct(idVariant: Int): VariantWithVariantMix
	fun insertVariantMix(data: VariantMix)
	fun removeVariantMix(data: VariantMix)

	fun getProductWithCategories(category: Int): LiveData<List<ProductWithCategory>>
	fun insertProduct(data: Product): Long
	fun updateProduct(data: Product)

	fun getCategories(query: SimpleSQLiteQuery): LiveData<List<Category>>
	fun insertCategory(data: Category)
	fun updateCategory(data: Category)

	val variants: LiveData<List<Variant>>
	fun insertVariant(data: Variant)
	fun updateVariant(data: Variant)

	fun getVariantOptions(query: SupportSQLiteQuery): LiveData<List<VariantOption>>
	fun insertVariantOption(data: VariantOption)
	fun updateVariantOption(data: VariantOption)

	val customers: LiveData<List<Customer>>
	fun insertCustomer(data: Customer): Long
	fun updateCustomer(data: Customer)

	val suppliers: LiveData<List<Supplier>>
	fun insertSupplier(data: Supplier): Long
	fun updateSupplier(data: Supplier)

	val payments: LiveData<List<Payment>>
	fun insertPayment(data: Payment)
	fun updatePayment(data: Payment)

	fun getOutcome(date: String): LiveData<List<Outcome>>
	fun insertOutcome(data: Outcome)
	fun updateOutcome(data: Outcome)
	fun fillData()
}
