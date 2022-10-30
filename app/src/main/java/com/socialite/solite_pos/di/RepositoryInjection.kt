package com.socialite.solite_pos.di

import android.content.Context
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.getInstance
import com.socialite.solite_pos.data.source.repository.CategoriesRepository
import com.socialite.solite_pos.data.source.repository.CustomersRepository
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.data.source.repository.SoliteRepository
import com.socialite.solite_pos.data.source.repository.StoreRepository
import com.socialite.solite_pos.data.source.repository.SuppliersRepository
import com.socialite.solite_pos.data.source.repository.VariantMixesRepository
import com.socialite.solite_pos.data.source.repository.VariantOptionsRepository
import com.socialite.solite_pos.data.source.repository.VariantsRepository
import com.socialite.solite_pos.data.source.repository.impl.CategoriesRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.CustomersRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.OrdersRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.OutcomesRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.PaymentsRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.ProductVariantsRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.ProductsRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.SettingRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.StoreRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.SuppliersRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.VariantMixesRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.VariantOptionsRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.VariantsRepositoryImpl
import com.socialite.solite_pos.utils.database.AppExecutors

object RepositoryInjection {

    fun provideSoliteRepository(context: Context): SoliteRepository {
        val database = getInstance(context)
        val appExecutors = AppExecutors(context)
        return SoliteRepository.getInstance(appExecutors, database.soliteDao())
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
        return OutcomesRepositoryImpl.getInstance(
            database.outcomesDao(),
            SettingRepositoryImpl.getDataStoreInstance(context)
        )
    }

    fun provideProductsRepository(context: Context): ProductsRepository {
        val database = getInstance(context)
        return ProductsRepositoryImpl.getInstance(database.productsDao())
    }

    fun provideProductVariantsRepository(context: Context): ProductVariantsRepository {
        val database = getInstance(context)
        return ProductVariantsRepositoryImpl.getInstance(database.productVariantsDao())
    }

    fun provideOrdersRepository(context: Context): OrdersRepository {
        val database = getInstance(context)
        return OrdersRepositoryImpl.getInstance(
            database.ordersDao(),
            SettingRepositoryImpl.getDataStoreInstance(context)
        )
    }

    fun provideVariantMixesRepository(context: Context): VariantMixesRepository {
        val database = getInstance(context)
        return VariantMixesRepositoryImpl.getInstance(database.variantMixesDao())
    }

    fun provideStoreRepository(context: Context): StoreRepository {
        val database = getInstance(context)
        return StoreRepositoryImpl.getInstance(database.storeDao())
    }

    fun provideSettingRepository(context: Context): SettingRepository {
        return SettingRepositoryImpl.getDataStoreInstance(context)
    }
}
