package com.socialite.solite_pos.utils.tools

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.socialite.solite_pos.data.source.domain.GetOrdersGeneralMenuBadge
import com.socialite.solite_pos.data.source.domain.GetProductOrder
import com.socialite.solite_pos.data.source.domain.GetProductVariantOptions
import com.socialite.solite_pos.data.source.domain.GetRecapData
import com.socialite.solite_pos.data.source.domain.LoginUser
import com.socialite.solite_pos.data.source.domain.MigrateToUUID
import com.socialite.solite_pos.data.source.domain.NewOrder
import com.socialite.solite_pos.data.source.domain.NewOutcome
import com.socialite.solite_pos.data.source.domain.PayOrder
import com.socialite.solite_pos.data.source.domain.RegisterUser
import com.socialite.solite_pos.data.source.domain.UpdateOrderProducts
import com.socialite.solite_pos.data.source.repository.CategoriesRepository
import com.socialite.solite_pos.data.source.repository.CustomersRepository
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.data.source.repository.PromosRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.data.source.repository.StoreRepository
import com.socialite.solite_pos.data.source.repository.SuppliersRepository
import com.socialite.solite_pos.data.source.domain.Synchronize
import com.socialite.solite_pos.data.source.repository.AccountRepository
import com.socialite.solite_pos.data.source.repository.VariantMixesRepository
import com.socialite.solite_pos.data.source.repository.VariantOptionsRepository
import com.socialite.solite_pos.data.source.repository.VariantsRepository
import com.socialite.solite_pos.di.DomainInjection.provideGetIncomesRecapData
import com.socialite.solite_pos.di.DomainInjection.provideGetOrdersGeneralMenuBadge
import com.socialite.solite_pos.di.DomainInjection.provideGetProductOrder
import com.socialite.solite_pos.di.DomainInjection.provideGetProductVariantOptions
import com.socialite.solite_pos.di.DomainInjection.provideMigrateToUUID
import com.socialite.solite_pos.di.DomainInjection.provideLoginUser
import com.socialite.solite_pos.di.DomainInjection.provideNewOrder
import com.socialite.solite_pos.di.DomainInjection.provideNewOutcome
import com.socialite.solite_pos.di.DomainInjection.providePayOrder
import com.socialite.solite_pos.di.DomainInjection.provideRegisterUser
import com.socialite.solite_pos.di.DomainInjection.provideSynchronize
import com.socialite.solite_pos.di.DomainInjection.provideUpdateOrderProducts
import com.socialite.solite_pos.di.RepositoryInjection.provideCategoriesRepository
import com.socialite.solite_pos.di.RepositoryInjection.provideCustomersRepository
import com.socialite.solite_pos.di.RepositoryInjection.provideOrdersRepository
import com.socialite.solite_pos.di.RepositoryInjection.provideOutcomesRepository
import com.socialite.solite_pos.di.RepositoryInjection.providePaymentsRepository
import com.socialite.solite_pos.di.RepositoryInjection.provideProductVariantsRepository
import com.socialite.solite_pos.di.RepositoryInjection.provideProductsRepository
import com.socialite.solite_pos.di.RepositoryInjection.providePromosRepository
import com.socialite.solite_pos.di.RepositoryInjection.provideSettingRepository
import com.socialite.solite_pos.di.RepositoryInjection.provideStoreRepository
import com.socialite.solite_pos.di.RepositoryInjection.provideSupplierRepository
import com.socialite.solite_pos.di.RepositoryInjection.provideUserRepository
import com.socialite.solite_pos.di.RepositoryInjection.provideVariantMixesRepository
import com.socialite.solite_pos.di.RepositoryInjection.provideVariantOptionsRepository
import com.socialite.solite_pos.di.RepositoryInjection.provideVariantsRepository
import com.socialite.solite_pos.view.login.LoginViewModel
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import com.socialite.solite_pos.view.viewModel.ProductViewModel

