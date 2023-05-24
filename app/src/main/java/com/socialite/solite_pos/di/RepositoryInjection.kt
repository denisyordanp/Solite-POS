package com.socialite.solite_pos.di

import android.content.Context
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.getInstance
import com.socialite.solite_pos.data.source.preference.impl.UserPreferencesImpl
import com.socialite.solite_pos.data.source.repository.CategoriesRepository
import com.socialite.solite_pos.data.source.repository.CustomersRepository
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.data.source.repository.PromosRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.data.source.repository.StoreRepository
import com.socialite.solite_pos.data.source.repository.SuppliersRepository
import com.socialite.solite_pos.data.source.repository.UserRepository
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
import com.socialite.solite_pos.data.source.repository.impl.PromosRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.SettingRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.StoreRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.SuppliersRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.UserRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.VariantMixesRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.VariantOptionsRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.VariantsRepositoryImpl

object RepositoryInjection {

    fun providePaymentsRepository(context: Context): PaymentsRepository {
        val database = getInstance(context)
        return PaymentsRepositoryImpl.getInstance(database.paymentsDao(), database)
    }

    fun provideSupplierRepository(context: Context): SuppliersRepository {
        val database = getInstance(context)
        return SuppliersRepositoryImpl.getInstance(database.suppliersDao())
    }

    fun provideCustomersRepository(context: Context): CustomersRepository {
        val database = getInstance(context)
        return CustomersRepositoryImpl.getInstance(
            database.customersDao(),
            database
        )
    }

    fun provideVariantsRepository(context: Context): VariantsRepository {
        val database = getInstance(context)
        return VariantsRepositoryImpl.getInstance(database.variantsDao(), database)
    }

    fun provideVariantOptionsRepository(context: Context): VariantOptionsRepository {
        val database = getInstance(context)
        return VariantOptionsRepositoryImpl.getInstance(
            dao = database.variantOptionsDao(),
            variantsDao = database.variantsDao(),
            db = database
        )
    }

    fun provideCategoriesRepository(context: Context): CategoriesRepository {
        val database = getInstance(context)
        return CategoriesRepositoryImpl.getInstance(database.categoriesDao(), database)
    }

    fun provideOutcomesRepository(context: Context): OutcomesRepository {
        val database = getInstance(context)
        return OutcomesRepositoryImpl.getInstance(
            dao = database.outcomesDao(),
            storesDao = database.storeDao(),
            settingRepository = SettingRepositoryImpl.getDataStoreInstance(context),
            db = database
        )
    }

    fun provideProductsRepository(context: Context): ProductsRepository {
        val database = getInstance(context)
        return ProductsRepositoryImpl.getInstance(
            dao = database.productsDao(),
            categoryDao = database.categoriesDao(),
            db = database
        )
    }

    fun provideProductVariantsRepository(context: Context): ProductVariantsRepository {
        val database = getInstance(context)
        return ProductVariantsRepositoryImpl.getInstance(
            dao = database.productVariantsDao(),
            variantsDao = database.variantsDao(),
            variantOptionsDao = database.variantOptionsDao(),
            productsDao = database.productsDao(),
            db = database
        )
    }

    fun provideOrdersRepository(context: Context): OrdersRepository {
        val database = getInstance(context)
        return OrdersRepositoryImpl.getInstance(
            dao = database.ordersDao(),
            customersDao = database.customersDao(),
            storesDao = database.storeDao(),
            productsDao = database.productsDao(),
            paymentDao = database.paymentsDao(),
            promosDao = database.promoDao(),
            variantOptionsDao = database.variantOptionsDao(),
            settingRepository = SettingRepositoryImpl.getDataStoreInstance(context),
            db = database
        )
    }

    fun provideVariantMixesRepository(context: Context): VariantMixesRepository {
        val database = getInstance(context)
        return VariantMixesRepositoryImpl.getInstance(database.variantMixesDao())
    }

    fun provideStoreRepository(context: Context): StoreRepository {
        val database = getInstance(context)
        val settingRepository = provideSettingRepository(context)
        return StoreRepositoryImpl.getInstance(
            dao = database.storeDao(),
            settingRepository = settingRepository,
            db = database
        )
    }

    fun provideSettingRepository(context: Context): SettingRepository {
        return SettingRepositoryImpl.getDataStoreInstance(context)
    }

    fun providePromosRepository(context: Context): PromosRepository {
        val database = getInstance(context)
        return PromosRepositoryImpl.getInstance(dao = database.promoDao(), database)
    }

    fun provideUserRepository(context: Context): UserRepository {
        val userPreferences = UserPreferencesImpl.getInstance(context)
        val service = NetworkInjector.provideSoliteServices(userPreferences)
        return UserRepositoryImpl(service, userPreferences)
    }
}
