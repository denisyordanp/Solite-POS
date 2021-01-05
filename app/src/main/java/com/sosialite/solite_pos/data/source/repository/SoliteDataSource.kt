package com.sosialite.solite_pos.data.source.repository

import androidx.lifecycle.LiveData
import com.sosialite.solite_pos.data.source.local.entity.helper.ProductWithCategory
import com.sosialite.solite_pos.data.source.local.entity.main.Category
import com.sosialite.solite_pos.data.source.local.entity.main.Customer
import com.sosialite.solite_pos.data.source.local.entity.main.Product
import com.sosialite.solite_pos.data.source.local.entity.main.Variant

internal interface SoliteDataSource{
	fun getProducts(category: Int): LiveData<List<ProductWithCategory>>
	fun insertProducts(data: List<Product>)

	val categories: LiveData<List<Category>>
	fun insertCategories(data: List<Category>)

	val variants: LiveData<List<Variant>>
	fun insertVariants(data: List<Variant>)

	val customers: LiveData<List<Customer>>
	fun insertCustomers(data: List<Customer>)
}
