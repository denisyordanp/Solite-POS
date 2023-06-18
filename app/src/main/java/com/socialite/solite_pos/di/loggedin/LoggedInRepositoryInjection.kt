package com.socialite.solite_pos.di.loggedin

import android.content.Context
import com.socialite.solite_pos.builder.RemoteConfigManager
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.getInstance
import com.socialite.solite_pos.data.source.preference.impl.UserPreferencesImpl
import com.socialite.solite_pos.data.source.repository.CategoriesRepository
import com.socialite.solite_pos.data.source.repository.CustomersRepository
import com.socialite.solite_pos.data.source.repository.OrderDetailsRepository
import com.socialite.solite_pos.data.source.repository.OrderPaymentsRepository
import com.socialite.solite_pos.data.source.repository.OrderProductVariantsRepository
import com.socialite.solite_pos.data.source.repository.OrderPromosRepository
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.data.source.repository.PromosRepository
import com.socialite.solite_pos.data.source.repository.RemoteConfigRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.data.source.repository.StoreRepository
import com.socialite.solite_pos.data.source.repository.SuppliersRepository
import com.socialite.solite_pos.data.source.repository.VariantMixesRepository
import com.socialite.solite_pos.data.source.repository.VariantOptionsRepository
import com.socialite.solite_pos.data.source.repository.VariantsRepository
import com.socialite.solite_pos.data.source.repository.impl.CategoriesRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.CustomersRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.OrderDetailsRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.OrderPaymentsRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.OrderProductVariantsRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.OrderPromosRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.OrdersRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.OutcomesRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.PaymentsRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.ProductVariantsRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.ProductsRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.PromosRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.RemoteConfigRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.SettingRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.StoreRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.SuppliersRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.VariantMixesRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.VariantOptionsRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.VariantsRepositoryImpl

object LoggedInRepositoryInjection {

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

    fun provideOrderProductVariantsRepository(context: Context): OrderProductVariantsRepository {
        val database = getInstance(context)
        return OrderProductVariantsRepositoryImpl.getInstance(
            dao = database.orderProductVariantsDao(),
            orderDetailsDao = database.orderDetailsDao(),
            variantOptionsDao = database.variantOptionsDao(),
            db = database
        )
    }

    fun provideCategoriesRepository(context: Context): CategoriesRepository {
        val database = getInstance(context)
        return CategoriesRepositoryImpl.getInstance(database.categoriesDao(), database)
    }

    fun provideOutcomesRepository(context: Context): OutcomesRepository {
        val database = getInstance(context)
        val userPreferences = UserPreferencesImpl.getInstance(context)
        return OutcomesRepositoryImpl.getInstance(
            dao = database.outcomesDao(),
            storesDao = database.storeDao(),
            settingRepository = SettingRepositoryImpl.getDataStoreInstance(
                context,
                userPreferences
            ),
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
        val userPreferences = UserPreferencesImpl.getInstance(context)
        return OrdersRepositoryImpl.getInstance(
            dao = database.ordersDao(),
            customersDao = database.customersDao(),
            storesDao = database.storeDao(),
            settingRepository = SettingRepositoryImpl.getDataStoreInstance(
                context,
                userPreferences
            ),
            db = database
        )
    }

    fun provideOrderDetailsRepository(context: Context): OrderDetailsRepository {
        val database = getInstance(context)
        return OrderDetailsRepositoryImpl.getInstance(
            dao = database.orderDetailsDao(),
            ordersDao = database.ordersDao(),
            productsDao = database.productsDao(),
            orderProductVariantsDao = database.orderProductVariantsDao(),
            db = database
        )
    }

    fun provideOrderPaymentsRepository(context: Context): OrderPaymentsRepository {
        val database = getInstance(context)
        return OrderPaymentsRepositoryImpl.getInstance(
            dao = database.orderPaymentsDao(),
            ordersDao = database.ordersDao(),
            paymentDao = database.paymentsDao(),
            db = database
        )
    }

    fun provideOrderPromosRepository(context: Context): OrderPromosRepository {
        val database = getInstance(context)
        return OrderPromosRepositoryImpl.getInstance(
            dao = database.orderPromosDao(),
            ordersDao = database.ordersDao(),
            promosDao = database.promoDao(),
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
        val userPreferences = UserPreferencesImpl.getInstance(context)
        return SettingRepositoryImpl.getDataStoreInstance(context, userPreferences)
    }

    fun providePromosRepository(context: Context): PromosRepository {
        val database = getInstance(context)
        return PromosRepositoryImpl.getInstance(dao = database.promoDao(), database)
    }

    fun provideRemoteConfigRepository(context: Context): RemoteConfigRepository {
        return RemoteConfigRepositoryImpl(RemoteConfigManager.getInstance(context))
    }
}