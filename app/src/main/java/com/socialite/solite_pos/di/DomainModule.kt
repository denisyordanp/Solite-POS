package com.socialite.solite_pos.di

import com.socialite.solite_pos.data.source.domain.GetCategoryProductVariantCount
import com.socialite.solite_pos.data.source.domain.GetOrderMenusWithAmount
import com.socialite.solite_pos.data.source.domain.GetOrderWithProduct
import com.socialite.solite_pos.data.source.domain.GetOrdersGeneralMenuBadge
import com.socialite.solite_pos.data.source.domain.GetOrdersMenuWithOrders
import com.socialite.solite_pos.data.source.domain.GetProductVariantOptions
import com.socialite.solite_pos.data.source.domain.GetProductWithCategories
import com.socialite.solite_pos.data.source.domain.GetProductWithVariantOptions
import com.socialite.solite_pos.data.source.domain.GetRecapData
import com.socialite.solite_pos.data.source.domain.GetVariantsWithOptions
import com.socialite.solite_pos.data.source.domain.LoginUser
import com.socialite.solite_pos.data.source.domain.MigrateToUUID
import com.socialite.solite_pos.data.source.domain.NewOrder
import com.socialite.solite_pos.data.source.domain.NewOutcome
import com.socialite.solite_pos.data.source.domain.PayOrder
import com.socialite.solite_pos.data.source.domain.RegisterUser
import com.socialite.solite_pos.data.source.domain.Synchronize
import com.socialite.solite_pos.data.source.domain.UpdateOrderProducts
import com.socialite.solite_pos.data.source.domain.impl.GetCategoryProductVariantCountImpl
import com.socialite.solite_pos.data.source.domain.impl.GetOrderMenusWithAmountImpl
import com.socialite.solite_pos.data.source.domain.impl.GetOrderWithProductImpl
import com.socialite.solite_pos.data.source.domain.impl.GetOrdersGeneralMenuBadgeImpl
import com.socialite.solite_pos.data.source.domain.impl.GetOrdersMenuWithOrdersImpl
import com.socialite.solite_pos.data.source.domain.impl.GetProductVariantOptionsImpl
import com.socialite.solite_pos.data.source.domain.impl.GetProductWithCategoriesImpl
import com.socialite.solite_pos.data.source.domain.impl.GetProductWithVariantOptionsImpl
import com.socialite.solite_pos.data.source.domain.impl.GetRecapDataImpl
import com.socialite.solite_pos.data.source.domain.impl.GetVariantsWithOptionsImpl
import com.socialite.solite_pos.data.source.domain.impl.LoginUserImpl
import com.socialite.solite_pos.data.source.domain.impl.MigrateToUUIDImpl
import com.socialite.solite_pos.data.source.domain.impl.NewOrderImpl
import com.socialite.solite_pos.data.source.domain.impl.NewOutcomeImpl
import com.socialite.solite_pos.data.source.domain.impl.PayOrderImpl
import com.socialite.solite_pos.data.source.domain.impl.RegisterUserImpl
import com.socialite.solite_pos.data.source.domain.impl.SynchronizeImpl
import com.socialite.solite_pos.data.source.domain.impl.UpdateOrderProductsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindGetOrdersGeneralMenuBadge(
        getOrdersGeneralMenuBadgeImpl: GetOrdersGeneralMenuBadgeImpl
    ): GetOrdersGeneralMenuBadge

    @Binds
    abstract fun bindMigrateToUUID(
        migrateToUUIDImpl: MigrateToUUIDImpl
    ): MigrateToUUID

    @Binds
    abstract fun bindSynchronize(
        synchronizeImpl: SynchronizeImpl
    ): Synchronize

    @Binds
    abstract fun bindNewOrder(
        newOrderImpl: NewOrderImpl
    ): NewOrder

    @Binds
    abstract fun bindGetProductWithCategories(
        getProductWithCategoriesImpl: GetProductWithCategoriesImpl
    ): GetProductWithCategories

    @Binds
    abstract fun bindGetProductWithVariantOptions(
        getProductWithVariantOptions: GetProductWithVariantOptionsImpl
    ): GetProductWithVariantOptions

    @Binds
    abstract fun bindUpdateOrderProducts(
        updateOrderProducts: UpdateOrderProductsImpl
    ): UpdateOrderProducts

    @Binds
    abstract fun bindGetOrderWithProduct(
        getOrderWithProduct: GetOrderWithProductImpl
    ): GetOrderWithProduct

    @Binds
    abstract fun bindPayOrder(
        payOrder: PayOrderImpl
    ): PayOrder

    @Binds
    abstract fun bindGetOrdersMenuWithOrders(
        getOrdersMenuWithOrders: GetOrdersMenuWithOrdersImpl
    ): GetOrdersMenuWithOrders

    @Binds
    abstract fun bindNewOutcome(
        newOutcome: NewOutcomeImpl
    ): NewOutcome

    @Binds
    abstract fun bindGetProductVariantOptions(
        getProductVariantOptions: GetProductVariantOptionsImpl
    ): GetProductVariantOptions

    @Binds
    abstract fun bindGetCategoryProductVariantCount(
        getCategoryProductVariantCount: GetCategoryProductVariantCountImpl
    ): GetCategoryProductVariantCount

    @Binds
    abstract fun bindGetRecapData(
        getRecapData: GetRecapDataImpl
    ): GetRecapData

    @Binds
    abstract fun bindGetOrderMenusWithAmount(
        getOrderMenusWithAmount: GetOrderMenusWithAmountImpl
    ): GetOrderMenusWithAmount

    @Binds
    abstract fun bindGetVariantsWithOptions(
        getVariantsWithOptions: GetVariantsWithOptionsImpl
    ): GetVariantsWithOptions

    @Binds
    abstract fun bindLoginUser(
        loginUser: LoginUserImpl
    ): LoginUser

    @Binds
    abstract fun bindRegisterUser(
        registerUser: RegisterUserImpl
    ): RegisterUser
}
