package com.socialite.solite_pos.view.screens.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.domain.domain.GetOrderWithProduct
import com.socialite.domain.domain.UpdateOrderProducts
import com.socialite.domain.schema.helper.BucketOrder
import com.socialite.domain.schema.helper.ProductOrderDetail
import com.socialite.domain.schema.helper.findExisting
import com.socialite.data.preference.SettingPreferences
import com.socialite.domain.helper.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val updateOrderProducts: UpdateOrderProducts,
    private val getOrderWithProduct: GetOrderWithProduct,
    private val settingPreferences: SettingPreferences
) : ViewModel() {

    private val _viewState = MutableStateFlow(OrdersViewState.idle())
    val viewState = _viewState.asStateFlow()

    val defaultPrinterAddress get() = settingPreferences.printerDevice

    fun setDefaultPage(page: Int) {
        viewModelScope.launch {
            _viewState.emit(
                _viewState.value.copy(
                    defaultTabPage = page
                )
            )
        }
    }

    fun createBucketForEdit(orderId: String) {
        viewModelScope.launch {
            val orderWithProduct = getOrderWithProduct(orderId).first()
            orderWithProduct?.let {

                val newBucket = BucketOrder(
                    time = DateUtils.strToDate(it.orderData.order.orderTime).time,
                    products = it.products
                )

                _viewState.emit(
                    _viewState.value.copy(
                        bucketOrder = newBucket
                    )
                )
            }
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

    fun updateOrderProducts(orderId: String) {
        viewModelScope.launch {
            _viewState.value.bucketOrder.products?.let {
                updateOrderProducts.invoke(orderId, it)
            }
        }
    }
}
