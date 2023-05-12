package com.socialite.solite_pos.data.source.repository.impl

import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import com.socialite.solite_pos.data.source.local.entity.room.master.Promo
import com.socialite.solite_pos.data.source.remote.SoliteServices
import com.socialite.solite_pos.data.source.remote.response.entity.SynchronizeResponse
import com.socialite.solite_pos.data.source.repository.CategoriesRepository
import com.socialite.solite_pos.data.source.repository.CustomersRepository
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.data.source.repository.PromosRepository
import com.socialite.solite_pos.data.source.repository.StoreRepository
import com.socialite.solite_pos.data.source.repository.SynchronizationRepository
import com.socialite.solite_pos.data.source.repository.VariantOptionsRepository
import com.socialite.solite_pos.data.source.repository.VariantsRepository
import kotlinx.coroutines.flow.firstOrNull

class SynchronizationRepositoryImpl(
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
    private val service: SoliteServices
) : SynchronizationRepository {
    override suspend fun synchronize(): Boolean {
        // Get all un uploaded data
        val needUploadCustomers = customersRepository.getNotUploadedCustomers().map { it.toResponse() }
        val store = storeRepository.getStores().firstOrNull()?.map { it.toResponse() } ?: emptyList()
        val category =
            categoriesRepository.getCategories(Category.getFilter(Category.ALL)).firstOrNull()
                ?.map { it.toResponse() } ?: emptyList()
        val promo = promosRepository.getPromos(Promo.filter(Promo.Status.ALL)).firstOrNull()
            ?.map { it.toResponse() } ?: emptyList()
        val payment = paymentsRepository.getPayments(Payment.filter(Payment.ALL)).firstOrNull()
            ?.map { it.toResponse() } ?: emptyList()
        val order = ordersRepository.getOrders().map { it.toResponse() }
        val outcome = outcomesRepository.getOutcomes().map { it.toResponse() }
        val product = productsRepository.getProducts().map { it.toResponse() }
        val variant = variantsRepository.getVariants().firstOrNull()?.map { it.toResponse() } ?: emptyList()
        val orderDetail = ordersRepository.getOrderDetails().map { it.toResponse() }
        val orderPayment = ordersRepository.getOrderPayments().map { it.toResponse() }
        val orderPromo = ordersRepository.getOrderPromos().map { it.toResponse() }
        val variantOption = variantOptionsRepository.getVariantOptions().map { it.toResponse() }
        val orderProductVariant = ordersRepository.getOrderProductVariants().map { it.toResponse() }
        val variantProduct = productVariantsRepository.getVariantProducts().map { it.toResponse() }

        val synchronizeData = SynchronizeResponse(
            customer = needUploadCustomers,
            category = category,
            store = store,
            promo = promo,
            payment = payment,
            order = order,
            outcome = outcome,
            product = product,
            variant = variant,
            orderDetail = orderDetail,
            orderPayment = orderPayment,
            orderPromo = orderPromo,
            variantOption = variantOption,
            orderProductVariant = orderProductVariant,
            variantProduct = variantProduct
        )
        val response = service.synchronize(synchronizeData)

        // Update all un uploaded data that already uploaded
        if (needUploadCustomers.isNotEmpty()) {
            customersRepository.insertCustomers(needUploadCustomers.map { it.toEntity() })
        }

        // Insert all missing data that given by server
        val missingCustomer = response.data?.customer
        if (!missingCustomer.isNullOrEmpty()) {
            customersRepository.insertCustomers(missingCustomer.map { it.toEntity() })
        }

        return true
    }
}
