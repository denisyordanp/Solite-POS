package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.Synchronize
import com.socialite.solite_pos.data.source.remote.SoliteServices
import com.socialite.solite_pos.data.source.remote.response.entity.SynchronizeParamItem
import com.socialite.solite_pos.data.source.remote.response.entity.SynchronizeParams
import com.socialite.solite_pos.data.source.repository.CategoriesRepository
import com.socialite.solite_pos.data.source.repository.CustomersRepository
import com.socialite.solite_pos.data.source.repository.OrderDetailsRepository
import com.socialite.solite_pos.data.source.repository.OrderPaymentsRepository
import com.socialite.solite_pos.data.source.repository.OrderProductVariantsRepository
import com.socialite.solite_pos.data.source.repository.OrderPromosRepository
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.data.source.repository.PromosRepository
import com.socialite.solite_pos.data.source.repository.StoreRepository
import com.socialite.solite_pos.data.source.repository.VariantOptionsRepository
import com.socialite.solite_pos.data.source.repository.VariantsRepository
import com.socialite.solite_pos.di.AuthorizationService
import javax.inject.Inject

class SynchronizeImpl @Inject constructor(
    private val customersRepository: CustomersRepository,
    private val storeRepository: StoreRepository,
    private val categoriesRepository: CategoriesRepository,
    private val promosRepository: PromosRepository,
    private val paymentsRepository: PaymentsRepository,
    private val ordersRepository: OrdersRepository,
    private val outcomesRepository: OutcomesRepository,
    private val productsRepository: ProductsRepository,
    private val variantsRepository: VariantsRepository,
    private val orderDetailsRepository: OrderDetailsRepository,
    private val orderPaymentsRepository: OrderPaymentsRepository,
    private val orderPromosRepository: OrderPromosRepository,
    private val variantOptionsRepository: VariantOptionsRepository,
    private val orderProductVariantsRepository: OrderProductVariantsRepository,
    private val productVariantsRepository: ProductVariantsRepository,
    @AuthorizationService private val service: SoliteServices
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
        val needUploadOrderDetails = SynchronizeParamItem(
            deletedItems = orderDetailsRepository.getDeletedOrderDetailIds(),
            items = orderDetailsRepository.getNeedUploadOrderDetails().map { it.toResponse() }
        )
        val needUploadOrderPayments =
            orderPaymentsRepository.getNeedUploadOrderPayments().map { it.toResponse() }
        val needUploadOrderPromos =
            orderPromosRepository.getNeedUploadOrderPromos().map { it.toResponse() }
        val needUploadVariantOptions =
            variantOptionsRepository.getNeedUploadVariantOptions().map { it.toResponse() }
        val needUploadOrderProductVariants = SynchronizeParamItem(
            deletedItems = orderProductVariantsRepository.getDeletedOrderProductVariantIds(),
            items = orderProductVariantsRepository.getNeedUploadOrderProductVariants()
                .map { it.toResponse() }
        )
        val needUploadVariantProducts = SynchronizeParamItem(
            deletedItems = productVariantsRepository.getProductVariantIds(),
            items = productVariantsRepository.getNeedUploadVariantProducts().map { it.toResponse() }
        )

        val synchronizeData = SynchronizeParams(
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
            customersRepository.updateItems(
                needUploadCustomers.map { it.toEntity() }
            )
        }
        if (needUploadStores.isNotEmpty()) {
            storeRepository.updateItems(needUploadStores.map { it.toEntity() })
        }
        if (needUploadCategories.isNotEmpty()) {
            categoriesRepository.updateItems(needUploadCategories.map { it.toEntity() })
        }
        if (needUploadPromo.isNotEmpty()) {
            promosRepository.updateItems(needUploadPromo.map { it.toEntity() })
        }
        if (needUploadPayments.isNotEmpty()) {
            paymentsRepository.updateItems(needUploadPayments.map { it.toEntity() })
        }
        if (needUploadOrders.isNotEmpty()) {
            ordersRepository.updateItems(needUploadOrders.map { it.toEntity() })
        }
        if (needUploadOutcomes.isNotEmpty()) {
            outcomesRepository.updateItems(needUploadOutcomes.map { it.toEntity() })
        }
        if (needUploadProducts.isNotEmpty()) {
            productsRepository.updateItems(needUploadProducts.map { it.toEntity() })
        }
        if (needUploadVariants.isNotEmpty()) {
            variantsRepository.updateItems(needUploadVariants.map { it.toEntity() })
        }
        if (needUploadOrderDetails.items.isNotEmpty()) {
            orderDetailsRepository.updateItems(needUploadOrderDetails.items.map { it.toEntity() })
        }
        if (needUploadOrderPayments.isNotEmpty()) {
            orderPaymentsRepository.updateItems(needUploadOrderPayments.map { it.toEntity() })
        }
        if (needUploadOrderPromos.isNotEmpty()) {
            orderPromosRepository.updateItems(needUploadOrderPromos.map { it.toEntity() })
        }
        if (needUploadVariantOptions.isNotEmpty()) {
            variantOptionsRepository.updateItems(needUploadVariantOptions.map { it.toEntity() })
        }
        if (needUploadOrderProductVariants.items.isNotEmpty()) {
            orderProductVariantsRepository.updateItems(needUploadOrderProductVariants.items.map { it.toEntity() })
        }
        if (needUploadVariantProducts.items.isNotEmpty()) {
            productVariantsRepository.updateItems(needUploadVariantProducts.items.map { it.toEntity() })
        }

        // Insert all missing data that given by server
        val data = response.data
        customersRepository.updateSynchronization(data?.customer?.map { it.toEntity() })
        storeRepository.updateSynchronization(data?.store?.map { it.toEntity() })
        categoriesRepository.updateSynchronization(data?.category?.map { it.toEntity() })
        promosRepository.updateSynchronization(data?.promo?.map { it.toEntity() })
        paymentsRepository.updateSynchronization(data?.payment?.map { it.toEntity() })
        ordersRepository.updateSynchronization(data?.order?.map { it.toEntity() })
        outcomesRepository.updateSynchronization(data?.outcome?.map { it.toEntity() })
        productsRepository.updateSynchronization(data?.product?.map { it.toEntity() })
        variantsRepository.updateSynchronization(data?.variant?.map { it.toEntity() })
        orderDetailsRepository.updateSynchronization(data?.orderDetail?.map { it.toEntity() })
        orderPaymentsRepository.updateSynchronization(data?.orderPayment?.map { it.toEntity() })
        orderPromosRepository.updateSynchronization(data?.orderPromo?.map { it.toEntity() })
        variantOptionsRepository.updateSynchronization(data?.variantOption?.map { it.toEntity() })
        orderProductVariantsRepository.updateSynchronization(data?.orderProductVariant?.map { it.toEntity() })
        productVariantsRepository.updateSynchronization(data?.variantProduct?.map { it.toEntity() })

        // Delete the deleted items
        if (needUploadOrderDetails.deletedItems.isNotEmpty()) {
            orderDetailsRepository.deleteAllDeletedOrderDetails()
        }
        if (needUploadOrderProductVariants.deletedItems.isNotEmpty()) {
            orderProductVariantsRepository.deleteAllDeletedOrderProductVariants()
        }
        if (needUploadOrderProductVariants.deletedItems.isNotEmpty()) {
            productVariantsRepository.deleteAllDeletedProductVariants()
        }

        return true
    }
}
