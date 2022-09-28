package com.socialite.solite_pos.utils.di

import android.content.Context
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.getInstance
import com.socialite.solite_pos.data.source.local.room.LocalDataSource
import com.socialite.solite_pos.data.source.repository.CategoriesRepository
import com.socialite.solite_pos.data.source.repository.CustomersRepository
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.data.source.repository.SoliteRepository
import com.socialite.solite_pos.data.source.repository.SuppliersRepository
import com.socialite.solite_pos.data.source.repository.VariantOptionsRepository
import com.socialite.solite_pos.data.source.repository.VariantsRepository
import com.socialite.solite_pos.data.source.repository.impl.CategoriesRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.CustomersRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.OutcomesRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.PaymentsRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.ProductsRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.SuppliersRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.VariantOptionsRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.VariantsRepositoryImpl
import com.socialite.solite_pos.utils.database.AppExecutors

object Injection {

    fun provideSoliteRepository(context: Context): SoliteRepository {
        val database = getInstance(context)
        val appExecutors = AppExecutors(context)
        val localDataSource = LocalDataSource.getInstance(database.soliteDao())
        return SoliteRepository.getInstance(appExecutors, localDataSource)
    }

    fun providePaymentsRepository(context: Context): PaymentsRepository {
        val database = getInstance(context)
        return PaymentsRepositoryImpl.getInstance(database.paymentsDao())
    }

    fun provideSupplierRepository(context: Context): SuppliersRepository {
        val database = getInstance(context)
        return SuppliersRepositoryImpl.getInstance(database.suppliersDao())
    }

    fun provideCustomersRepository(context: Context): CustomersRepository {
        val database = getInstance(context)
        return CustomersRepositoryImpl.getInstance(database.customersDao())
    }

    fun provideVariantsRepository(context: Context): VariantsRepository {
        val database = getInstance(context)
        return VariantsRepositoryImpl.getInstance(database.variantsDao())
    }

    fun provideVariantOptionsRepository(context: Context): VariantOptionsRepository {
        val database = getInstance(context)
        return VariantOptionsRepositoryImpl.getInstance(database.variantOptionsDao())
    }

    fun provideCategoriesRepository(context: Context): CategoriesRepository {
        val database = getInstance(context)
        return CategoriesRepositoryImpl.getInstance(database.categoriesDao())
    }

    fun provideOutcomesRepository(context: Context): OutcomesRepository {
        val database = getInstance(context)
        return OutcomesRepositoryImpl.getInstance(database.outcomesDao())
    }

    fun provideProductsRepository(context: Context): ProductsRepository {
        val database = getInstance(context)
        return ProductsRepositoryImpl.getInstance(database.productsDao())
    }
}
