package com.socialite.data.repository.impl

import com.socialite.data.database.dao.ProductsDao
import com.socialite.data.database.dao.PurchasesDao
import com.socialite.data.repository.PurchasesRepository
import com.socialite.data.schema.helper.PurchaseProductWithProduct
import com.socialite.data.schema.helper.PurchaseWithProduct
import kotlinx.coroutines.flow.flowOf

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

//    override fun getPurchaseProducts(purchaseNo: String) =
//        dao.getPurchasesProduct(purchaseNo)

    override fun getPurchaseProducts(purchaseNo: String) = flowOf(emptyList<PurchaseProductWithProduct>())

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
