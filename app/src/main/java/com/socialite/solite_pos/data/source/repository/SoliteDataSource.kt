package com.socialite.solite_pos.data.source.repository

import androidx.lifecycle.LiveData
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseProductWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.VariantWithOptions
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.helper.PurchaseWithSupplier
import com.socialite.solite_pos.data.source.local.entity.room.helper.VariantWithVariantMix
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.User
import com.socialite.solite_pos.data.source.remote.response.helper.ApiResponse
import com.socialite.solite_pos.vo.Resource

internal interface SoliteDataSource {

    fun getOrderList(status: Int, date: String): LiveData<Resource<List<OrderData>>>
    fun getLocalOrders(status: Int, date: String): LiveData<List<OrderData>>
    fun getProductOrder(orderNo: String): LiveData<Resource<List<ProductOrderDetail>>>

    fun newOrder(order: OrderWithProduct)
    fun updateOrder(order: Order)
    fun replaceProductOrder(old: OrderWithProduct, new: OrderWithProduct)

    val purchases: LiveData<Resource<List<PurchaseWithSupplier>>>
    fun getPurchaseProducts(purchaseNo: String): LiveData<Resource<List<PurchaseProductWithProduct>>>
    fun newPurchase(data: PurchaseWithProduct, callback: (ApiResponse<Boolean>) -> Unit)

    fun getProductVariantOptions(idProduct: Long): LiveData<Resource<List<VariantWithOptions>?>>
    fun getVariantProduct(
        idProduct: Long,
        idVariantOption: Long
    ): LiveData<Resource<VariantProduct?>>

    fun getVariantProductById(idProduct: Long): LiveData<Resource<VariantProduct?>>
    fun insertVariantProduct(data: VariantProduct, callback: (ApiResponse<Long>) -> Unit)
    fun removeVariantProduct(data: VariantProduct, callback: (ApiResponse<Boolean>) -> Unit)

    fun getVariantMixProductById(idVariant: Long, idProduct: Long): LiveData<Resource<VariantMix?>>
    fun getVariantMixProduct(idVariant: Long): LiveData<Resource<VariantWithVariantMix>>
    fun insertVariantMix(data: VariantMix, callback: (ApiResponse<Long>) -> Unit)
    fun removeVariantMix(data: VariantMix, callback: (ApiResponse<Boolean>) -> Unit)

    fun getUsers(userId: String): LiveData<Resource<User?>>
}
