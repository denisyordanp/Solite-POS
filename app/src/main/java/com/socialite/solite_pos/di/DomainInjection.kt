package com.socialite.solite_pos.di

import android.content.Context
import com.socialite.solite_pos.data.source.domain.GetOrdersGeneralMenuBadge
import com.socialite.solite_pos.data.source.domain.GetProductOrder
import com.socialite.solite_pos.data.source.domain.GetProductVariantOptions
import com.socialite.solite_pos.data.source.domain.GetRecapData
import com.socialite.solite_pos.data.source.domain.MigrateToUUID
import com.socialite.solite_pos.data.source.domain.LoginUser
import com.socialite.solite_pos.data.source.domain.NewOrder
import com.socialite.solite_pos.data.source.domain.NewOutcome
import com.socialite.solite_pos.data.source.domain.PayOrder
import com.socialite.solite_pos.data.source.domain.RegisterUser
import com.socialite.solite_pos.data.source.domain.UpdateOrderProducts
import com.socialite.solite_pos.data.source.domain.impl.GetOrdersGeneralMenuBadgeImpl
import com.socialite.solite_pos.data.source.domain.impl.GetProductOrderImpl
import com.socialite.solite_pos.data.source.domain.impl.GetProductVariantOptionsImpl
import com.socialite.solite_pos.data.source.domain.impl.GetRecapDataImpl
import com.socialite.solite_pos.data.source.domain.impl.MigrateToUUIDImpl
import com.socialite.solite_pos.data.source.domain.impl.LoginUserImpl
import com.socialite.solite_pos.data.source.domain.impl.NewOrderImpl
import com.socialite.solite_pos.data.source.domain.impl.NewOutcomeImpl
import com.socialite.solite_pos.data.source.domain.impl.PayOrderImpl
import com.socialite.solite_pos.data.source.domain.impl.RegisterUserImpl
import com.socialite.solite_pos.data.source.domain.impl.UpdateOrderProductsImpl
import com.socialite.solite_pos.data.source.local.room.AppDatabase.Companion.getInstance
import com.socialite.solite_pos.data.source.repository.impl.SettingRepositoryImpl
import com.socialite.solite_pos.data.source.preference.OrderPref
import com.socialite.solite_pos.data.source.preference.impl.UserPreferencesImpl
import com.socialite.solite_pos.data.source.domain.Synchronize
import com.socialite.solite_pos.data.source.domain.impl.SynchronizeImpl

object DomainInjection {
    fun provideGetProductVariantOptions(context: Context): GetProductVariantOptions {
        val database = getInstance(context)
        return GetProductVariantOptionsImpl(database.productVariantsDao())
    }

    fun provideNewOrder(context: Context): NewOrder {
        return NewOrderImpl(
            orderPref = OrderPref(context),
            ordersRepository = RepositoryInjection.provideOrdersRepository(context),
            orderDetailsRepository = RepositoryInjection.provideOrderDetailsRepository(context),
            settingRepository = SettingRepositoryImpl.getDataStoreInstance(context)
        )
    }

