package com.socialite.solite_pos.di.loggedin

import android.content.Context
import com.socialite.solite_pos.data.source.domain.GetCategoryProductVariantCount
import com.socialite.solite_pos.data.source.domain.GetOrdersGeneralMenuBadge
import com.socialite.solite_pos.data.source.domain.GetProductOrder
import com.socialite.solite_pos.data.source.domain.GetProductWithCategories
import com.socialite.solite_pos.data.source.domain.GetProductWithVariantOptions
import com.socialite.solite_pos.data.source.domain.GetRecapData
import com.socialite.solite_pos.data.source.domain.GetVariantOptions
import com.socialite.solite_pos.data.source.domain.MigrateToUUID
import com.socialite.solite_pos.data.source.domain.NewOrder
import com.socialite.solite_pos.data.source.domain.NewOutcome
import com.socialite.solite_pos.data.source.domain.PayOrder
import com.socialite.solite_pos.data.source.domain.Synchronize
import com.socialite.solite_pos.data.source.domain.UpdateOrderProducts
import com.socialite.solite_pos.data.source.domain.impl.GetCategoryProductVariantCountImpl
import com.socialite.solite_pos.data.source.domain.impl.GetOrdersGeneralMenuBadgeImpl
import com.socialite.solite_pos.data.source.domain.impl.GetProductOrderImpl
import com.socialite.solite_pos.data.source.domain.impl.GetProductWithCategoriesImpl
import com.socialite.solite_pos.data.source.domain.impl.GetProductWithVariantOptionsImpl
import com.socialite.solite_pos.data.source.domain.impl.GetRecapDataImpl
import com.socialite.solite_pos.data.source.domain.impl.GetVariantOptionsImpl
import com.socialite.solite_pos.data.source.domain.impl.MigrateToUUIDImpl
import com.socialite.solite_pos.data.source.domain.impl.NewOrderImpl
import com.socialite.solite_pos.data.source.domain.impl.NewOutcomeImpl
import com.socialite.solite_pos.data.source.domain.impl.PayOrderImpl
import com.socialite.solite_pos.data.source.domain.impl.SynchronizeImpl
import com.socialite.solite_pos.data.source.domain.impl.UpdateOrderProductsImpl
import com.socialite.solite_pos.data.source.preference.OrderPref
import com.socialite.solite_pos.data.source.preference.impl.UserPreferencesImpl
import com.socialite.solite_pos.data.source.repository.impl.SettingRepositoryImpl

object LoggedInDomainInjection {
    fun provideGetVariantOptions(context: Context): GetVariantOptions {
        val productVariantRepository =
            LoggedInRepositoryInjection.provideProductVariantsRepository(context)
        return GetVariantOptionsImpl(productVariantRepository)
    }

    fun provideNewOrder(context: Context): NewOrder {
        val userPreferences = UserPreferencesImpl.getInstance(context)
        return NewOrderImpl(
            orderPref = OrderPref(context),
            ordersRepository = LoggedInRepositoryInjection.provideOrdersRepository(context),
            orderDetailsRepository = LoggedInRepositoryInjection.provideOrderDetailsRepository(
                context
            ),
            orderProductVariantsRepository = LoggedInRepositoryInjection.provideOrderProductVariantsRepository(
                context
            ),
            settingRepository = SettingRepositoryImpl.getDataStoreInstance(context, userPreferences)
        )
    }

    fun provideGetProductOrder(context: Context): GetProductOrder {
        return GetProductOrderImpl(
            orderDetailRepository = LoggedInRepositoryInjection.provideOrderDetailsRepository(
                context
            ),
            productsRepository = LoggedInRepositoryInjection.provideProductsRepository(context)
        )
    }

    fun provideGetIncomesRecapData(context: Context): GetRecapData {
        return GetRecapDataImpl(
            LoggedInRepositoryInjection.provideOrdersRepository(context),
            LoggedInRepositoryInjection.provideOutcomesRepository(context),
            provideGetProductOrder(context)
        )
    }

    fun providePayOrder(context: Context): PayOrder {
        return PayOrderImpl(
            ordersRepository = LoggedInRepositoryInjection.provideOrdersRepository(context),
            orderPaymentsRepository = LoggedInRepositoryInjection.provideOrderPaymentsRepository(
                context
            ),
            orderPromosRepository = LoggedInRepositoryInjection.provideOrderPromosRepository(context)
        )
    }

    fun provideGetOrdersGeneralMenuBadge(context: Context): GetOrdersGeneralMenuBadge {
        return GetOrdersGeneralMenuBadgeImpl(
            LoggedInRepositoryInjection.provideOrdersRepository(context)
        )
    }

