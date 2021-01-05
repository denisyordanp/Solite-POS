package com.sosialite.solite_pos.data.source.local.room

import androidx.lifecycle.LiveData
import com.sosialite.solite_pos.data.source.local.entity.helper.ProductWithCategory
import com.sosialite.solite_pos.data.source.local.entity.main.Category
import com.sosialite.solite_pos.data.source.local.entity.main.Customer
import com.sosialite.solite_pos.data.source.local.entity.main.Product
import com.sosialite.solite_pos.data.source.local.entity.main.Variant

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

	fun getProduct(category: Int): LiveData<List<ProductWithCategory>>{
		return soliteDao.getProducts(category)
	}

	fun insertProducts(data: List<Product>){
		soliteDao.insertProducts(data)
	}

	val categories: LiveData<List<Category>>
		get() = soliteDao.getCategories()

	fun insertCategories(data: List<Category>){
		soliteDao.insertCategories(data)
	}

	val variants: LiveData<List<Variant>>
		get() = soliteDao.getVariants()

	fun insertVariants(data: List<Variant>){
		soliteDao.insertVariants(data)
	}

	val customers: LiveData<List<Customer>>
		get() = soliteDao.getCustomers()

	fun insertCustomers(data: List<Customer>){
		soliteDao.insertCustomers(data)
	}
}
