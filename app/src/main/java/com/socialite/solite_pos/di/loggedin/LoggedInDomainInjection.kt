package com.socialite.solite_pos.di.loggedin

import android.content.Context
import com.socialite.solite_pos.data.source.domain.GetCategoryProductVariantCount
import com.socialite.solite_pos.data.source.domain.GetOrderMenusWithAmount
import com.socialite.solite_pos.data.source.domain.GetOrderWithProduct
import com.socialite.solite_pos.data.source.domain.GetOrdersGeneralMenuBadge
import com.socialite.solite_pos.data.source.domain.GetOrdersMenuWithOrders
import com.socialite.solite_pos.data.source.domain.GetProductOrder
import com.socialite.solite_pos.data.source.domain.GetProductVariantOptions
import com.socialite.solite_pos.data.source.domain.GetProductWithCategories
import com.socialite.solite_pos.data.source.domain.GetProductWithVariantOptions
import com.socialite.solite_pos.data.source.domain.GetRecapData
import com.socialite.solite_pos.data.source.domain.GetVariantsWithOptions
import com.socialite.solite_pos.data.source.domain.NewOutcome
import com.socialite.solite_pos.data.source.domain.PayOrder
import com.socialite.solite_pos.data.source.domain.UpdateOrderProducts
import com.socialite.solite_pos.data.source.domain.impl.GetCategoryProductVariantCountImpl
import com.socialite.solite_pos.data.source.domain.impl.GetOrderMenusWithAmountImpl
import com.socialite.solite_pos.data.source.domain.impl.GetOrderWithProductImpl
import com.socialite.solite_pos.data.source.domain.impl.GetOrdersGeneralMenuBadgeImpl
import com.socialite.solite_pos.data.source.domain.impl.GetOrdersMenuWithOrdersImpl
import com.socialite.solite_pos.data.source.domain.impl.GetProductOrderImpl
import com.socialite.solite_pos.data.source.domain.impl.GetProductVariantOptionsImpl
import com.socialite.solite_pos.data.source.domain.impl.GetProductWithCategoriesImpl
import com.socialite.solite_pos.data.source.domain.impl.GetProductWithVariantOptionsImpl
import com.socialite.solite_pos.data.source.domain.impl.GetRecapDataImpl
import com.socialite.solite_pos.data.source.domain.impl.GetVariantsWithOptionsImpl
import com.socialite.solite_pos.data.source.domain.impl.NewOutcomeImpl
import com.socialite.solite_pos.data.source.domain.impl.PayOrderImpl
import com.socialite.solite_pos.data.source.domain.impl.UpdateOrderProductsImpl
import com.socialite.solite_pos.utils.tools.ProductOrderDetailConverter

object LoggedInDomainInjection {
    fun provideGetVariantOptions(context: Context): GetProductVariantOptions {
        val productVariantRepository =
            LoggedInRepositoryInjection.provideProductVariantsRepository(context)
        return GetProductVariantOptionsImpl(productVariantRepository)
    }

    fun provideGetProductOrder(context: Context): GetProductOrder {
        val orderDetailRepository = LoggedInRepositoryInjection.provideOrderDetailsRepository(
            context
        )
        val productsRepository = LoggedInRepositoryInjection.provideProductsRepository(context)
        return GetProductOrderImpl(
            orderDetailRepository = orderDetailRepository,
            converter = ProductOrderDetailConverter(
                orderDetailRepository = orderDetailRepository,
                productsRepository = productsRepository
            )
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

    fun provideGetOrderMenusWithAmount(context: Context): GetOrderMenusWithAmount {
        val ordersRepository = LoggedInRepositoryInjection.provideOrdersRepository(context)
        return GetOrderMenusWithAmountImpl(
            ordersRepository
        )
    }

    fun provideGetVariantsWithOptions(context: Context): GetVariantsWithOptions {
        val variantsRepository = LoggedInRepositoryInjection.provideVariantsRepository(context)
        val variantOptionsRepository =
            LoggedInRepositoryInjection.provideVariantOptionsRepository(context)
        return GetVariantsWithOptionsImpl(
            variantsRepository = variantsRepository,
            variantOptionsRepository = variantOptionsRepository
        )
    }

    fun provideGetOrdersMenuWithOrders(context: Context): GetOrdersMenuWithOrders {
        val ordersRepository = LoggedInRepositoryInjection.provideOrdersRepository(context)
        val orderDetailsRepository =
            LoggedInRepositoryInjection.provideOrderDetailsRepository(context)
        val productsRepository = LoggedInRepositoryInjection.provideProductsRepository(context)
        return GetOrdersMenuWithOrdersImpl(
            orderRepository = ordersRepository,
            orderDetailRepository = orderDetailsRepository,
            converter = ProductOrderDetailConverter(
                orderDetailRepository = orderDetailsRepository,
                productsRepository = productsRepository
            )
        )
    }

    fun provideGetOrderWithProduct(context: Context): GetOrderWithProduct {
        val ordersRepository = LoggedInRepositoryInjection.provideOrdersRepository(context)
        val orderDetailsRepository =
            LoggedInRepositoryInjection.provideOrderDetailsRepository(context)
        val productsRepository = LoggedInRepositoryInjection.provideProductsRepository(context)
        return GetOrderWithProductImpl(
            orderRepository = ordersRepository,
            orderDetailRepository = orderDetailsRepository,
            converter = ProductOrderDetailConverter(
                orderDetailRepository = orderDetailsRepository,
                productsRepository = productsRepository
            )
        )
    }
}
