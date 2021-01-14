package com.sosialite.solite_pos.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.sosialite.solite_pos.data.source.local.entity.room.helper.DataProduct
import com.sosialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.sosialite.solite_pos.data.source.local.entity.room.helper.VariantWithVariantMix
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.sosialite.solite_pos.data.source.local.entity.room.master.*

@Dao
interface SoliteDao{

//	@Query("SELECT * FROM '${AppDatabase.TBL_ORDER}'")
//	fun getOrders(): LiveData<List<OrderWithProduct>>
//
//	@Query("SELECT * FROM '${AppDatabase.TBL_ORDER_PRODUCT}' WHERE ${Order.NO} = :orderNo")
//	fun getDetailOrders(orderNo: String): LiveData<List<DetailOrder>>

	@Query("SELECT * FROM ${AppDatabase.TBL_PRODUCT} WHERE ${Category.ID} = :category")
	fun getProductWithCategories(category: Int): LiveData<List<ProductWithCategory>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertProduct(data: Product): Long

	@Update
	fun updateProduct(data: Product)

//	@Transaction
//	fun newProduct(data: DataProduct){
//		if (data.product != null){
//			val idProduct = insertProduct(data.product!!)
//		}
//
//	}

//	VARIANT PRODUCT

	@Query("SELECT * FROM ${AppDatabase.TBL_PRODUCT} WHERE ${Category.ID} = :idCategory")
	fun getDataProduct(idCategory: Int): LiveData<List<DataProduct>>

	@Query("SELECT * FROM ${AppDatabase.TBL_VARIANT_PRODUCT} WHERE ${Product.ID} = :idProduct AND ${VariantOption.ID} = :idVariantOption")
	fun getVariantProduct(idProduct: Int, idVariantOption: Int): List<VariantProduct>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariantProduct(data: VariantProduct)

	@Query("DELETE FROM ${AppDatabase.TBL_VARIANT_PRODUCT} WHERE ${VariantOption.ID} = :idVariant AND ${Product.ID} = :idProduct")
	fun removeVariantProduct(idVariant: Int, idProduct: Int)

//	VARIANT MIX

	@Query("SELECT * FROM ${AppDatabase.TBL_VARIANT} WHERE ${Variant.ID} = :idVariant")
	fun getVariantMixProduct(idVariant: Int): VariantWithVariantMix

	@Query("SELECT * FROM ${AppDatabase.TBL_VARIANT} WHERE ${Variant.ID} = :idVariant")
	fun getLiveVariantMixProduct(idVariant: Int): LiveData<VariantWithVariantMix>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariantMix(data: VariantMix)

	@Query("DELETE FROM ${AppDatabase.TBL_VARIANT_MIX} WHERE ${Variant.ID} = :idVariant AND ${Product.ID} = :idProduct")
	fun removeVariantMix(idVariant: Int, idProduct: Int)

//	CATEGORY

	@RawQuery(observedEntities = [Category::class])
	fun getCategories(query: SupportSQLiteQuery): LiveData<List<Category>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertCategory(data: Category): Long

	@Update
	fun updateCategory(data: Category)

//	VARIANT

	@Query("SELECT * FROM ${AppDatabase.TBL_VARIANT}")
	fun getVariants(): LiveData<List<Variant>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariant(data: Variant)

	@Update
	fun updateVariant(data: Variant)

//	VARIANT OPTIONS

	@RawQuery(observedEntities = [VariantOption::class])
	fun getVariantOptions(query: SupportSQLiteQuery): LiveData<List<VariantOption>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariantOption(data: VariantOption)

	@Update
	fun updateVariantOption(data: VariantOption)

//	CUSTOMERS

	@Query("SELECT * FROM ${AppDatabase.TBL_CUSTOMER}")
	fun getCustomers(): LiveData<List<Customer>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertCustomer(data: Customer)

	@Update
	fun updateCustomer(data: Customer)

//	PAYMENTS

	@Query("SELECT * FROM ${AppDatabase.TBL_PAYMENT}")
	fun getPayments(): LiveData<List<Payment>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertPayment(data: Payment)

	@Update
	fun updatePayment(data: Payment)
}
