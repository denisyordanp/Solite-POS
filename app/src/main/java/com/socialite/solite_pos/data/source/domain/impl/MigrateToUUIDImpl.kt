package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.MigrateToUUID
import com.socialite.solite_pos.data.source.repository.CategoriesRepository
import com.socialite.solite_pos.data.source.repository.CustomersRepository
import com.socialite.solite_pos.data.source.repository.OrderDetailsRepository
import com.socialite.solite_pos.data.source.repository.OrderPaymentsRepository
import com.socialite.solite_pos.data.source.repository.OrderPromosRepository
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.data.source.repository.PromosRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.data.source.repository.StoreRepository
import com.socialite.solite_pos.data.source.repository.VariantOptionsRepository
import com.socialite.solite_pos.data.source.repository.VariantsRepository

class MigrateToUUIDImpl(
    private val customersRepository: CustomersRepository,
    private val storeRepository: StoreRepository,
    private val categoriesRepository: CategoriesRepository,
    private val promosRepository: PromosRepository,
    private val paymentsRepository: PaymentsRepository,
    private val ordersRepository: OrdersRepository,
    private val orderDetailsRepository: OrderDetailsRepository,
    private val orderPaymentsRepository: OrderPaymentsRepository,
    private val orderPromosRepository: OrderPromosRepository,
    private val outcomesRepository: OutcomesRepository,
    private val productsRepository: ProductsRepository,
    private val variantsRepository: VariantsRepository,
    private val variantOptionsRepository: VariantOptionsRepository,
    private val productVariantsRepository: ProductVariantsRepository,
    private val settingRepository: SettingRepository
) : MigrateToUUID {
    override suspend fun invoke() {
        if (settingRepository.isMigrated() && settingRepository.isMigratedPhase2().not()) {
            customersRepository.deleteAllNewCustomers()
            storeRepository.deleteAllNewStores()
            categoriesRepository.deleteAllNewCategories()
            promosRepository.deleteAllNewCustomers()
            paymentsRepository.deleteAllNewPayments()
            outcomesRepository.deleteAllNewOutcomes()
            productsRepository.deleteAllNewProducts()
            variantsRepository.deleteAllNewVariants()
            variantOptionsRepository.deleteAllNewVariantOptions()
            productVariantsRepository.deleteAllNewProductVariants()
            ordersRepository.deleteAllNewOrders()

            settingRepository.setMigration(false)
            settingRepository.setMigrationPhase2(true)
        }
        if (settingRepository.isMigrated().not()) {
            customersRepository.migrateToUUID()
            storeRepository.migrateToUUID()
            categoriesRepository.migrateToUUID()
            promosRepository.migrateToUUID()
            paymentsRepository.migrateToUUID()
            outcomesRepository.migrateToUUID()
            productsRepository.migrateToUUID()
            variantsRepository.migrateToUUID()
            variantOptionsRepository.migrateToUUID()
            productVariantsRepository.migrateToUUID()
            ordersRepository.migrateToUUID()
            orderDetailsRepository.migrateToUUID()
            orderPaymentsRepository.migrateToUUID()
            orderPromosRepository.migrateToUUID()

//            customersRepository.deleteAllOldCustomers()
//            storeRepository.deleteAllOldStores()
//            categoriesRepository.deleteAllOldCategories()
//            promosRepository.deleteAllOldCustomers()
//            paymentsRepository.deleteAllOldCustomers()
//            outcomesRepository.deleteAllOldOutcomes()
//            productsRepository.deleteAllOldProducts()
//            variantsRepository.deleteAllOldVariants()
//            variantOptionsRepository.deleteAllOldVariantOptions()
//            productVariantsRepository.deleteAllOldProductVariants()
//            ordersRepository.deleteAllOldOrders()

            settingRepository.setMigration(true)
        }
    }
}
