package com.socialite.solite_pos.data.repository

import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseProductWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.socialite.solite_pos.data.schema.room.helper.PurchaseWithSupplier
import kotlinx.coroutines.flow.Flow

interface PurchasesRepository {

    fun getPurchases(): Flow<List<PurchaseWithSupplier>>
    fun getPurchaseProducts(purchaseNo: String): Flow<List<PurchaseProductWithProduct>>
    suspend fun newPurchase(data: PurchaseWithProduct)
}
