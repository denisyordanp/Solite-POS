package com.sosialite.solite_pos.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sosialite.solite_pos.data.source.local.entity.helper.ProductWithCategory
import com.sosialite.solite_pos.data.source.local.entity.main.Category
import com.sosialite.solite_pos.data.source.local.entity.main.Customer
import com.sosialite.solite_pos.data.source.local.entity.main.Product
import com.sosialite.solite_pos.data.source.local.entity.main.Variant
import com.sosialite.solite_pos.data.source.repository.SoliteRepository

class MainViewModel(private val repository: SoliteRepository) : ViewModel(){

	fun getProducts(category: Int?): LiveData<List<ProductWithCategory>>? {
		return if (category != null){
			repository.getProducts(category)
		}else{
			null
		}
	}

	fun insertProducts(data: List<Product>){
		repository.insertProducts(data)
	}

	val categories: LiveData<List<Category>>
	get() = repository.categories

	fun insertCategories(data: List<Category>){
		repository.insertCategories(data)
	}

	val variants: LiveData<List<Variant>>
		get() = repository.variants

	fun insertVariants(data: List<Variant>){
		repository.insertVariants(data)
	}

	val customers: LiveData<List<Customer>>
		get() = repository.customers

	fun insertCustomers(data: List<Customer>){
		repository.insertCustomers(data)
	}
}
