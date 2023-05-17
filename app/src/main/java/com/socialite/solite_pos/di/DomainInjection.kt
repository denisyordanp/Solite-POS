package com.socialite.solite_pos.di

import android.content.Context
import com.socialite.solite_pos.data.source.domain.GetOrdersGeneralMenuBadge
import com.socialite.solite_pos.data.source.domain.GetProductOrder
import com.socialite.solite_pos.data.source.domain.GetProductVariantOptions
import com.socialite.solite_pos.data.source.domain.GetRecapData
import com.socialite.solite_pos.data.source.domain.MigrateToUUID
import com.socialite.solite_pos.data.source.domain.NewOrder
import com.socialite.solite_pos.data.source.domain.NewOutcome
import com.socialite.solite_pos.data.source.domain.PayOrder
import com.socialite.solite_pos.data.source.domain.UpdateOrderProducts
import com.socialite.solite_pos.data.source.domain.impl.GetOrdersGeneralMenuBadgeImpl
import com.socialite.solite_pos.data.source.domain.impl.GetProductOrderImpl
import com.socialite.solite_pos.data.source.domain.impl.GetProductVariantOptionsImpl
import com.socialite.solite_pos.data.source.domain.impl.GetRecapDataImpl
import com.socialite.solite_pos.data.source.domain.impl.MigrateToUUIDImpl
import com.socialite.solite_pos.data.source.domain.impl.NewOrderImpl
import com.socialite.solite_pos.data.source.domain.impl.NewOutcomeImpl
import com.socialite.solite_pos.data.source.domain.impl.PayOrderImpl
import com.socialite.solite_pos.data.source.domain.impl.UpdateOrderProductsImpl
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.getInstance
import com.socialite.solite_pos.data.source.repository.impl.SettingRepositoryImpl
import com.socialite.solite_pos.data.source.preference.OrderPref

object DomainInjection {
    fun provideGetProductVariantOptions(context: Context): GetProductVariantOptions {
        val database = getInstance(context)
        return GetProductVariantOptionsImpl(database.productVariantsDao())
    }

    fun provideNewOrder(context: Context): NewOrder {
        val database = getInstance(context)
        return NewOrderImpl(
            dao = database.ordersDao(),
//            productDao = database.productsDao(),
            soliteDao = database.soliteDao(),
            orderPref = OrderPref(context),
            settingRepository = SettingRepositoryImpl.getDataStoreInstance(context)
        )
    }

    fun provideGetProductOrder(context: Context): GetProductOrder {
        val database = getInstance(context)
        return GetProductOrderImpl(
            dao = database.ordersDao(),
            productsDao = database.productsDao()
        )
    }

    fun provideGetIncomesRecapData(context: Context): GetRecapData {
        return GetRecapDataImpl(
            RepositoryInjection.provideOrdersRepository(context),
            RepositoryInjection.provideOutcomesRepository(context),
            provideGetProductOrder(context)
        )
    }

    fun providePayOrder(context: Context): PayOrder {
        return PayOrderImpl(
            RepositoryInjection.provideOrdersRepository(context)
        )
    }

    fun provideGetOrdersGeneralMenuBadge(context: Context): GetOrdersGeneralMenuBadge {
        return GetOrdersGeneralMenuBadgeImpl(
            RepositoryInjection.provideOrdersRepository(context)
        )
    }

    fun provideNewOutcome(context: Context): NewOutcome {
        return NewOutcomeImpl(
            RepositoryInjection.provideSettingRepository(context),
            RepositoryInjection.provideOutcomesRepository(context)
        )
    }

    fun provideUpdateOrderProducts(context: Context): UpdateOrderProducts {
        val database = getInstance(context)
        return UpdateOrderProductsImpl(
            database.ordersDao(),
            database.soliteDao()
        )
    }

    fun provideMigrateToUUID(context: Context): MigrateToUUID {
        val customerRepository = RepositoryInjection.provideCustomersRepository(context)
        val storeRepository = RepositoryInjection.provideStoreRepository(context)
        val categoriesRepository = RepositoryInjection.provideCategoriesRepository(context)
        return MigrateToUUIDImpl(
            customersRepository = customerRepository,
            storeRepository = storeRepository,
            categoriesRepository = categoriesRepository,
        )
    }
}