    fun provideGetProductOrder(context: Context): GetProductOrder {
        return GetProductOrderImpl(
            orderDetailRepository = RepositoryInjection.provideOrderDetailsRepository(context),
            productsRepository = RepositoryInjection.provideProductsRepository(context)
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
            ordersRepository = RepositoryInjection.provideOrdersRepository(context),
            orderPaymentsRepository = RepositoryInjection.provideOrderPaymentsRepository(context),
            orderPromosRepository = RepositoryInjection.provideOrderPromosRepository(context)
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
        val ordersRepository = RepositoryInjection.provideOrdersRepository(context)
        return UpdateOrderProductsImpl(ordersRepository)
    }

    fun provideMigrateToUUID(context: Context): MigrateToUUID {
        val customerRepository = RepositoryInjection.provideCustomersRepository(context)
        val storeRepository = RepositoryInjection.provideStoreRepository(context)
        val categoriesRepository = RepositoryInjection.provideCategoriesRepository(context)
        val promosRepository = RepositoryInjection.providePromosRepository(context)
        val paymentsRepository = RepositoryInjection.providePaymentsRepository(context)
        val ordersRepository = RepositoryInjection.provideOrdersRepository(context)
        val outcomesRepository = RepositoryInjection.provideOutcomesRepository(context)
        val productsRepository = RepositoryInjection.provideProductsRepository(context)
        val variantsRepository = RepositoryInjection.provideVariantsRepository(context)
        val variantOptionsRepository = RepositoryInjection.provideVariantOptionsRepository(context)
        val productVariantsRepository = RepositoryInjection.provideProductVariantsRepository(context)
        val settingRepository = RepositoryInjection.provideSettingRepository(context)
        return MigrateToUUIDImpl(
            customersRepository = customerRepository,
            storeRepository = storeRepository,
            categoriesRepository = categoriesRepository,
            promosRepository = promosRepository,
            paymentsRepository = paymentsRepository,
            ordersRepository = ordersRepository,
            outcomesRepository = outcomesRepository,
            productsRepository = productsRepository,
            variantsRepository = variantsRepository,
            variantOptionsRepository = variantOptionsRepository,
            productVariantsRepository = productVariantsRepository,
            settingRepository = settingRepository
        )
    }

    fun provideLoginUser(context: Context): LoginUser {
        val repository = RepositoryInjection.provideUserRepository(context)
        return LoginUserImpl(repository)
    }

    fun provideRegisterUser(context: Context): RegisterUser {
        val repository = RepositoryInjection.provideUserRepository(context)
        return RegisterUserImpl(repository)
    }

    fun provideSynchronize(context: Context): Synchronize {
        val customerRepository = RepositoryInjection.provideCustomersRepository(context)
        val storeRepository = RepositoryInjection.provideStoreRepository(context)
        val categoriesRepository = RepositoryInjection.provideCategoriesRepository(context)
        val promosRepository = RepositoryInjection.providePromosRepository(context)
        val paymentsRepository = RepositoryInjection.providePaymentsRepository(context)
        val ordersRepository = RepositoryInjection.provideOrdersRepository(context)
        val outcomesRepository = RepositoryInjection.provideOutcomesRepository(context)
        val productsRepository = RepositoryInjection.provideProductsRepository(context)
        val variantsRepository = RepositoryInjection.provideVariantsRepository(context)
        val orderDetailsRepository = RepositoryInjection.provideOrderDetailsRepository(context)
        val orderPaymentsRepository = RepositoryInjection.provideOrderPaymentsRepository(context)
        val orderPromosRepository = RepositoryInjection.provideOrderPromosRepository(context)
        val variantOptionsRepository = RepositoryInjection.provideVariantOptionsRepository(context)
        val orderProductVariantsRepository = RepositoryInjection.provideOrderProductVariantsRepository(context)
        val productVariantsRepository = RepositoryInjection.provideProductVariantsRepository(context)
        val userPreferences = UserPreferencesImpl.getInstance(context)
        val service = NetworkLoggedInInjector.provideSoliteServices(userPreferences)

        return SynchronizeImpl(
            customersRepository = customerRepository,
            storeRepository = storeRepository,
            categoriesRepository = categoriesRepository,
            promosRepository = promosRepository,
            paymentsRepository = paymentsRepository,
            ordersRepository = ordersRepository,
            outcomesRepository = outcomesRepository,
            productsRepository = productsRepository,
            variantsRepository = variantsRepository,
            variantOptionsRepository = variantOptionsRepository,
            orderDetailsRepository = orderDetailsRepository,
            orderPaymentsRepository = orderPaymentsRepository,
            orderPromosRepository = orderPromosRepository,
            orderProductVariantsRepository = orderProductVariantsRepository,
            productVariantsRepository = productVariantsRepository,
            service = service
        )
    }
}
