package com.sosialite.solite_pos.data.source.repository

import androidx.lifecycle.LiveData
import com.sosialite.solite_pos.data.source.local.entity.helper.ProductWithCategory
import com.sosialite.solite_pos.data.source.local.entity.main.Category
import com.sosialite.solite_pos.data.source.local.entity.main.Customer
import com.sosialite.solite_pos.data.source.local.entity.main.Product
import com.sosialite.solite_pos.data.source.local.entity.main.Variant
import com.sosialite.solite_pos.data.source.local.room.LocalDataSource
import com.sosialite.solite_pos.data.source.remote.RemoteDataSource
import com.sosialite.solite_pos.utils.database.AppExecutors

class SoliteRepository private constructor(private val remoteDataSource: RemoteDataSource, private val localDataSource: LocalDataSource, private val appExecutors: AppExecutors) : SoliteDataSource {

	companion object {
		@Volatile
		private var INSTANCE: SoliteRepository? = null
//		private val TAG = BaseKey.DATA_LOGS + BerkarirRepository::class.java.simpleName

		fun getInstance(remoteData: RemoteDataSource, localDataSource: LocalDataSource, appExecutors: AppExecutors): SoliteRepository {
			if (INSTANCE == null) {
				synchronized(SoliteRepository::class.java) {
					if (INSTANCE == null) {
						INSTANCE = SoliteRepository(remoteData, localDataSource, appExecutors)
					}
				}
			}
			return INSTANCE!!
		}
	}

	override fun getProducts(category: Int): LiveData<List<ProductWithCategory>> {
		return localDataSource.getProduct(category)
	}

	override fun insertProducts(data: List<Product>) {
		localDataSource.insertProducts(data)
	}

	override val categories: LiveData<List<Category>>
		get() = localDataSource.categories

	override fun insertCategories(data: List<Category>) {
		localDataSource.insertCategories(data)
	}

	override val variants: LiveData<List<Variant>>
		get() = localDataSource.variants

	override fun insertVariants(data: List<Variant>) {
		localDataSource.insertVariants(data)
	}

	override val customers: LiveData<List<Customer>>
		get() = localDataSource.customers

	override fun insertCustomers(data: List<Customer>) {
		localDataSource.insertCustomers(data)
	}
}
