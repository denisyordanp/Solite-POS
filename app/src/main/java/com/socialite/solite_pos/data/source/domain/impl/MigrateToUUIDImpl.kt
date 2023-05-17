package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.MigrateToUUID
import com.socialite.solite_pos.data.source.repository.CategoriesRepository
import com.socialite.solite_pos.data.source.repository.CustomersRepository
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.data.source.repository.PromosRepository
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
    private val outcomesRepository: OutcomesRepository,
    private val productsRepository: ProductsRepository,
    private val variantsRepository: VariantsRepository,
    private val variantOptionsRepository: VariantOptionsRepository,
) : MigrateToUUID {
    override suspend fun invoke() {
        customersRepository.migrateToUUID()
        storeRepository.migrateToUUID()
        categoriesRepository.migrateToUUID()
        promosRepository.migrateToUUID()
        paymentsRepository.migrateToUUID()
        ordersRepository.migrateToUUID()
        outcomesRepository.migrateToUUID()
        productsRepository.migrateToUUID()
        variantsRepository.migrateToUUID()
        variantOptionsRepository.migrateToUUID()
    }
}