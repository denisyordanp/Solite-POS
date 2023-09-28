package com.socialite.domain.di

import com.socialite.domain.domain.AddNewCategory
import com.socialite.domain.domain.AddNewPayment
import com.socialite.domain.domain.AddNewProduct
import com.socialite.domain.domain.AddNewPromo
import com.socialite.domain.domain.AddNewStore
import com.socialite.domain.domain.AddNewUser
import com.socialite.domain.domain.AddNewVariant
import com.socialite.domain.domain.AddNewVariantOption
import com.socialite.domain.domain.AddNewVariantProduct
import com.socialite.domain.domain.ChangePassword
import com.socialite.domain.domain.FetchRemoteConfig
import com.socialite.domain.domain.GetAllOrderListByReport
import com.socialite.domain.domain.GetCategories
import com.socialite.domain.domain.GetCategoryProductVariantCount
import com.socialite.domain.domain.GetCustomers
import com.socialite.domain.domain.FetchLoggedInUser
import com.socialite.domain.domain.FetchUsers
import com.socialite.domain.domain.GetLoggedInUser
import com.socialite.domain.domain.GetOrderListByReport
import com.socialite.domain.domain.GetOrderMenusWithAmount
import com.socialite.domain.domain.GetOrderWithProduct
import com.socialite.domain.domain.GetOrdersGeneralMenuBadge
import com.socialite.domain.domain.GetOrdersMenuWithOrders
import com.socialite.domain.domain.GetOutcomes
import com.socialite.domain.domain.GetPayments
import com.socialite.domain.domain.GetPrinterDevice
import com.socialite.domain.domain.GetProductById
import com.socialite.domain.domain.GetProductVariantOptions
import com.socialite.domain.domain.GetProductWithCategories
import com.socialite.domain.domain.GetProductWithCategoryById
import com.socialite.domain.domain.GetProductWithVariantOptions
import com.socialite.domain.domain.GetPromos
import com.socialite.domain.domain.GetRecapData
import com.socialite.domain.domain.GetSelectedStore
import com.socialite.domain.domain.GetSelectedStoreId
import com.socialite.domain.domain.GetStoreMenus
import com.socialite.domain.domain.GetStores
import com.socialite.domain.domain.GetToken
import com.socialite.domain.domain.GetUsers
import com.socialite.domain.domain.GetVariantsProductById
import com.socialite.domain.domain.GetVariantsWithOptions
import com.socialite.domain.domain.IsServerActive
import com.socialite.domain.domain.IsShouldSelectStore
import com.socialite.domain.domain.IsUserStaff
import com.socialite.domain.domain.LoginUser
import com.socialite.domain.domain.Logout
import com.socialite.domain.domain.MigrateToUUID
import com.socialite.domain.domain.NewCustomer
import com.socialite.domain.domain.NewOrder
import com.socialite.domain.domain.NewOutcome
import com.socialite.domain.domain.PayOrder
import com.socialite.domain.domain.RegisterUser
import com.socialite.domain.domain.RemoveVariantProduct
import com.socialite.domain.domain.SelectStore
import com.socialite.domain.domain.SetDarkMode
import com.socialite.domain.domain.SetNewPrinterAddress
import com.socialite.domain.domain.Synchronize
import com.socialite.domain.domain.UpdateCategory
import com.socialite.domain.domain.UpdateOrder
import com.socialite.domain.domain.UpdateOrderProducts
import com.socialite.domain.domain.UpdatePayment
import com.socialite.domain.domain.UpdateProduct
import com.socialite.domain.domain.UpdatePromo
import com.socialite.domain.domain.UpdateStore
import com.socialite.domain.domain.UpdateUser
import com.socialite.domain.domain.UpdateVariant
import com.socialite.domain.domain.UpdateVariantOption
import com.socialite.domain.domain.impl.AddNewCategoryImpl
import com.socialite.domain.domain.impl.AddNewPaymentImpl
import com.socialite.domain.domain.impl.AddNewProductImpl
import com.socialite.domain.domain.impl.AddNewPromoImpl
import com.socialite.domain.domain.impl.AddNewStoreImpl
import com.socialite.domain.domain.impl.AddNewUserImpl
import com.socialite.domain.domain.impl.AddNewVariantImpl
import com.socialite.domain.domain.impl.AddNewVariantOptionImpl
import com.socialite.domain.domain.impl.AddNewVariantProductImpl
import com.socialite.domain.domain.impl.ChangePasswordImpl
import com.socialite.domain.domain.impl.FetchRemoteConfigImpl
import com.socialite.domain.domain.impl.GetAllOrderListByReportImpl
import com.socialite.domain.domain.impl.GetCategoriesImpl
import com.socialite.domain.domain.impl.GetCategoryProductVariantCountImpl
import com.socialite.domain.domain.impl.GetCustomersImpl
import com.socialite.domain.domain.impl.FetchLoggedInUserImpl
import com.socialite.domain.domain.impl.FetchUsersImpl
import com.socialite.domain.domain.impl.GetLoggedInUserImpl
import com.socialite.domain.domain.impl.GetOrderListByReportImpl
import com.socialite.domain.domain.impl.GetOrderMenusWithAmountImpl
import com.socialite.domain.domain.impl.GetOrderWithProductImpl
import com.socialite.domain.domain.impl.GetOrdersGeneralMenuBadgeImpl
import com.socialite.domain.domain.impl.GetOrdersMenuWithOrdersImpl
import com.socialite.domain.domain.impl.GetOutcomesImpl
import com.socialite.domain.domain.impl.GetPaymentsImpl
import com.socialite.domain.domain.impl.GetPrinterDeviceImpl
import com.socialite.domain.domain.impl.GetProductByIdImpl
import com.socialite.domain.domain.impl.GetProductVariantOptionsImpl
import com.socialite.domain.domain.impl.GetProductWithCategoriesImpl
import com.socialite.domain.domain.impl.GetProductWithCategoryByIdImpl
import com.socialite.domain.domain.impl.GetProductWithVariantOptionsImpl
import com.socialite.domain.domain.impl.GetPromosImpl
import com.socialite.domain.domain.impl.GetRecapDataImpl
import com.socialite.domain.domain.impl.GetSelectedStoreIdImpl
import com.socialite.domain.domain.impl.GetSelectedStoreImpl
import com.socialite.domain.domain.impl.GetStoreMenusImpl
import com.socialite.domain.domain.impl.GetStoresImpl
import com.socialite.domain.domain.impl.GetTokenImpl
import com.socialite.domain.domain.impl.GetUsersImpl
import com.socialite.domain.domain.impl.GetVariantsProductByIdImpl
import com.socialite.domain.domain.impl.GetVariantsWithOptionsImpl
import com.socialite.domain.domain.impl.IsServerActiveImpl
import com.socialite.domain.domain.impl.IsShouldSelectStoreImpl
import com.socialite.domain.domain.impl.IsUserStaffImpl
import com.socialite.domain.domain.impl.LoginUserImpl
import com.socialite.domain.domain.impl.LogoutImpl
import com.socialite.domain.domain.impl.MigrateToUUIDImpl
import com.socialite.domain.domain.impl.NewCustomerImpl
import com.socialite.domain.domain.impl.NewOrderImpl
import com.socialite.domain.domain.impl.NewOutcomeImpl
import com.socialite.domain.domain.impl.PayOrderImpl
import com.socialite.domain.domain.impl.RegisterUserImpl
import com.socialite.domain.domain.impl.RemoveVariantProductImpl
import com.socialite.domain.domain.impl.SelectStoreImpl
import com.socialite.domain.domain.impl.SetDarkModeImpl
import com.socialite.domain.domain.impl.SetNewPrinterAddressImpl
import com.socialite.domain.domain.impl.SynchronizeImpl
import com.socialite.domain.domain.impl.UpdateCategoryImpl
import com.socialite.domain.domain.impl.UpdateOrderImpl
import com.socialite.domain.domain.impl.UpdateOrderProductsImpl
import com.socialite.domain.domain.impl.UpdatePaymentImpl
import com.socialite.domain.domain.impl.UpdateProductImpl
import com.socialite.domain.domain.impl.UpdatePromoImpl
import com.socialite.domain.domain.impl.UpdateStoreImpl
import com.socialite.domain.domain.impl.UpdateUserImpl
import com.socialite.domain.domain.impl.UpdateVariantImpl
import com.socialite.domain.domain.impl.UpdateVariantOptionImpl
import com.socialite.domain.helper.IdManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainProviderModule {
    @Provides
    fun provideIdManager(): IdManager = IdManager()
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainBinderModule {

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
    abstract fun bindSetDarkMode(
        setDarkModeImpl: SetDarkModeImpl
    ): SetDarkMode

    @Binds
    abstract fun bindNewCustomer(
        newCustomerImpl: NewCustomerImpl
    ): NewCustomer

    @Binds
    abstract fun bindGetCustomers(
        getCustomersImpl: GetCustomersImpl
    ): GetCustomers

    @Binds
    abstract fun bindUpdateOrder(
        updateOrderImpl: UpdateOrderImpl
    ): UpdateOrder

    @Binds
    abstract fun bindGetPromos(
        getPromosImpl: GetPromosImpl
    ): GetPromos

    @Binds
    abstract fun bindGetPayments(
        getPaymentsImpl: GetPaymentsImpl
    ): GetPayments

    @Binds
    abstract fun bindGetPrinterDevice(
        getPrinterDeviceImpl: GetPrinterDeviceImpl
    ): GetPrinterDevice

    @Binds
    abstract fun bindGetCategories(
        getCategoriesImpl: GetCategoriesImpl
    ): GetCategories

    @Binds
    abstract fun bindAddNewCategory(
        addNewCategoryImpl: AddNewCategoryImpl
    ): AddNewCategory

    @Binds
    abstract fun bindUpdateCategory(
        updateCategoryImpl: UpdateCategoryImpl
    ): UpdateCategory

    @Binds
    abstract fun bindGetOutcomes(
        getOutcomesImpl: GetOutcomesImpl
    ): GetOutcomes

    @Binds
    abstract fun bindAddNewPayment(
        addNewPaymentImpl: AddNewPaymentImpl
    ): AddNewPayment

    @Binds
    abstract fun bindUpdatePayment(
        updatePaymentImpl: UpdatePaymentImpl
    ): UpdatePayment

    @Binds
    abstract fun bindGetProductWithCategoryById(
        getProductWithCategoryByIdImpl: GetProductWithCategoryByIdImpl
    ): GetProductWithCategoryById

    @Binds
    abstract fun bindUpdateProduct(
        updateProductImpl: UpdateProductImpl
    ): UpdateProduct

    @Binds
    abstract fun bindAddNewProduct(
        addNewProductImpl: AddNewProductImpl
    ): AddNewProduct

    @Binds
    abstract fun bindAddNewPromo(
        addNewPromoImpl: AddNewPromoImpl
    ): AddNewPromo

    @Binds
    abstract fun bindUpdatePromo(
        updatePromoImpl: UpdatePromoImpl
    ): UpdatePromo

    @Binds
    abstract fun bindGetStores(
        getStoresImpl: GetStoresImpl
    ): GetStores

    @Binds
    abstract fun bindGetSelectedStoreId(
        getSelectedStoreIdImpl: GetSelectedStoreIdImpl
    ): GetSelectedStoreId

    @Binds
    abstract fun bindSelectStore(
        selectStoreImpl: SelectStoreImpl
    ): SelectStore

    @Binds
    abstract fun bindAddNewStore(
        addNewStoreImpl: AddNewStoreImpl
    ): AddNewStore

    @Binds
    abstract fun bindUpdateStore(
        updateStoreImpl: UpdateStoreImpl
    ): UpdateStore

    @Binds
    abstract fun bindAddNewVariant(
        addNewVariantImpl: AddNewVariantImpl
    ): AddNewVariant

    @Binds
    abstract fun bindUpdateVariant(
        updateVariantImpl: UpdateVariantImpl
    ): UpdateVariant

    @Binds
    abstract fun bindAddNewVariantOption(
        addNewVariantOptionImpl: AddNewVariantOptionImpl
    ): AddNewVariantOption

    @Binds
    abstract fun bindUpdateVariantOption(
        updateVariantOptionImpl: UpdateVariantOptionImpl
    ): UpdateVariantOption

    @Binds
    abstract fun bindGetProductById(
        getProductByIdImpl: GetProductByIdImpl
    ): GetProductById

    @Binds
    abstract fun bindGetVariantsProductById(
        getVariantsProductByIdImpl: GetVariantsProductByIdImpl
    ): GetVariantsProductById

    @Binds
    abstract fun bindAddNewVariantProduct(
        addNewVariantProductImpl: AddNewVariantProductImpl
    ): AddNewVariantProduct

    @Binds
    abstract fun bindRemoveVariantProduct(
        removeVariantProductImpl: RemoveVariantProductImpl
    ): RemoveVariantProduct

    @Binds
    abstract fun bindGetOrderListByReport(
        getOrderListByReportImpl: GetOrderListByReportImpl
    ): GetOrderListByReport

    @Binds
    abstract fun bindGetAllOrderListByReport(
        getAllOrderListByReportImpl: GetAllOrderListByReportImpl
    ): GetAllOrderListByReport

    @Binds
    abstract fun bindGetUsers(
        getUsersImpl: GetUsersImpl
    ): GetUsers

    @Binds
    abstract fun bindAddNewUser(
        addNewUserImpl: AddNewUserImpl
    ): AddNewUser

    @Binds
    abstract fun bindUpdateUser(
        updateUserImpl: UpdateUserImpl
    ): UpdateUser

    @Binds
    abstract fun bindLogout(
        logoutImpl: LogoutImpl
    ): Logout

    @Binds
    abstract fun bindGetStoreMenus(
        getStoreMenusImpl: GetStoreMenusImpl
    ): GetStoreMenus

    @Binds
    abstract fun bindIsUserStaff(
        isUserStaffImpl: IsUserStaffImpl
    ): IsUserStaff

    @Binds
    abstract fun bindFetchLoggedInUser(
        getLoggedInUserImpl: FetchLoggedInUserImpl
    ): FetchLoggedInUser

    @Binds
    abstract fun bindFetchUsers(
        fetchUsersImpl: FetchUsersImpl
    ): FetchUsers

    @Binds
    abstract fun bindGetLoggedInUser(
        getLoggedInUserImpl: GetLoggedInUserImpl
    ): GetLoggedInUser

    @Binds
    abstract fun bindGetSelectedStore(
        getSelectedStoreImpl: GetSelectedStoreImpl
    ): GetSelectedStore

    @Binds
    abstract fun bindChangePassword(
        changePasswordImpl: ChangePasswordImpl
    ): ChangePassword
}
