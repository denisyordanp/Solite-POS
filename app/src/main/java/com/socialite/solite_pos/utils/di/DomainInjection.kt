package com.socialite.solite_pos.utils.di

import android.content.Context
import com.socialite.solite_pos.data.source.domain.GetIncomesRecapData
import com.socialite.solite_pos.data.source.domain.GetProductOrder
import com.socialite.solite_pos.data.source.domain.GetProductVariantOptions
import com.socialite.solite_pos.data.source.domain.NewOrder
import com.socialite.solite_pos.data.source.domain.PayOrder
import com.socialite.solite_pos.data.source.domain.impl.GetIncomesRecapDataImpl
import com.socialite.solite_pos.data.source.domain.impl.GetProductOrderImpl
import com.socialite.solite_pos.data.source.domain.impl.GetProductVariantOptionsImpl
import com.socialite.solite_pos.data.source.domain.impl.NewOrderImpl
import com.socialite.solite_pos.data.source.domain.impl.PayOrderImpl
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.getInstance
import com.socialite.solite_pos.utils.preference.OrderPref

object DomainInjection {
    fun provideGetProductVariantOptions(context: Context): GetProductVariantOptions {
        val database = getInstance(context)
        return GetProductVariantOptionsImpl(database.productVariantsDao())
    }

    fun provideNewOrder(context: Context): NewOrder {
        val database = getInstance(context)
        return NewOrderImpl(
            dao = database.ordersDao(),
            productDao = database.productsDao(),
            soliteDao = database.soliteDao(),
            orderPref = OrderPref(context)
        )
    }

    fun provideGetProductOrder(context: Context): GetProductOrder {
        val database = getInstance(context)
        return GetProductOrderImpl(
            dao = database.ordersDao(),
            productsDao = database.productsDao()
        )
    }

    fun provideGetIncomesRecapData(context: Context): GetIncomesRecapData {
        return GetIncomesRecapDataImpl(
            RepositoryInjection.provideOrdersRepository(context),
            provideGetProductOrder(context)
        )
    }

    fun providePayOrder(context: Context): PayOrder {
        return PayOrderImpl(
            RepositoryInjection.provideOrdersRepository(context)
        )
    }
}
