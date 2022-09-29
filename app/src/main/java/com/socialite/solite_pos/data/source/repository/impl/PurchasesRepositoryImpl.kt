package com.socialite.solite_pos.data.source.repository.impl

import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.socialite.solite_pos.data.source.local.room.PurchasesDao
import com.socialite.solite_pos.data.source.repository.PurchasesRepository

class PurchasesRepositoryImpl(
    private val dao: PurchasesDao
) : PurchasesRepository {

    companion object {
        @Volatile
        private var INSTANCE: PurchasesRepositoryImpl? = null

        fun getInstance(
            dao: PurchasesDao
        ): PurchasesRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(PurchasesRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = PurchasesRepositoryImpl(dao = dao)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getPurchases() = dao.getPurchases()

    override fun getPurchaseProducts(purchaseNo: String) =
        dao.getPurchasesProduct(purchaseNo)

    override suspend fun newPurchase(data: PurchaseWithProduct) {
        dao.insertPurchase(data.purchase)
        dao.insertPurchaseProducts(data.purchaseProduct)
    }
}
