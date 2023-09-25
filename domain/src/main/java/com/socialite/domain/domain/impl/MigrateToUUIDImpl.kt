package com.socialite.domain.domain.impl

import com.socialite.data.repository.CategoriesRepository
import com.socialite.data.repository.CustomersRepository
import com.socialite.data.repository.OrdersRepository
import com.socialite.data.repository.OutcomesRepository
import com.socialite.data.repository.PaymentsRepository
import com.socialite.data.repository.ProductVariantsRepository
import com.socialite.data.repository.ProductsRepository
import com.socialite.data.repository.PromosRepository
import com.socialite.data.repository.SettingRepository
import com.socialite.data.repository.StoreRepository
import com.socialite.data.repository.VariantOptionsRepository
import com.socialite.data.repository.VariantsRepository
import com.socialite.domain.domain.MigrateToUUID
import javax.inject.Inject

class MigrateToUUIDImpl @Inject constructor(
    private val customersRepository: CustomersRepository,
    private val storeRepository: StoreRepository,
    private val categoriesRepository: CategoriesRepository,
    private val promosRepository: PromosRepository,
    private val paymentsRepository: PaymentsRepository,
    private val ordersRepository: OrdersRepository,
    private val outcomesRepository: OutcomesRepository,
    private val productsRepository: ProductsRepository,
    private val variantsRepository: VariantsRepository,
    private val variantOptionsRepository: VariantOptionsRepository,
    private val productVariantsRepository: ProductVariantsRepository,
    private val settingRepository: SettingRepository
) : MigrateToUUID {
    override suspend fun invoke() {
        if (settingRepository.isMigrated().not()) {
            customersRepository.deleteAllOldCustomers()
            storeRepository.deleteAllOldStores()
            categoriesRepository.deleteAllOldCategories()
            promosRepository.deleteAllOldCustomers()
            paymentsRepository.deleteAllOldPayments()
            outcomesRepository.deleteAllOldOutcomes()
            productsRepository.deleteAllOldProducts()
            variantsRepository.deleteAllOldVariants()
            variantOptionsRepository.deleteAllOldVariantOptions()
            productVariantsRepository.deleteAllOldProductVariants()
            ordersRepository.deleteAllOldOrders()
            settingRepository.setMigration(true)
        }
    }
}
