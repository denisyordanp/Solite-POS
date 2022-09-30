package com.socialite.solite_pos.data.source.repository

import androidx.lifecycle.LiveData
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.User
import com.socialite.solite_pos.vo.Resource

internal interface SoliteDataSource {
    fun updateOrder(order: Order)
    fun replaceProductOrder(old: OrderWithProduct, new: OrderWithProduct)

    fun getUsers(userId: String): LiveData<Resource<User?>>
}
