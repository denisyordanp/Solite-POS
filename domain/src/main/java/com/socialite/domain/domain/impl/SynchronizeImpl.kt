package com.socialite.domain.domain.impl

import com.socialite.common.utility.di.DefaultDispatcher
import com.socialite.common.utility.extension.dataStateFlow
import com.socialite.common.utility.state.DataState
import com.socialite.common.utility.state.ErrorState
import com.socialite.core.network.response.ApiResponse
import com.socialite.data.repository.CategoriesRepository
import com.socialite.data.repository.CustomersRepository
import com.socialite.data.repository.OrderDetailsRepository
import com.socialite.data.repository.OrderPaymentsRepository
import com.socialite.data.repository.OrderProductVariantsRepository
import com.socialite.data.repository.OrderPromosRepository
import com.socialite.data.repository.OrdersRepository
import com.socialite.data.repository.OutcomesRepository
import com.socialite.data.repository.PaymentsRepository
import com.socialite.data.repository.ProductVariantsRepository
import com.socialite.data.repository.ProductsRepository
import com.socialite.data.repository.PromosRepository
import com.socialite.data.repository.StoreRepository
import com.socialite.data.repository.SynchronizeRepository
import com.socialite.data.repository.UserRepository
import com.socialite.data.repository.VariantOptionsRepository
import com.socialite.data.repository.VariantsRepository
import com.socialite.data.schema.response.SynchronizeParamItem
import com.socialite.data.schema.response.SynchronizeParams
import com.socialite.data.schema.response.SynchronizeResponse
import com.socialite.domain.domain.FetchLoggedInUser
import com.socialite.domain.domain.Synchronize
import com.socialite.domain.mapper.toCategoryResponse
import com.socialite.domain.mapper.toCustomerResponse
import com.socialite.domain.mapper.toOrderDetailResponse
import com.socialite.domain.mapper.toOrderPaymentResponse
import com.socialite.domain.mapper.toOrderProductVariantResponse
import com.socialite.domain.mapper.toOrderPromoResponse
import com.socialite.domain.mapper.toOrderResponse
import com.socialite.domain.mapper.toOutcomeResponse
import com.socialite.domain.mapper.toPaymentResponse
import com.socialite.domain.mapper.toProductResponse
import com.socialite.domain.mapper.toPromoResponse
import com.socialite.domain.mapper.toStoreResponse
import com.socialite.domain.mapper.toVariantOptionResponse
import com.socialite.domain.mapper.toVariantProductResponse
import com.socialite.domain.mapper.toVariantResponse
import com.socialite.schema.ui.main.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
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
    private val userRepository: UserRepository,
    private val synchronizeRepository: SynchronizeRepository,
    private val fetchLoggedInUser: FetchLoggedInUser,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : Synchronize {
    @OptIn(FlowPreview::class)
    override fun invoke() = fetchLoggedInUser().flatMapConcat<DataState<User>, DataState<Boolean>> {
        when (it) {
            is DataState.Error -> flowOf(DataState.Error(it.errorState))
            DataState.Idle -> flowOf(DataState.Idle)
            DataState.Loading -> flowOf(DataState.Loading)
            is DataState.Success -> flow {
                if (it.data.isUserActive) {
                    val synchronizeParam = createSynchronizeParams()
                    emitAll(beginSynchronize(synchronizeParam))
                } else {
                    emit(DataState.Error(ErrorState.DeactivatedAccount))
                }
            }
        }
    }.flowOn(dispatcher)

    @OptIn(FlowPreview::class)
    private fun beginSynchronize(params: SynchronizeParams): Flow<DataState<Boolean>> {
        return dataStateFlow(dispatcher) {
            synchronizeRepository.synchronize(params)
        }.flatMapConcat<DataState<ApiResponse<SynchronizeResponse>>, DataState<Boolean>> {
            when (it) {
                is DataState.Error -> flowOf(DataState.Error(it.errorState))
                DataState.Idle -> flowOf(DataState.Idle)
                DataState.Loading -> flowOf(DataState.Loading)
                is DataState.Success -> flow {
                    val response = it.data
                    // Update all un uploaded data that already uploaded
                    updateAllUnUploadedDataAfterUploaded(params)

                    // Insert all missing data that given by server
                    insertAllMissingDataFromServer(response.data)

                    // Delete the deleted items
                    deleteAllDeletedItems(params)

                    emit(DataState.Success(true))
                }
            }
        }
    }

    private suspend fun createSynchronizeParams(): SynchronizeParams {
        val needUploadCustomers =
            customersRepository.getNeedUploadCustomers().map { it.toCustomerResponse() }
        val needUploadStores = storeRepository.getNeedUploadStores().map { it.toStoreResponse() }
        val needUploadCategories =
            categoriesRepository.getNeedUploadCategories().map { it.toCategoryResponse() }
        val needUploadPromo = promosRepository.getNeedUploadPromos().map { it.toPromoResponse() }
        val needUploadPayments = paymentsRepository.getNeedUploadPayments().map { it.toPaymentResponse() }
        val needUploadOrders = ordersRepository.getNeedUploadOrders().map { it.toOrderResponse() }
        val needUploadOutcomes = outcomesRepository.getNeedUploadOutcomes().map { it.toOutcomeResponse() }
        val needUploadProducts = productsRepository.getNeedUploadProducts().map { it.toProductResponse() }
        val needUploadVariants = variantsRepository.getNeedUploadVariants().map { it.toVariantResponse() }
        val needUploadOrderDetails = SynchronizeParamItem(
            deletedItems = orderDetailsRepository.getDeletedOrderDetailIds(),
            items = orderDetailsRepository.getNeedUploadOrderDetails().map { it.toOrderDetailResponse() }
        )
        val needUploadOrderPayments =
            orderPaymentsRepository.getNeedUploadOrderPayments().map { it.toOrderPaymentResponse() }
        val needUploadOrderPromos =
            orderPromosRepository.getNeedUploadOrderPromos().map { it.toOrderPromoResponse() }
        val needUploadVariantOptions =
            variantOptionsRepository.getNeedUploadVariantOptions().map { it.toVariantOptionResponse() }
        val needUploadOrderProductVariants = SynchronizeParamItem(
            deletedItems = orderProductVariantsRepository.getDeletedOrderProductVariantIds(),
            items = orderProductVariantsRepository.getNeedUploadOrderProductVariants()
                .map { it.toOrderProductVariantResponse() }
        )
        val needUploadVariantProducts = SynchronizeParamItem(
            deletedItems = productVariantsRepository.getProductVariantIds(),
            items = productVariantsRepository.getNeedUploadVariantProducts().map { it.toVariantProductResponse() }
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
        data: SynchronizeResponse?
    ) {
        userRepository.updateSynchronization(data?.user?.map { it.toEntity() })
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
