package com.socialite.solite_pos.data.source.repository

import androidx.lifecycle.LiveData
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.socialite.solite_pos.data.source.local.entity.room.helper.VariantWithVariantMix
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.User
import com.socialite.solite_pos.data.source.remote.response.helper.ApiResponse
import com.socialite.solite_pos.vo.Resource

internal interface SoliteDataSource {
    fun updateOrder(order: Order)
    fun replaceProductOrder(old: OrderWithProduct, new: OrderWithProduct)

    fun getVariantMixProductById(idVariant: Long, idProduct: Long): LiveData<Resource<VariantMix?>>
    fun getVariantMixProduct(idVariant: Long): LiveData<Resource<VariantWithVariantMix>>
    fun insertVariantMix(data: VariantMix, callback: (ApiResponse<Long>) -> Unit)
    fun removeVariantMix(data: VariantMix, callback: (ApiResponse<Boolean>) -> Unit)

    fun getUsers(userId: String): LiveData<Resource<User?>>
}