class ViewModelFactory private constructor(
    private val paymentsRepository: PaymentsRepository,
    private val supplierRepository: SuppliersRepository,
    private val customersRepository: CustomersRepository,
    private val variantsRepository: VariantsRepository,
    private val variantOptionsRepository: VariantOptionsRepository,
    private val categoriesRepository: CategoriesRepository,
    private val outcomesRepository: OutcomesRepository,
    private val productsRepository: ProductsRepository,
    private val productVariantsRepository: ProductVariantsRepository,
    private val getProductVariantOptions: GetProductVariantOptions,
    private val ordersRepository: OrdersRepository,
    private val newOrder: NewOrder,
    private val getProductOrder: GetProductOrder,
    private val getRecapData: GetRecapData,
    private val variantMixesRepository: VariantMixesRepository,
    private val payOrder: PayOrder,
    private val getOrdersGeneralMenuBadge: GetOrdersGeneralMenuBadge,
    private val storeRepository: StoreRepository,
    private val settingRepository: SettingRepository,
    private val newOutcome: NewOutcome,
    private val updateOrderProducts: UpdateOrderProducts,
    private val promosRepository: PromosRepository,
    private val migrateToUUID: MigrateToUUID,
    private val loginUser: LoginUser,
    private val registerUser: RegisterUser,
    private val synchronize: Synchronize,
    private val accountRepository: AccountRepository
) : NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = ViewModelFactory(
                            paymentsRepository = providePaymentsRepository(context),
                            supplierRepository = provideSupplierRepository(context),
                            customersRepository = provideCustomersRepository(context),
                            variantsRepository = provideVariantsRepository(context),
                            variantOptionsRepository = provideVariantOptionsRepository(context),
                            categoriesRepository = provideCategoriesRepository(context),
                            outcomesRepository = provideOutcomesRepository(context),
                            productsRepository = provideProductsRepository(context),
                            productVariantsRepository = provideProductVariantsRepository(context),
                            getProductVariantOptions = provideGetProductVariantOptions(context),
                            ordersRepository = provideOrdersRepository(context),
                            newOrder = provideNewOrder(context),
                            getProductOrder = provideGetProductOrder(context),
                            getRecapData = provideGetIncomesRecapData(context),
                            variantMixesRepository = provideVariantMixesRepository(context),
                            payOrder = providePayOrder(context),
                            getOrdersGeneralMenuBadge = provideGetOrdersGeneralMenuBadge(context),
                            storeRepository = provideStoreRepository(context),
                            settingRepository = provideSettingRepository(context),
                            newOutcome = provideNewOutcome(context),
                            updateOrderProducts = provideUpdateOrderProducts(context),
                            promosRepository = providePromosRepository(context),
                            migrateToUUID = provideMigrateToUUID(context),
                            loginUser = provideLoginUser(context),
                            registerUser = provideRegisterUser(context),
                            synchronize = provideSynchronize(context),
                            accountRepository = provideUserRepository(context)
                        )
                    }
                }
            }
            return INSTANCE!!
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(
                    paymentRepository = paymentsRepository,
                    supplierRepository = supplierRepository,
                    customersRepository = customersRepository,
                    outcomesRepository = outcomesRepository,
                    storeRepository = storeRepository,
                    settingRepository = settingRepository,
                    newOutcome = newOutcome,
                    promosRepository = promosRepository,
                    migrateToUUID = migrateToUUID,
                    synchronize = synchronize,
                    accountRepository = accountRepository
                ) as T
            }

            modelClass.isAssignableFrom(OrderViewModel::class.java) -> {
                OrderViewModel(
                    orderRepository = ordersRepository,
                    newOrder = newOrder,
                    getProductOrder = getProductOrder,
                    getRecapData = getRecapData,
                    payOrder = payOrder,
                    getOrdersGeneralMenuBadge = getOrdersGeneralMenuBadge,
                    updateOrderProducts = updateOrderProducts
                ) as T
            }

            modelClass.isAssignableFrom(ProductViewModel::class.java) -> {
                ProductViewModel(
                    variantsRepository = variantsRepository,
                    variantOptionsRepository = variantOptionsRepository,
                    categoriesRepository = categoriesRepository,
                    productsRepository = productsRepository,
                    productVariantsRepository = productVariantsRepository,
                    getProductVariantOptions = getProductVariantOptions,
                    variantMixesRepository = variantMixesRepository
                ) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(
                    loginUser = loginUser,
                    registerUser = registerUser
                ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}
