package com.socialite.solite_pos.data.repository.impl

import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.socialite.solite_pos.database.dao.ProductsDao
import com.socialite.solite_pos.database.dao.PurchasesDao
import com.socialite.solite_pos.data.repository.PurchasesRepository

class PurchasesRepositoryImpl(
    private val dao: PurchasesDao,
    private val productsDao: ProductsDao
) : PurchasesRepository {

    companion object {
        @Volatile
        private var INSTANCE: PurchasesRepositoryImpl? = null

        fun getInstance(
            dao: PurchasesDao,
            productsDao: ProductsDao
        ): PurchasesRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(PurchasesRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = PurchasesRepositoryImpl(dao, productsDao)
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
        data.products.forEach {
            it.purchaseProduct?.let { purchase ->
                productsDao.increaseProductStock(purchase.idProduct, purchase.amount)
            }
        }
    }
}
