package com.socialite.solite_pos.view.orders

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.GetOrderWithProduct
import com.socialite.solite_pos.data.source.domain.UpdateOrderProducts
import com.socialite.solite_pos.data.source.local.entity.helper.BucketOrder
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.helper.findExisting
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection
import com.socialite.solite_pos.utils.config.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

class OrdersViewModel(
    private val updateOrderProducts: UpdateOrderProducts,
    private val getOrderWithProduct: GetOrderWithProduct,
) : ViewModel() {

    private val _viewState = MutableStateFlow(OrdersViewState.idle())
    val viewState = _viewState.asStateFlow()

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
                    time = DateUtils.strToDate(it.order.order.orderTime).time,
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

    companion object {
        fun getFactory(activity: FragmentActivity) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return OrdersViewModel(
                    updateOrderProducts = LoggedInDomainInjection.provideUpdateOrderProducts(
                        activity
                    ),
                    getOrderWithProduct = LoggedInDomainInjection.provideGetOrderWithProduct(
                        activity
                    ),
                ) as T
            }
        }
    }
}
