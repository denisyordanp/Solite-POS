package com.socialite.solite_pos.view.order_customer

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.GetOrdersGeneralMenuBadge
import com.socialite.solite_pos.data.source.domain.GetProductVariantOptions
import com.socialite.solite_pos.data.source.domain.GetProductWithCategories
import com.socialite.solite_pos.data.source.domain.NewOrder
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Customer
import com.socialite.solite_pos.data.source.local.entity.room.new_master.VariantOption
import com.socialite.solite_pos.data.source.repository.CustomersRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.view.factory.LoggedInViewModelFromFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar

class OrderCustomerViewModel(
    private val settingRepository: SettingRepository,
    private val productsRepository: ProductsRepository,
    private val customersRepository: CustomersRepository,
    private val getProductWithCategories: GetProductWithCategories,
    private val getProductVariantOptions: GetProductVariantOptions,
    private val newOrder: NewOrder,
    private val getOrdersGeneralMenuBadge: GetOrdersGeneralMenuBadge,
) : ViewModel() {

    companion object : LoggedInViewModelFromFactory<OrderCustomerViewModel>() {
        fun getOrderCustomerViewModel(activity: FragmentActivity): OrderCustomerViewModel {
            return buildViewModel(activity, OrderCustomerViewModel::class.java)
        }
    }

    private val _viewState = MutableStateFlow(OrderCustomerViewState.idle())
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            settingRepository.getNewSelectedStore()
                .combine(getProductWithCategories()) { selectedStore, categoryWithProducts ->
                    _viewState.value.copy(
                        allProducts = categoryWithProducts,
                        isShouldSelectStore = selectedStore.isEmpty() && categoryWithProducts.isNotEmpty()
                    )
                }
                .collect(_viewState)
        }
    }

    fun loadBadges(date: String) {
        viewModelScope.launch {
            getOrdersGeneralMenuBadge(date = date)
                .map {
                    _viewState.value.copy(
                        badges = it
                    )
                }
                .collect(_viewState)
        }
    }

    fun loadProductForSelectingVariants(productId: String) {
        viewModelScope.launch {
            getProductVariantOptions(productId)
                .combine(productsRepository.getProductById(productId)) { variants, product ->
                    _viewState.value.copy(
                        orderSelectVariantsState = OrderSelectVariantsState(
                            selectedProductVariantOptions = variants,
                            productOrderDetail = ProductOrderDetail.productNoVariant(product)
                        )
                    )
                }
                .collect(_viewState)
        }
    }

    fun clearProductForSelectingVariants() {
        viewModelScope.launch {
            _viewState.emit(
                _viewState.value.copy(
                    orderSelectVariantsState = OrderSelectVariantsState.idle()
                )
            )
        }
    }

    fun optionSelected(prevOption: VariantOption?, option: VariantOption, isSelected: Boolean) {
        viewModelScope.launch {
            val variantState = _viewState.value.orderSelectVariantsState
            val productDetailState = variantState.productOrderDetail
            _viewState.emit(
                _viewState.value.copy(
                    orderSelectVariantsState = variantState.copy(
                        productOrderDetail = productDetailState.copy(
                            variants = if (isSelected) {
                                productDetailState.addOption(option, prevOption)
                            } else {
                                productDetailState.removeOption(option)
                            }
                        )
                    )
                )
            )
        }
    }

    fun onAmountClicked(isAdd: Boolean) {
        viewModelScope.launch {
            val variantState = _viewState.value.orderSelectVariantsState
            val productDetailState = variantState.productOrderDetail
            val newAmount = productDetailState.amount.run {
                return@run if (isAdd) {
                    this + 1
                } else {
                    if (this == 1) this else this - 1
                }
            }

            _viewState.emit(
                _viewState.value.copy(
                    orderSelectVariantsState = variantState.copy(
                        productOrderDetail = productDetailState.copy(
                            amount = newAmount
                        )
                    )
                )
            )
        }
    }

    fun loadCustomers() {
        viewModelScope.launch {
            customersRepository.getCustomers()
                .map {
                    val customerState = _viewState.value.orderCustomerNameState
                    _viewState.value.copy(
                        orderCustomerNameState = customerState.copy(
                            customers = it
                        )
                    )
                }
                .collect(_viewState)
        }
    }

    fun newCustomer(data: Customer) {
        viewModelScope.launch {
            customersRepository.insertCustomer(data)
        }
    }

    fun addProductToBucket(detail: ProductOrderDetail) {
        viewModelScope.launch {
            val bucketState = _viewState.value.bucketOrderState
            val newBucket = if (bucketState.isIdle()) {
                BucketOrderState(
                    time = Calendar.getInstance().timeInMillis,
                    products = listOf(detail)
                )
            } else {
                val currentProducts = bucketState.products!!.toMutableList()
                val existingDetail = currentProducts.findExisting(detail)
                if (existingDetail != null) {
                    currentProducts.remove(existingDetail)
                    currentProducts.add(
                        existingDetail.copy(
                            amount = existingDetail.amount + 1
                        )
                    )
                } else {
                    currentProducts.add(detail)
                }

                bucketState.copy(
                    products = currentProducts
                )
            }

            _viewState.emit(
                _viewState.value.copy(
                    bucketOrderState = newBucket
                )
            )
        }
    }

    fun decreaseProduct(detail: ProductOrderDetail) {
        viewModelScope.launch {
            val bucketState = _viewState.value.bucketOrderState
            val currentProducts = bucketState.products?.toMutableList()

            val existingDetail = currentProducts?.findExisting(detail)
            if (existingDetail != null) {
                currentProducts.remove(existingDetail)
                if (existingDetail.amount > 1) {
                    currentProducts.add(
                        existingDetail.copy(
                            amount = existingDetail.amount - 1
                        )
                    )
                }
            }

            if (currentProducts.isNullOrEmpty()) {
                _viewState.emit(
                    _viewState.value.copy(
                        bucketOrderState = BucketOrderState.idle()
                    )
                )
            } else {
                _viewState.emit(
                    _viewState.value.copy(
                        bucketOrderState = bucketState.copy(
                            products = currentProducts
                        )
                    )
                )
            }
        }
    }

    fun removeProductFromBucket(detail: ProductOrderDetail) {
        viewModelScope.launch {
            val bucketState = _viewState.value.bucketOrderState
            val currentProducts = bucketState.products?.toMutableList()

            val existingDetail = currentProducts?.findExisting(detail)
            if (existingDetail != null) {
                currentProducts.remove(existingDetail)
            }

            if (currentProducts.isNullOrEmpty()) {
                _viewState.emit(
                    _viewState.value.copy(
                        bucketOrderState = BucketOrderState.idle()
                    )
                )
            } else {
                _viewState.emit(
                    _viewState.value.copy(
                        bucketOrderState = bucketState.copy(
                            products = currentProducts
                        )
                    )
                )
            }
        }
    }

    fun newOrder(
        customer: Customer,
        isTakeAway: Boolean,
    ) {
        viewModelScope.launch {
            _viewState.value.bucketOrderState.products?.let {
                newOrder.invoke(
                    customer = customer,
                    isTakeAway = isTakeAway,
                    products = it,
                    currentTime = DateUtils.currentDateTime
                )
            }
        }
    }

    private fun List<ProductOrderDetail>.findExisting(compare: ProductOrderDetail): ProductOrderDetail? {
        return this.find {
            it.product == compare.product &&
                    it.variants == compare.variants
        }
    }
}
