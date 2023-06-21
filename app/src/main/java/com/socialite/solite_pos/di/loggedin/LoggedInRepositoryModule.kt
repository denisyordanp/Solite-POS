package com.socialite.solite_pos.di.loggedin

import com.socialite.solite_pos.data.source.preference.UserPreferences
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
import com.socialite.solite_pos.data.source.repository.impl.VariantOptionsRepositoryImpl
import com.socialite.solite_pos.data.source.repository.impl.VariantsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LoggedInRepositoryModule {

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
    abstract fun provideUserPreferences(
        userPreferencesImpl: UserPreferencesImpl
    ): UserPreferences
}
