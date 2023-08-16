package com.socialite.data.di

import com.socialite.data.preference.UserPreferences
import com.socialite.data.preference.impl.UserPreferencesImpl
import com.socialite.data.repository.AccountRepository
import com.socialite.data.repository.CategoriesRepository
import com.socialite.data.repository.CustomersRepository
import com.socialite.data.repository.OrderDetailsRepository
import com.socialite.data.repository.OrderPaymentsRepository
import com.socialite.data.repository.OrderProductVariantsRepository
import com.socialite.data.repository.OrderPromosRepository
import com.socialite.data.repository.OrdersRepository
import com.socialite.data.repository.OutcomesRepository
import com.socialite.data.repository.PaymentsRepository
import com.socialite.data.repository.ProductVariantsRepository
import com.socialite.data.repository.ProductsRepository
import com.socialite.data.repository.PromosRepository
import com.socialite.data.repository.RemoteConfigRepository
import com.socialite.data.repository.SettingRepository
import com.socialite.data.repository.StoreRepository
import com.socialite.data.repository.VariantOptionsRepository
import com.socialite.data.repository.VariantsRepository
import com.socialite.data.repository.impl.AccountRepositoryImpl
import com.socialite.data.repository.impl.CategoriesRepositoryImpl
import com.socialite.data.repository.impl.CustomersRepositoryImpl
import com.socialite.data.repository.impl.OrderDetailsRepositoryImpl
import com.socialite.data.repository.impl.OrderPaymentsRepositoryImpl
import com.socialite.data.repository.impl.OrderProductVariantsRepositoryImpl
import com.socialite.data.repository.impl.OrderPromosRepositoryImpl
import com.socialite.data.repository.impl.OrdersRepositoryImpl
import com.socialite.data.repository.impl.OutcomesRepositoryImpl
import com.socialite.data.repository.impl.PaymentsRepositoryImpl
import com.socialite.data.repository.impl.ProductVariantsRepositoryImpl
import com.socialite.data.repository.impl.ProductsRepositoryImpl
import com.socialite.data.repository.impl.PromosRepositoryImpl
import com.socialite.data.repository.impl.RemoteConfigRepositoryImpl
import com.socialite.data.repository.impl.SettingRepositoryImpl
import com.socialite.data.repository.impl.StoreRepositoryImpl
import com.socialite.data.repository.impl.VariantOptionsRepositoryImpl
import com.socialite.data.repository.impl.VariantsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindPaymentsRepository(
        paymentsRepositoryImpl: PaymentsRepositoryImpl
    ): PaymentsRepository

    @Binds
    abstract fun bindCustomersRepository(
        customersRepositoryImpl: CustomersRepositoryImpl
    ): CustomersRepository

    @Binds
    abstract fun bindVariantsRepository(
        variantsRepositoryImpl: VariantsRepositoryImpl
    ): VariantsRepository

    @Binds
    abstract fun bindVariantOptionsRepository(
        variantOptionsRepositoryImpl: VariantOptionsRepositoryImpl
    ): VariantOptionsRepository

    @Binds
    abstract fun bindOrderProductVariantsRepository(
        orderProductVariantsRepositoryImpl: OrderProductVariantsRepositoryImpl
    ): OrderProductVariantsRepository

    @Binds
    abstract fun bindCategoriesRepository(
        categoriesRepositoryImpl: CategoriesRepositoryImpl
    ): CategoriesRepository

    @Binds
    abstract fun bindOutcomesRepository(
        outcomesRepositoryImpl: OutcomesRepositoryImpl
    ): OutcomesRepository

    @Binds
    abstract fun bindProductsRepository(
        productsRepositoryImpl: ProductsRepositoryImpl
    ): ProductsRepository

    @Binds
    abstract fun bindProductVariantsRepository(
        productVariantsRepositoryImpl: ProductVariantsRepositoryImpl
    ): ProductVariantsRepository

    @Binds
    abstract fun bindOrdersRepository(
        ordersRepositoryImpl: OrdersRepositoryImpl
    ): OrdersRepository

    @Binds
    abstract fun bindOrderDetailsRepository(
        orderDetailsRepositoryImpl: OrderDetailsRepositoryImpl
    ): OrderDetailsRepository

    @Binds
    abstract fun bindOrderPaymentsRepository(
        orderPaymentsRepositoryImpl: OrderPaymentsRepositoryImpl
    ): OrderPaymentsRepository

    @Binds
    abstract fun bindOrderPromosRepository(
        orderPromosRepositoryImpl: OrderPromosRepositoryImpl
    ): OrderPromosRepository

    @Binds
    abstract fun bindStoreRepository(
        storeRepositoryImpl: StoreRepositoryImpl
    ): StoreRepository

    @Binds
    abstract fun bindSettingRepository(
        settingRepositoryImpl: SettingRepositoryImpl
    ): SettingRepository

    @Binds
    abstract fun bindPromosRepository(
        promosRepositoryImpl: PromosRepositoryImpl
    ): PromosRepository

    @Binds
    abstract fun bindRemoteConfigRepository(
        remoteConfigRepositoryImpl: RemoteConfigRepositoryImpl
    ): RemoteConfigRepository

    @Binds
    abstract fun bindUserPreferences(
        userPreferencesImpl: UserPreferencesImpl
    ): UserPreferences

    @Binds
    abstract fun bindAccountRepository(
        accountRepository: AccountRepositoryImpl
    ): AccountRepository
}
