package com.socialite.solite_pos.di

import android.content.Context
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.CategoriesDao
import com.socialite.solite_pos.data.source.local.room.CustomersDao
import com.socialite.solite_pos.data.source.local.room.OrderDetailsDao
import com.socialite.solite_pos.data.source.local.room.OrderPaymentsDao
import com.socialite.solite_pos.data.source.local.room.OrderProductVariantsDao
import com.socialite.solite_pos.data.source.local.room.OrderPromosDao
import com.socialite.solite_pos.data.source.local.room.OrdersDao
import com.socialite.solite_pos.data.source.local.room.OutcomesDao
import com.socialite.solite_pos.data.source.local.room.PaymentsDao
import com.socialite.solite_pos.data.source.local.room.ProductVariantsDao
import com.socialite.solite_pos.data.source.local.room.ProductsDao
import com.socialite.solite_pos.data.source.local.room.PromosDao
import com.socialite.solite_pos.data.source.local.room.StoreDao
import com.socialite.solite_pos.data.source.local.room.VariantOptionsDao
import com.socialite.solite_pos.data.source.local.room.VariantsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun providePaymentsDao(appDatabase: AppDatabase): PaymentsDao = appDatabase.paymentsDao()

    @Provides
    fun customersDao(appDatabase: AppDatabase): CustomersDao = appDatabase.customersDao()

    @Provides
    fun variantsDao(appDatabase: AppDatabase): VariantsDao = appDatabase.variantsDao()

    @Provides
    fun variantOptionsDao(appDatabase: AppDatabase): VariantOptionsDao =
        appDatabase.variantOptionsDao()

    @Provides
    fun categoriesDao(appDatabase: AppDatabase): CategoriesDao = appDatabase.categoriesDao()

    @Provides
    fun outcomesDao(appDatabase: AppDatabase): OutcomesDao = appDatabase.outcomesDao()

    @Provides
    fun productsDao(appDatabase: AppDatabase): ProductsDao = appDatabase.productsDao()

    @Provides
    fun productVariantsDao(appDatabase: AppDatabase): ProductVariantsDao =
        appDatabase.productVariantsDao()

    @Provides
    fun ordersDao(appDatabase: AppDatabase): OrdersDao = appDatabase.ordersDao()

    @Provides
    fun orderDetailsDao(appDatabase: AppDatabase): OrderDetailsDao = appDatabase.orderDetailsDao()

    @Provides
    fun orderPaymentsDao(appDatabase: AppDatabase): OrderPaymentsDao =
        appDatabase.orderPaymentsDao()

    @Provides
    fun orderPromosDao(appDatabase: AppDatabase): OrderPromosDao = appDatabase.orderPromosDao()

    @Provides
    fun orderProductVariantsDao(appDatabase: AppDatabase): OrderProductVariantsDao =
        appDatabase.orderProductVariantsDao()

    @Provides
    fun storeDao(appDatabase: AppDatabase): StoreDao = appDatabase.storeDao()

    @Provides
    fun promoDao(appDatabase: AppDatabase): PromosDao = appDatabase.promoDao()
}
