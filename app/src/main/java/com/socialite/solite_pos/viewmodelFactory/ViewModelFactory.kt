package com.socialite.solite_pos.viewmodelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.socialite.solite_pos.data.source.domain.GetIncomesRecapData
import com.socialite.solite_pos.data.source.domain.GetProductOrder
import com.socialite.solite_pos.data.source.domain.GetProductVariantOptions
import com.socialite.solite_pos.data.source.domain.NewOrder
import com.socialite.solite_pos.data.source.repository.CategoriesRepository
import com.socialite.solite_pos.data.source.repository.CustomersRepository
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.data.source.repository.PurchasesRepository
import com.socialite.solite_pos.data.source.repository.SoliteRepository
import com.socialite.solite_pos.data.source.repository.SuppliersRepository
import com.socialite.solite_pos.data.source.repository.VariantMixesRepository
import com.socialite.solite_pos.data.source.repository.VariantOptionsRepository
import com.socialite.solite_pos.data.source.repository.VariantsRepository
import com.socialite.solite_pos.utils.di.DomainInjection.provideGetIncomesRecapData
import com.socialite.solite_pos.utils.di.DomainInjection.provideGetProductOrder
import com.socialite.solite_pos.utils.di.DomainInjection.provideGetProductVariantOptions
import com.socialite.solite_pos.utils.di.DomainInjection.provideNewOrder
import com.socialite.solite_pos.utils.di.RepositoryInjection.provideCategoriesRepository
import com.socialite.solite_pos.utils.di.RepositoryInjection.provideCustomersRepository
import com.socialite.solite_pos.utils.di.RepositoryInjection.provideOrdersRepository
import com.socialite.solite_pos.utils.di.RepositoryInjection.provideOutcomesRepository
import com.socialite.solite_pos.utils.di.RepositoryInjection.providePaymentsRepository
import com.socialite.solite_pos.utils.di.RepositoryInjection.provideProductVariantsRepository
import com.socialite.solite_pos.utils.di.RepositoryInjection.provideProductsRepository
import com.socialite.solite_pos.utils.di.RepositoryInjection.providePurchasesRepository
import com.socialite.solite_pos.utils.di.RepositoryInjection.provideSoliteRepository
import com.socialite.solite_pos.utils.di.RepositoryInjection.provideSupplierRepository
import com.socialite.solite_pos.utils.di.RepositoryInjection.provideVariantMixesRepository
import com.socialite.solite_pos.utils.di.RepositoryInjection.provideVariantOptionsRepository
import com.socialite.solite_pos.utils.di.RepositoryInjection.provideVariantsRepository
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import com.socialite.solite_pos.view.viewModel.UserViewModel

class ViewModelFactory private constructor(
    private val repository: SoliteRepository,
    private val paymentsRepository: PaymentsRepository,
    private val supplierRepository: SuppliersRepository,
    private val customersRepository: CustomersRepository,
    private val variantsRepository: VariantsRepository,
    private val variantOptionsRepository: VariantOptionsRepository,
    private val categoriesRepository: CategoriesRepository,
    private val outcomesRepository: OutcomesRepository,
    private val productsRepository: ProductsRepository,
    private val productVariantsRepository: ProductVariantsRepository,
    private val purchasesRepository: PurchasesRepository,
    private val getProductVariantOptions: GetProductVariantOptions,
    private val ordersRepository: OrdersRepository,
    private val newOrder: NewOrder,
    private val getProductOrder: GetProductOrder,
    private val getIncomesRecapData: GetIncomesRecapData,
    private val variantMixesRepository: VariantMixesRepository,
) : NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = ViewModelFactory(
                            provideSoliteRepository(context),
                            providePaymentsRepository(context),
                            provideSupplierRepository(context),
                            provideCustomersRepository(context),
                            provideVariantsRepository(context),
                            provideVariantOptionsRepository(context),
                            provideCategoriesRepository(context),
                            provideOutcomesRepository(context),
                            provideProductsRepository(context),
                            provideProductVariantsRepository(context),
                            providePurchasesRepository(context),
                            provideGetProductVariantOptions(context),
                            provideOrdersRepository(context),
                            provideNewOrder(context),
                            provideGetProductOrder(context),
                            provideGetIncomesRecapData(context),
                            provideVariantMixesRepository(context),
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
                    paymentsRepository,
                    supplierRepository,
                    customersRepository,
                    outcomesRepository,
                    purchasesRepository
                ) as T
            }

            modelClass.isAssignableFrom(OrderViewModel::class.java) -> {
                OrderViewModel(
                    repository,
                    ordersRepository,
                    newOrder,
                    getProductOrder,
                    getIncomesRecapData
                ) as T
            }

            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProductViewModel::class.java) -> {
                ProductViewModel(
                    variantsRepository,
                    variantOptionsRepository,
                    categoriesRepository,
                    productsRepository,
                    productVariantsRepository,
                    getProductVariantOptions,
                    variantMixesRepository
                ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}
