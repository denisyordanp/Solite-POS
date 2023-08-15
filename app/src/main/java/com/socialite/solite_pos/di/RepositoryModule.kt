package com.socialite.solite_pos.di

import com.socialite.solite_pos.data.source.preference.UserPreferences
import com.socialite.solite_pos.data.source.preference.impl.UserPreferencesImpl
import com.socialite.solite_pos.data.repository.AccountRepository
import com.socialite.solite_pos.data.repository.CategoriesRepository
import com.socialite.solite_pos.data.repository.CustomersRepository
import com.socialite.solite_pos.data.repository.OrderDetailsRepository
import com.socialite.solite_pos.data.repository.OrderPaymentsRepository
import com.socialite.solite_pos.data.repository.OrderProductVariantsRepository
import com.socialite.solite_pos.data.repository.OrderPromosRepository
import com.socialite.solite_pos.data.repository.OrdersRepository
import com.socialite.solite_pos.data.repository.OutcomesRepository
import com.socialite.solite_pos.data.repository.PaymentsRepository
import com.socialite.solite_pos.data.repository.ProductVariantsRepository
import com.socialite.solite_pos.data.repository.ProductsRepository
import com.socialite.solite_pos.data.repository.PromosRepository
import com.socialite.solite_pos.data.repository.RemoteConfigRepository
import com.socialite.solite_pos.data.repository.SettingRepository
import com.socialite.solite_pos.data.repository.StoreRepository
import com.socialite.solite_pos.data.repository.VariantOptionsRepository
import com.socialite.solite_pos.data.repository.VariantsRepository
import com.socialite.solite_pos.data.repository.impl.AccountRepositoryImpl
import com.socialite.solite_pos.data.repository.impl.CategoriesRepositoryImpl
import com.socialite.solite_pos.data.repository.impl.CustomersRepositoryImpl
import com.socialite.solite_pos.data.repository.impl.OrderDetailsRepositoryImpl
import com.socialite.solite_pos.data.repository.impl.OrderPaymentsRepositoryImpl
import com.socialite.solite_pos.data.repository.impl.OrderProductVariantsRepositoryImpl
import com.socialite.solite_pos.data.repository.impl.OrderPromosRepositoryImpl
import com.socialite.solite_pos.data.repository.impl.OrdersRepositoryImpl
import com.socialite.solite_pos.data.repository.impl.OutcomesRepositoryImpl
import com.socialite.solite_pos.data.repository.impl.PaymentsRepositoryImpl
import com.socialite.solite_pos.data.repository.impl.ProductVariantsRepositoryImpl
import com.socialite.solite_pos.data.repository.impl.ProductsRepositoryImpl
import com.socialite.solite_pos.data.repository.impl.PromosRepositoryImpl
import com.socialite.solite_pos.data.repository.impl.RemoteConfigRepositoryImpl
import com.socialite.solite_pos.data.repository.impl.SettingRepositoryImpl
import com.socialite.solite_pos.data.repository.impl.StoreRepositoryImpl
import com.socialite.solite_pos.data.repository.impl.VariantOptionsRepositoryImpl
import com.socialite.solite_pos.data.repository.impl.VariantsRepositoryImpl
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
