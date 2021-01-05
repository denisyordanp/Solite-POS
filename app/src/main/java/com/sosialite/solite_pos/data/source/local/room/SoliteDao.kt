package com.sosialite.solite_pos.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sosialite.solite_pos.data.source.local.entity.helper.ProductWithCategory
import com.sosialite.solite_pos.data.source.local.entity.main.Category
import com.sosialite.solite_pos.data.source.local.entity.main.Customer
import com.sosialite.solite_pos.data.source.local.entity.main.Product
import com.sosialite.solite_pos.data.source.local.entity.main.Variant

@Dao
interface SoliteDao{
	@Query("SELECT * FROM ${AppDatabase.TBL_PRODUCT} WHERE ${Category.ID} = :category")
	@Transaction
	fun getProducts(category: Int): LiveData<List<ProductWithCategory>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertProducts(data: List<Product>)

	@Query("SELECT * FROM ${AppDatabase.TBL_CATEGORY}")
	fun getCategories(): LiveData<List<Category>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertCategories(data: List<Category>)

	@Query("SELECT * FROM ${AppDatabase.TBL_VARIANT}")
	fun getVariants(): LiveData<List<Variant>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariants(data: List<Variant>)

	@Query("SELECT * FROM ${AppDatabase.TBL_CUSTOMER}")
	fun getCustomers(): LiveData<List<Customer>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertCustomers(data: List<Customer>)
}
