package com.socialite.solite_pos.di

import com.socialite.solite_pos.data.domain.GetCategoryProductVariantCount
import com.socialite.solite_pos.data.domain.GetOrderMenusWithAmount
import com.socialite.solite_pos.data.domain.GetOrderWithProduct
import com.socialite.solite_pos.data.domain.GetOrdersGeneralMenuBadge
import com.socialite.solite_pos.data.domain.GetOrdersMenuWithOrders
import com.socialite.solite_pos.data.domain.GetProductVariantOptions
import com.socialite.solite_pos.data.domain.GetProductWithCategories
import com.socialite.solite_pos.data.domain.GetProductWithVariantOptions
import com.socialite.solite_pos.data.domain.GetRecapData
import com.socialite.solite_pos.data.domain.GetVariantsWithOptions
import com.socialite.solite_pos.data.domain.LoginUser
import com.socialite.solite_pos.data.domain.MigrateToUUID
import com.socialite.solite_pos.data.domain.NewOrder
import com.socialite.solite_pos.data.domain.NewOutcome
import com.socialite.solite_pos.data.domain.PayOrder
import com.socialite.solite_pos.data.domain.RegisterUser
import com.socialite.solite_pos.data.domain.Synchronize
import com.socialite.solite_pos.data.domain.UpdateOrderProducts
import com.socialite.solite_pos.data.domain.impl.GetCategoryProductVariantCountImpl
import com.socialite.solite_pos.data.domain.impl.GetOrderMenusWithAmountImpl
import com.socialite.solite_pos.data.domain.impl.GetOrderWithProductImpl
import com.socialite.solite_pos.data.domain.impl.GetOrdersGeneralMenuBadgeImpl
import com.socialite.solite_pos.data.domain.impl.GetOrdersMenuWithOrdersImpl
import com.socialite.solite_pos.data.domain.impl.GetProductVariantOptionsImpl
import com.socialite.solite_pos.data.domain.impl.GetProductWithCategoriesImpl
import com.socialite.solite_pos.data.domain.impl.GetProductWithVariantOptionsImpl
import com.socialite.solite_pos.data.domain.impl.GetRecapDataImpl
import com.socialite.solite_pos.data.domain.impl.GetVariantsWithOptionsImpl
import com.socialite.solite_pos.data.domain.impl.LoginUserImpl
import com.socialite.solite_pos.data.domain.impl.MigrateToUUIDImpl
import com.socialite.solite_pos.data.domain.impl.NewOrderImpl
import com.socialite.solite_pos.data.domain.impl.NewOutcomeImpl
import com.socialite.solite_pos.data.domain.impl.PayOrderImpl
import com.socialite.solite_pos.data.domain.impl.RegisterUserImpl
import com.socialite.solite_pos.data.domain.impl.SynchronizeImpl
import com.socialite.solite_pos.data.domain.impl.UpdateOrderProductsImpl
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
