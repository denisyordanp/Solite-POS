package com.socialite.solite_pos.data.source.repository.impl

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
import com.socialite.solite_pos.data.source.repository.Synchronize
import com.socialite.solite_pos.data.source.repository.VariantOptionsRepository
import com.socialite.solite_pos.data.source.repository.VariantsRepository

class SynchronizeImpl(
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
) : Synchronize {
    override suspend fun invoke(): Boolean {
        // Get all un uploaded data
        val needUploadCustomers =
            customersRepository.getNeedUploadCustomers().map { it.toResponse() }
        val needUploadStores = storeRepository.getNeedUploadStores().map { it.toResponse() }
        val needUploadCategories =
            categoriesRepository.getNeedUploadCategories().map { it.toResponse() }
        val needUploadPromo = promosRepository.getNeedUploadPromos().map { it.toResponse() }
        val needUploadPayments = paymentsRepository.getNeedUploadPayments().map { it.toResponse() }
        val needUploadOrders = ordersRepository.getNeedUploadOrders().map { it.toResponse() }
        val needUploadOutcomes = outcomesRepository.getNeedUploadOutcomes().map { it.toResponse() }
        val needUploadProducts = productsRepository.getNeedUploadProducts().map { it.toResponse() }
        val needUploadVariants = variantsRepository.getNeedUploadVariants().map { it.toResponse() }
        val needUploadOrderDetails =
            ordersRepository.getNeedUploadOrderDetails().map { it.toResponse() }
        val needUploadOrderPayments =
            ordersRepository.getNeedUploadOrderPayments().map { it.toResponse() }
        val needUploadOrderPromos =
            ordersRepository.getNeedUploadOrderPromos().map { it.toResponse() }
        val needUploadVariantOptions =
            variantOptionsRepository.getNeedUploadVariantOptions().map { it.toResponse() }
        val needUploadOrderProductVariants =
            ordersRepository.getNeedUploadOrderProductVariants().map { it.toResponse() }
        val needUploadVariantProducts =
            productVariantsRepository.getNeedUploadVariantProducts().map { it.toResponse() }

        val synchronizeData = SynchronizeResponse(
            customer = needUploadCustomers,
            store = needUploadStores,
            category = needUploadCategories,
            promo = needUploadPromo,
            payment = needUploadPayments,
            order = needUploadOrders,
            outcome = needUploadOutcomes,
            product = needUploadProducts,
            variant = needUploadVariants,
            orderDetail = needUploadOrderDetails,
            orderPayment = needUploadOrderPayments,
            orderPromo = needUploadOrderPromos,
            variantOption = needUploadVariantOptions,
            orderProductVariant = needUploadOrderProductVariants,
            variantProduct = needUploadVariantProducts
        )
        val response = service.synchronize(synchronizeData)

        // Update all un uploaded data that already uploaded
        if (needUploadCustomers.isNotEmpty()) {
            customersRepository.insertCustomers(needUploadCustomers.map { it.toEntity() })
        }
        if (needUploadStores.isNotEmpty()) {
            storeRepository.insertStores(needUploadStores.map { it.toEntity() })
        }
        if (needUploadCategories.isNotEmpty()) {
            categoriesRepository.insertCategories(needUploadCategories.map { it.toEntity() })
        }
        if (needUploadPromo.isNotEmpty()) {
            promosRepository.insertPromos(needUploadPromo.map { it.toEntity() })
        }
        if (needUploadPayments.isNotEmpty()) {
            paymentsRepository.insertPayments(needUploadPayments.map { it.toEntity() })
        }
        if (needUploadOrders.isNotEmpty()) {
            ordersRepository.insertOrders(needUploadOrders.map { it.toEntity() })
        }
        if (needUploadOutcomes.isNotEmpty()) {
            outcomesRepository.insertOutcomes(needUploadOutcomes.map { it.toEntity() })
        }
        if (needUploadProducts.isNotEmpty()) {
            productsRepository.insertProducts(needUploadProducts.map { it.toEntity() })
        }
        if (needUploadVariants.isNotEmpty()) {
            variantsRepository.insertVariants(needUploadVariants.map { it.toEntity() })
        }
        if (needUploadOrderDetails.isNotEmpty()) {
            ordersRepository.insertOrderDetails(needUploadOrderDetails.map { it.toEntity() })
        }
        if (needUploadOrderPayments.isNotEmpty()) {
            ordersRepository.insertOrderPayments(needUploadOrderPayments.map { it.toEntity() })
        }
        if (needUploadOrderPromos.isNotEmpty()) {
            ordersRepository.insertOrderPromos(needUploadOrderPromos.map { it.toEntity() })
        }
        if (needUploadVariantOptions.isNotEmpty()) {
            variantOptionsRepository.insertVariantOptions(needUploadVariantOptions.map { it.toEntity() })
        }
        if (needUploadOrderProductVariants.isNotEmpty()) {
            ordersRepository.insertOrderProductVariants(needUploadOrderProductVariants.map { it.toEntity() })
        }
        if (needUploadVariantProducts.isNotEmpty()) {
            productVariantsRepository.insertVariantProducts(needUploadVariantProducts.map { it.toEntity() })
        }

        // Insert all missing data that given by server
        val missingCustomer = response.data?.customer
        if (!missingCustomer.isNullOrEmpty()) {
            customersRepository.insertCustomers(missingCustomer.map { it.toEntity() })
        }
        val missingStore = response.data?.store
        if (!missingStore.isNullOrEmpty()) {
            storeRepository.insertStores(missingStore.map { it.toEntity() })
        }
        val missingCategories = response.data?.category
        if (!missingCategories.isNullOrEmpty()) {
            categoriesRepository.insertCategories(missingCategories.map { it.toEntity() })
        }
        val missingPromo = response.data?.promo
        if (!missingPromo.isNullOrEmpty()) {
            promosRepository.insertPromos(missingPromo.map { it.toEntity() })
        }
        val missingPayment = response.data?.payment
        if (!missingPayment.isNullOrEmpty()) {
            paymentsRepository.insertPayments(missingPayment.map { it.toEntity() })
        }
        val missingOrder = response.data?.order
        if (!missingOrder.isNullOrEmpty()) {
            ordersRepository.insertOrders(missingOrder.map { it.toEntity() })
        }
        val missingOutcome = response.data?.outcome
        if (!missingOutcome.isNullOrEmpty()) {
            outcomesRepository.insertOutcomes(missingOutcome.map { it.toEntity() })
        }
        val missingProduct = response.data?.product
        if (!missingProduct.isNullOrEmpty()) {
            productsRepository.insertProducts(missingProduct.map { it.toEntity() })
        }
        val missingVariant = response.data?.variant
        if (!missingVariant.isNullOrEmpty()) {
            variantsRepository.insertVariants(missingVariant.map { it.toEntity() })
        }
        val missingOrderDetail = response.data?.orderDetail
        if (!missingOrderDetail.isNullOrEmpty()) {
            ordersRepository.insertOrderDetails(missingOrderDetail.map { it.toEntity() })
        }
        val missingOrderPayment = response.data?.orderPayment
        if (!missingOrderPayment.isNullOrEmpty()) {
            ordersRepository.insertOrderPayments(missingOrderPayment.map { it.toEntity() })
        }
        val missingOrderPromo = response.data?.orderPromo
        if (!missingOrderPromo.isNullOrEmpty()) {
            ordersRepository.insertOrderPromos(missingOrderPromo.map { it.toEntity() })
        }
        val missingVariantOption = response.data?.variantOption
        if (!missingVariantOption.isNullOrEmpty()) {
            variantOptionsRepository.insertVariantOptions(missingVariantOption.map { it.toEntity() })
        }
        val missingOrderProductVariant = response.data?.orderProductVariant
        if (!missingOrderProductVariant.isNullOrEmpty()) {
            ordersRepository.insertOrderProductVariants(missingOrderProductVariant.map { it.toEntity() })
        }
        val missingVariantProduct = response.data?.variantProduct
        if (!missingVariantProduct.isNullOrEmpty()) {
            productVariantsRepository.insertVariantProducts(missingVariantProduct.map { it.toEntity() })
        }

        return true
    }
}