    fun provideNewOutcome(context: Context): NewOutcome {
        return NewOutcomeImpl(
            LoggedInRepositoryInjection.provideSettingRepository(context),
            LoggedInRepositoryInjection.provideOutcomesRepository(context)
        )
    }

    fun provideUpdateOrderProducts(context: Context): UpdateOrderProducts {
        return UpdateOrderProductsImpl(
            orderDetailsRepository = LoggedInRepositoryInjection.provideOrderDetailsRepository(
                context
            ),
            orderProductVariantsRepository = LoggedInRepositoryInjection.provideOrderProductVariantsRepository(
                context
            )
        )
    }


    fun provideMigrateToUUID(context: Context): MigrateToUUID {
        val customerRepository = LoggedInRepositoryInjection.provideCustomersRepository(context)
        val storeRepository = LoggedInRepositoryInjection.provideStoreRepository(context)
        val categoriesRepository = LoggedInRepositoryInjection.provideCategoriesRepository(context)
        val promosRepository = LoggedInRepositoryInjection.providePromosRepository(context)
        val paymentsRepository = LoggedInRepositoryInjection.providePaymentsRepository(context)
        val ordersRepository = LoggedInRepositoryInjection.provideOrdersRepository(context)
        val outcomesRepository = LoggedInRepositoryInjection.provideOutcomesRepository(context)
        val productsRepository = LoggedInRepositoryInjection.provideProductsRepository(context)
        val variantsRepository = LoggedInRepositoryInjection.provideVariantsRepository(context)
        val variantOptionsRepository =
            LoggedInRepositoryInjection.provideVariantOptionsRepository(context)
        val productVariantsRepository =
            LoggedInRepositoryInjection.provideProductVariantsRepository(context)
        val settingRepository = LoggedInRepositoryInjection.provideSettingRepository(context)
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

    fun provideSynchronize(context: Context): Synchronize {
        val customerRepository = LoggedInRepositoryInjection.provideCustomersRepository(context)
        val storeRepository = LoggedInRepositoryInjection.provideStoreRepository(context)
        val categoriesRepository = LoggedInRepositoryInjection.provideCategoriesRepository(context)
        val promosRepository = LoggedInRepositoryInjection.providePromosRepository(context)
        val paymentsRepository = LoggedInRepositoryInjection.providePaymentsRepository(context)
        val ordersRepository = LoggedInRepositoryInjection.provideOrdersRepository(context)
        val outcomesRepository = LoggedInRepositoryInjection.provideOutcomesRepository(context)
        val productsRepository = LoggedInRepositoryInjection.provideProductsRepository(context)
        val variantsRepository = LoggedInRepositoryInjection.provideVariantsRepository(context)
        val orderDetailsRepository =
            LoggedInRepositoryInjection.provideOrderDetailsRepository(context)
        val orderPaymentsRepository =
            LoggedInRepositoryInjection.provideOrderPaymentsRepository(context)
        val orderPromosRepository =
            LoggedInRepositoryInjection.provideOrderPromosRepository(context)
        val variantOptionsRepository =
            LoggedInRepositoryInjection.provideVariantOptionsRepository(context)
        val orderProductVariantsRepository =
            LoggedInRepositoryInjection.provideOrderProductVariantsRepository(context)
        val productVariantsRepository =
            LoggedInRepositoryInjection.provideProductVariantsRepository(context)
        val userPreferences = UserPreferencesImpl.getInstance(context)
        val service = LoggedNetworkInInjector.provideSoliteServices(userPreferences)

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

    fun provideGetProductWithCategories(context: Context): GetProductWithCategories {
        val productsRepository = LoggedInRepositoryInjection.provideProductsRepository(context)
        val productVariantsRepository =
            LoggedInRepositoryInjection.provideProductVariantsRepository(context)
        return GetProductWithCategoriesImpl(
            productsRepository = productsRepository,
            productVariantsRepository = productVariantsRepository
        )
    }

    fun provideGetProductWithVariantOptions(context: Context): GetProductWithVariantOptions {
        val productVariantRepository =
            LoggedInRepositoryInjection.provideProductVariantsRepository(context)
        val productsRepository = LoggedInRepositoryInjection.provideProductsRepository(context)
        return GetProductWithVariantOptionsImpl(
            productVariantsRepository = productVariantRepository,
            productsRepository = productsRepository
        )
    }

    fun provideGetCategoryProductItemViewData(context: Context): GetCategoryProductVariantCount {
        val productVariantRepository =
            LoggedInRepositoryInjection.provideProductVariantsRepository(context)
        val productsRepository = LoggedInRepositoryInjection.provideProductsRepository(context)
        return GetCategoryProductVariantCountImpl(
            productVariantsRepository = productVariantRepository,
            productsRepository = productsRepository
        )
    }
}
