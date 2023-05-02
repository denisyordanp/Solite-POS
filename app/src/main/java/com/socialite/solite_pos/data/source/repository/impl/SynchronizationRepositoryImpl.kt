package com.socialite.solite_pos.data.source.repository.impl

import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import com.socialite.solite_pos.data.source.local.entity.room.master.Promo
import com.socialite.solite_pos.data.source.repository.CategoriesRepository
import com.socialite.solite_pos.data.source.repository.CustomersRepository
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.data.source.repository.PromosRepository
import com.socialite.solite_pos.data.source.repository.SynchronizationRepository
import com.socialite.solite_pos.data.source.repository.VariantOptionsRepository
import com.socialite.solite_pos.data.source.repository.VariantsRepository
import kotlinx.coroutines.flow.firstOrNull

class SynchronizationRepositoryImpl(
    private val customersRepository: CustomersRepository,
    private val categoriesRepository: CategoriesRepository,
    private val promosRepository: PromosRepository,
    private val paymentsRepository: PaymentsRepository,
    private val ordersRepository: OrdersRepository,
    private val outcomesRepository: OutcomesRepository,
    private val productsRepository: ProductsRepository,
    private val variantsRepository: VariantsRepository,
    private val variantOptionsRepository: VariantOptionsRepository,
    private val productVariantsRepository: ProductVariantsRepository
) : SynchronizationRepository {
    override suspend fun synchronize(): Boolean {
        val customer = customersRepository.getCustomers().firstOrNull() ?: emptyList()
        val category = categoriesRepository.getCategories(Category.getFilter(Category.ALL))
        val promo = promosRepository.getPromos(Promo.filter(Promo.Status.ALL))
        val payment = paymentsRepository.getPayments(Payment.filter(Payment.ALL))
        val order = ordersRepository.getOrders()
        val outcome = outcomesRepository.getOutcomes()
        val product = productsRepository.getProducts()
        val variant = variantsRepository.getVariants().firstOrNull() ?: emptyList()
        val orderDetail = ordersRepository.getOrderDetails()
        val orderPayment = ordersRepository.getOrderPayments()
        val orderPromo = ordersRepository.getOrderPromos()
        val variantOption = variantOptionsRepository.getVariantOptions()
        val orderProductVariant = ordersRepository.getOrderProductVariants()
        val variantProduct = productVariantsRepository.getVariantProducts()

        return true
    }
}
