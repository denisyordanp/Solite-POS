package com.socialite.solite_pos.utils.tools

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.socialite.solite_pos.data.source.domain.GetOrdersGeneralMenuBadge
import com.socialite.solite_pos.data.source.domain.GetProductOrder
import com.socialite.solite_pos.data.source.domain.GetProductVariantOptions
import com.socialite.solite_pos.data.source.domain.GetProductWithCategories
import com.socialite.solite_pos.data.source.domain.GetRecapData
import com.socialite.solite_pos.data.source.domain.MigrateToUUID
import com.socialite.solite_pos.data.source.domain.NewOrder
import com.socialite.solite_pos.data.source.domain.NewOutcome
import com.socialite.solite_pos.data.source.domain.PayOrder
import com.socialite.solite_pos.data.source.domain.Synchronize
import com.socialite.solite_pos.data.source.domain.UpdateOrderProducts
import com.socialite.solite_pos.data.source.repository.CategoriesRepository
import com.socialite.solite_pos.data.source.repository.CustomersRepository
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
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection.provideGetIncomesRecapData
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection.provideGetOrdersGeneralMenuBadge
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection.provideGetProductOrder
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection.provideGetProductVariantOptions
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection.provideGetProductWithCategories
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection.provideMigrateToUUID
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection.provideNewOrder
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection.provideNewOutcome
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection.providePayOrder
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection.provideSynchronize
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection.provideUpdateOrderProducts
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.provideCategoriesRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.provideCustomersRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.provideOrdersRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.provideOutcomesRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.providePaymentsRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.provideProductVariantsRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.provideProductsRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.providePromosRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.provideRemoteConfigRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.provideSettingRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.provideStoreRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.provideVariantOptionsRepository
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection.provideVariantsRepository
import com.socialite.solite_pos.view.settings.SettingViewModel
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import com.socialite.solite_pos.view.viewModel.ProductViewModel

class LoggedInViewModelFactory private constructor(
    private val paymentsRepository: PaymentsRepository,
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
    private val payOrder: PayOrder,
    private val getOrdersGeneralMenuBadge: GetOrdersGeneralMenuBadge,
    private val storeRepository: StoreRepository,
    private val settingRepository: SettingRepository,
    private val newOutcome: NewOutcome,
    private val updateOrderProducts: UpdateOrderProducts,
    private val promosRepository: PromosRepository,
    private val synchronize: Synchronize,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val migrateToUUID: MigrateToUUID,
    private val getProductWithCategories: GetProductWithCategories
) : NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: LoggedInViewModelFactory? = null

        fun getInstance(context: Context): LoggedInViewModelFactory {
            if (INSTANCE == null) {
                synchronized(LoggedInViewModelFactory::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = LoggedInViewModelFactory(
                            paymentsRepository = providePaymentsRepository(context),
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
                            payOrder = providePayOrder(context),
                            getOrdersGeneralMenuBadge = provideGetOrdersGeneralMenuBadge(context),
                            storeRepository = provideStoreRepository(context),
                            settingRepository = provideSettingRepository(context),
                            newOutcome = provideNewOutcome(context),
                            updateOrderProducts = provideUpdateOrderProducts(context),
                            promosRepository = providePromosRepository(context),
                            synchronize = provideSynchronize(context),
                            remoteConfigRepository = provideRemoteConfigRepository(context),
                            migrateToUUID = provideMigrateToUUID(context),
                            getProductWithCategories = provideGetProductWithCategories(context)
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
                    customersRepository = customersRepository,
                    outcomesRepository = outcomesRepository,
                    storeRepository = storeRepository,
                    settingRepository = settingRepository,
                    newOutcome = newOutcome,
                    promosRepository = promosRepository,
                ) as T
            }

            modelClass.isAssignableFrom(OrderViewModel::class.java) -> {
                OrderViewModel(
                    orderRepository = ordersRepository,
                    newOrder = newOrder,
                    getProductOrder = getProductOrder,
                    getRecapData = getRecapData,
                    payOrder = payOrder,
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
                    getProductWithCategories = getProductWithCategories
                ) as T
            }

            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(
                    migrateToUUID = migrateToUUID,
                    synchronize = synchronize,
                    settingRepository = settingRepository,
                    remoteConfigRepository = remoteConfigRepository,
                    getOrdersGeneralMenuBadge = getOrdersGeneralMenuBadge
                ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}
