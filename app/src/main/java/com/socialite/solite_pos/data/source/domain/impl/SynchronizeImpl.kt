package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.Synchronize
import com.socialite.solite_pos.data.source.remote.SoliteServices
import com.socialite.solite_pos.data.source.remote.response.entity.SynchronizeParamItem
import com.socialite.solite_pos.data.source.remote.response.entity.SynchronizeParams
import com.socialite.solite_pos.data.source.remote.response.entity.SynchronizeResponse
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
        val synchronizeParam = createSynchronizeParams(
            customersRepository = customersRepository,
            storeRepository = storeRepository,
            categoriesRepository = categoriesRepository,
            promosRepository = promosRepository,
            paymentsRepository = paymentsRepository,
            ordersRepository = ordersRepository,
            outcomesRepository = outcomesRepository,
            productsRepository = productsRepository,
            variantsRepository = variantsRepository,
            orderDetailsRepository = orderDetailsRepository,
            orderPaymentsRepository = orderPaymentsRepository,
            orderPromosRepository = orderPromosRepository,
            variantOptionsRepository = variantOptionsRepository,
            orderProductVariantsRepository = orderProductVariantsRepository,
            productVariantsRepository = productVariantsRepository
        )
        val response = service.synchronize(synchronizeParam)

        // Update all un uploaded data that already uploaded
        updateAllUnUploadedDataAfterUploaded(
            customersRepository = customersRepository,
            storeRepository = storeRepository,
            categoriesRepository = categoriesRepository,
            promosRepository = promosRepository,
            paymentsRepository = paymentsRepository,
            ordersRepository = ordersRepository,
            outcomesRepository = outcomesRepository,
            productsRepository = productsRepository,
            variantsRepository = variantsRepository,
            orderDetailsRepository = orderDetailsRepository,
            orderPaymentsRepository = orderPaymentsRepository,
            orderPromosRepository = orderPromosRepository,
            variantOptionsRepository = variantOptionsRepository,
            orderProductVariantsRepository = orderProductVariantsRepository,
            productVariantsRepository = productVariantsRepository,
            synchronizeParams = synchronizeParam
        )

        // Insert all missing data that given by server
        insertAllMissingDataFromServer(
            customersRepository = customersRepository,
            storeRepository = storeRepository,
            categoriesRepository = categoriesRepository,
            promosRepository = promosRepository,
            paymentsRepository = paymentsRepository,
            ordersRepository = ordersRepository,
            outcomesRepository = outcomesRepository,
            productsRepository = productsRepository,
            variantsRepository = variantsRepository,
            orderDetailsRepository = orderDetailsRepository,
            orderPaymentsRepository = orderPaymentsRepository,
            orderPromosRepository = orderPromosRepository,
            variantOptionsRepository = variantOptionsRepository,
            orderProductVariantsRepository = orderProductVariantsRepository,
            productVariantsRepository = productVariantsRepository,
            data = response.data
        )

        // Delete the deleted items
        deleteAllDeletedItems(
            orderDetailsRepository = orderDetailsRepository,
            orderProductVariantsRepository = orderProductVariantsRepository,
            synchronizeParams = synchronizeParam
        )

        return true
    }

    private suspend fun createSynchronizeParams(
        customersRepository: CustomersRepository,
        storeRepository: StoreRepository,
        categoriesRepository: CategoriesRepository,
        promosRepository: PromosRepository,
        paymentsRepository: PaymentsRepository,
        ordersRepository: OrdersRepository,
        outcomesRepository: OutcomesRepository,
        productsRepository: ProductsRepository,
        variantsRepository: VariantsRepository,
        orderDetailsRepository: OrderDetailsRepository,
        orderPaymentsRepository: OrderPaymentsRepository,
        orderPromosRepository: OrderPromosRepository,
        variantOptionsRepository: VariantOptionsRepository,
        orderProductVariantsRepository: OrderProductVariantsRepository,
        productVariantsRepository: ProductVariantsRepository,
    ): SynchronizeParams {
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

        return SynchronizeParams(
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
    }

    private suspend fun updateAllUnUploadedDataAfterUploaded(
        customersRepository: CustomersRepository,
        storeRepository: StoreRepository,
        categoriesRepository: CategoriesRepository,
        promosRepository: PromosRepository,
        paymentsRepository: PaymentsRepository,
        ordersRepository: OrdersRepository,
        outcomesRepository: OutcomesRepository,
        productsRepository: ProductsRepository,
        variantsRepository: VariantsRepository,
        orderDetailsRepository: OrderDetailsRepository,
        orderPaymentsRepository: OrderPaymentsRepository,
        orderPromosRepository: OrderPromosRepository,
        variantOptionsRepository: VariantOptionsRepository,
        orderProductVariantsRepository: OrderProductVariantsRepository,
        productVariantsRepository: ProductVariantsRepository,
        synchronizeParams: SynchronizeParams
    ) {
        if (synchronizeParams.customer.isNotEmpty()) {
            customersRepository.updateItems(
                synchronizeParams.customer.map { it.toEntity() }
            )
        }
        if (synchronizeParams.store.isNotEmpty()) {
            storeRepository.updateItems(synchronizeParams.store.map { it.toEntity() })
        }
        if (synchronizeParams.category.isNotEmpty()) {
            categoriesRepository.updateItems(synchronizeParams.category.map { it.toEntity() })
        }
        if (synchronizeParams.promo.isNotEmpty()) {
            promosRepository.updateItems(synchronizeParams.promo.map { it.toEntity() })
        }
        if (synchronizeParams.payment.isNotEmpty()) {
            paymentsRepository.updateItems(synchronizeParams.payment.map { it.toEntity() })
        }
        if (synchronizeParams.order.isNotEmpty()) {
            ordersRepository.updateItems(synchronizeParams.order.map { it.toEntity() })
        }
        if (synchronizeParams.outcome.isNotEmpty()) {
            outcomesRepository.updateItems(synchronizeParams.outcome.map { it.toEntity() })
        }
        if (synchronizeParams.product.isNotEmpty()) {
            productsRepository.updateItems(synchronizeParams.product.map { it.toEntity() })
        }
        if (synchronizeParams.variant.isNotEmpty()) {
            variantsRepository.updateItems(synchronizeParams.variant.map { it.toEntity() })
        }
        if (synchronizeParams.orderDetail.items.isNotEmpty()) {
            orderDetailsRepository.updateItems(synchronizeParams.orderDetail.items.map { it.toEntity() })
        }
        if (synchronizeParams.orderPayment.isNotEmpty()) {
            orderPaymentsRepository.updateItems(synchronizeParams.orderPayment.map { it.toEntity() })
        }
        if (synchronizeParams.orderPromo.isNotEmpty()) {
            orderPromosRepository.updateItems(synchronizeParams.orderPromo.map { it.toEntity() })
        }
        if (synchronizeParams.variantOption.isNotEmpty()) {
            variantOptionsRepository.updateItems(synchronizeParams.variantOption.map { it.toEntity() })
        }
        if (synchronizeParams.orderProductVariant.items.isNotEmpty()) {
            orderProductVariantsRepository.updateItems(synchronizeParams.orderProductVariant.items.map { it.toEntity() })
        }
        if (synchronizeParams.variantProduct.items.isNotEmpty()) {
            productVariantsRepository.updateItems(synchronizeParams.variantProduct.items.map { it.toEntity() })
        }
    }

    private suspend fun insertAllMissingDataFromServer(
        customersRepository: CustomersRepository,
        storeRepository: StoreRepository,
        categoriesRepository: CategoriesRepository,
        promosRepository: PromosRepository,
        paymentsRepository: PaymentsRepository,
        ordersRepository: OrdersRepository,
        outcomesRepository: OutcomesRepository,
        productsRepository: ProductsRepository,
        variantsRepository: VariantsRepository,
        orderDetailsRepository: OrderDetailsRepository,
        orderPaymentsRepository: OrderPaymentsRepository,
        orderPromosRepository: OrderPromosRepository,
        variantOptionsRepository: VariantOptionsRepository,
        orderProductVariantsRepository: OrderProductVariantsRepository,
        productVariantsRepository: ProductVariantsRepository,
        data: SynchronizeResponse?
    ) {
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
    }

    private suspend fun deleteAllDeletedItems(
        orderDetailsRepository: OrderDetailsRepository,
        orderProductVariantsRepository: OrderProductVariantsRepository,
        synchronizeParams: SynchronizeParams
    ) {
        if (synchronizeParams.orderDetail.deletedItems.isNotEmpty()) {
            orderDetailsRepository.deleteAllDeletedOrderDetails()
        }
        if (synchronizeParams.orderProductVariant.deletedItems.isNotEmpty()) {
            orderProductVariantsRepository.deleteAllDeletedOrderProductVariants()
        }
        if (synchronizeParams.variantProduct.deletedItems.isNotEmpty()) {
            productVariantsRepository.deleteAllDeletedProductVariants()
        }
    }
}
