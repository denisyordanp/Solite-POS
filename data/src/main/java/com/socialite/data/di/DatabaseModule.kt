package com.socialite.data.di

import android.content.Context
import com.socialite.data.database.AppDatabase
import com.socialite.data.database.dao.CategoriesDao
import com.socialite.data.database.dao.CustomersDao
import com.socialite.data.database.dao.OrderDetailsDao
import com.socialite.data.database.dao.OrderPaymentsDao
import com.socialite.data.database.dao.OrderProductVariantsDao
import com.socialite.data.database.dao.OrderPromosDao
import com.socialite.data.database.dao.OrdersDao
import com.socialite.data.database.dao.OutcomesDao
import com.socialite.data.database.dao.PaymentsDao
import com.socialite.data.database.dao.ProductVariantsDao
import com.socialite.data.database.dao.ProductsDao
import com.socialite.data.database.dao.PromosDao
import com.socialite.data.database.dao.StoreDao
import com.socialite.data.database.dao.UserDao
import com.socialite.data.database.dao.VariantOptionsDao
import com.socialite.data.database.dao.VariantsDao
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

    @Provides
    fun userDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()
}
