package com.socialite.solite_pos.view.screens.order_customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.domain.GetProductWithCategories
import com.socialite.solite_pos.data.domain.NewOrder
import com.socialite.solite_pos.data.schema.helper.BucketOrder
import com.socialite.solite_pos.data.schema.helper.ProductOrderDetail
import com.socialite.solite_pos.data.schema.helper.findExisting
import com.socialite.solite_pos.data.schema.room.new_master.Customer
import com.socialite.solite_pos.data.repository.SettingRepository
import com.socialite.solite_pos.utils.config.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class OrderCustomerViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
    private val getProductWithCategories: GetProductWithCategories,
    private val newOrder: NewOrder,
) : ViewModel() {

    private val _viewState = MutableStateFlow(OrderCustomerViewState.idle())
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            settingRepository.getNewSelectedStore()
                .combine(getProductWithCategories()) { selectedStore, categoryWithProducts ->
                    _viewState.value.copy(
                        isShouldSelectStore = selectedStore.isEmpty() && categoryWithProducts.isNotEmpty()
                    )
                }
                .collect(_viewState)
        }
    }

    fun addProductToBucket(detail: ProductOrderDetail) {
        viewModelScope.launch {
            val bucketState = _viewState.value.bucketOrder
            val newBucket = if (bucketState.isIdle()) {
                BucketOrder(
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
                    bucketOrder = newBucket
                )
            )
        }
    }

    fun decreaseProduct(detail: ProductOrderDetail) {
        viewModelScope.launch {
            val bucketState = _viewState.value.bucketOrder
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
                        bucketOrder = BucketOrder.idle()
                    )
                )
            } else {
                _viewState.emit(
                    _viewState.value.copy(
                        bucketOrder = bucketState.copy(
                            products = currentProducts
                        )
                    )
                )
            }
        }
    }

    fun removeProductFromBucket(detail: ProductOrderDetail) {
        viewModelScope.launch {
            val bucketState = _viewState.value.bucketOrder
            val currentProducts = bucketState.products?.toMutableList()

            val existingDetail = currentProducts?.findExisting(detail)
            if (existingDetail != null) {
                currentProducts.remove(existingDetail)
            }

            if (currentProducts.isNullOrEmpty()) {
                _viewState.emit(
                    _viewState.value.copy(
                        bucketOrder = BucketOrder.idle()
                    )
                )
            } else {
                _viewState.emit(
                    _viewState.value.copy(
                        bucketOrder = bucketState.copy(
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
            _viewState.value.bucketOrder.products?.let {
                newOrder.invoke(
                    customer = customer,
                    isTakeAway = isTakeAway,
                    products = it,
                    currentTime = DateUtils.currentDateTime
                )
            }
        }
    }
}
