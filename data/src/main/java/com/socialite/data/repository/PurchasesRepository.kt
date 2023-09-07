package com.socialite.data.repository

import com.socialite.data.schema.helper.PurchaseProductWithProduct
import com.socialite.data.schema.helper.PurchaseWithProduct
import com.socialite.data.schema.room.helper.PurchaseWithSupplier
import kotlinx.coroutines.flow.Flow

interface PurchasesRepository {

    fun getPurchases(): Flow<List<PurchaseWithSupplier>>
    fun getPurchaseProducts(purchaseNo: String): Flow<List<PurchaseProductWithProduct>>
    suspend fun newPurchase(data: PurchaseWithProduct)
}
