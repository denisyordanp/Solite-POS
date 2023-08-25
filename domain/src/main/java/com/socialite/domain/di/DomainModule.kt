package com.socialite.domain.di

import com.socialite.domain.domain.FetchRemoteConfig
import com.socialite.domain.domain.GetCategoryProductVariantCount
import com.socialite.domain.domain.GetCustomers
import com.socialite.domain.domain.GetOrderMenusWithAmount
import com.socialite.domain.domain.GetOrderWithProduct
import com.socialite.domain.domain.GetOrdersGeneralMenuBadge
import com.socialite.domain.domain.GetOrdersMenuWithOrders
import com.socialite.domain.domain.GetProductVariantOptions
import com.socialite.domain.domain.GetProductWithCategories
import com.socialite.domain.domain.GetProductWithVariantOptions
import com.socialite.domain.domain.GetRecapData
import com.socialite.domain.domain.GetToken
import com.socialite.domain.domain.GetVariantsWithOptions
import com.socialite.domain.domain.IsDarkModeActive
import com.socialite.domain.domain.IsServerActive
import com.socialite.domain.domain.IsShouldSelectStore
import com.socialite.domain.domain.LoginUser
import com.socialite.domain.domain.MigrateToUUID
import com.socialite.domain.domain.NewCustomer
import com.socialite.domain.domain.NewOrder
import com.socialite.domain.domain.NewOutcome
import com.socialite.domain.domain.PayOrder
import com.socialite.domain.domain.RegisterUser
import com.socialite.domain.domain.SetDarkMode
import com.socialite.domain.domain.SetNewPrinterAddress
import com.socialite.domain.domain.SetNewToken
import com.socialite.domain.domain.Synchronize
import com.socialite.domain.domain.UpdateOrderProducts
import com.socialite.domain.domain.impl.FetchRemoteConfigImpl
import com.socialite.domain.domain.impl.GetCategoryProductVariantCountImpl
import com.socialite.domain.domain.impl.GetCustomersImpl
import com.socialite.domain.domain.impl.GetOrderMenusWithAmountImpl
import com.socialite.domain.domain.impl.GetOrderWithProductImpl
import com.socialite.domain.domain.impl.GetOrdersGeneralMenuBadgeImpl
import com.socialite.domain.domain.impl.GetOrdersMenuWithOrdersImpl
import com.socialite.domain.domain.impl.GetProductVariantOptionsImpl
import com.socialite.domain.domain.impl.GetProductWithCategoriesImpl
import com.socialite.domain.domain.impl.GetProductWithVariantOptionsImpl
import com.socialite.domain.domain.impl.GetRecapDataImpl
import com.socialite.domain.domain.impl.GetTokenImpl
import com.socialite.domain.domain.impl.GetVariantsWithOptionsImpl
import com.socialite.domain.domain.impl.IsDarkModeActiveImpl
import com.socialite.domain.domain.impl.IsServerActiveImpl
import com.socialite.domain.domain.impl.IsShouldSelectStoreImpl
import com.socialite.domain.domain.impl.LoginUserImpl
import com.socialite.domain.domain.impl.MigrateToUUIDImpl
import com.socialite.domain.domain.impl.NewCustomerImpl
import com.socialite.domain.domain.impl.NewOrderImpl
import com.socialite.domain.domain.impl.NewOutcomeImpl
import com.socialite.domain.domain.impl.PayOrderImpl
import com.socialite.domain.domain.impl.RegisterUserImpl
import com.socialite.domain.domain.impl.SetDarkModeImpl
import com.socialite.domain.domain.impl.SetNewPrinterAddressImpl
import com.socialite.domain.domain.impl.SetNewTokenImpl
import com.socialite.domain.domain.impl.SynchronizeImpl
import com.socialite.domain.domain.impl.UpdateOrderProductsImpl
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

    @Binds
    abstract fun bindIsShouldSelectStore(
        isShouldSelectStoreImpl: IsShouldSelectStoreImpl
    ): IsShouldSelectStore

    @Binds
    abstract fun bindGetToken(
        getTokenImpl: GetTokenImpl
    ): GetToken

    @Binds
    abstract fun bindFetchRemoteConfig(
        fetchRemoteConfigImpl: FetchRemoteConfigImpl
    ): FetchRemoteConfig

    @Binds
    abstract fun bindIsServerActive(
        isServerActiveImpl: IsServerActiveImpl
    ): IsServerActive

    @Binds
    abstract fun bindSetNewPrinterAddress(
        setNewPrinterAddressImpl: SetNewPrinterAddressImpl
    ): SetNewPrinterAddress

    @Binds
    abstract fun bindSetNewToken(
        setNewTokenImpl: SetNewTokenImpl
    ): SetNewToken

    @Binds
    abstract fun bindSetDarkMode(
        setDarkModeImpl: SetDarkModeImpl
    ): SetDarkMode

    @Binds
    abstract fun bindIsDarkModeActive(
        isDarkModeActiveImpl: IsDarkModeActiveImpl
    ): IsDarkModeActive

    @Binds
    abstract fun bindNewCustomer(
        newCustomerImpl: NewCustomerImpl
    ): NewCustomer

    @Binds
    abstract fun bindGetCustomers(
        getCustomersImpl: GetCustomersImpl
    ): GetCustomers
}
