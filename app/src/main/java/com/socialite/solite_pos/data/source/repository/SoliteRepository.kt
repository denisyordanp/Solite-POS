package com.socialite.solite_pos.data.source.repository

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.socialite.solite_pos.data.NetworkBoundResource
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderMixProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariantMix
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.User
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.SoliteDao
import com.socialite.solite_pos.data.source.remote.response.entity.BatchWithData
import com.socialite.solite_pos.data.source.remote.response.entity.BatchWithObject
import com.socialite.solite_pos.utils.database.AppExecutors
import com.socialite.solite_pos.utils.tools.helper.Resource

class SoliteRepository private constructor(
    private val appExecutors: AppExecutors,
    private val soliteDao: SoliteDao
) : SoliteDataSource {

    companion object {
        @Volatile
        private var INSTANCE: SoliteRepository? = null

        fun getInstance(
            appExecutors: AppExecutors,
            soliteDao: SoliteDao
        ): SoliteRepository {
            if (INSTANCE == null) {
                synchronized(SoliteRepository::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = SoliteRepository(appExecutors, soliteDao)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun replaceProductOrder(
        old: OrderWithProduct,
        new: OrderWithProduct
    ) {
        updateOrder(new.order.order)
        deleteOrderProduct(old)
        insertOrderProduct(new)
    }

    override fun updateOrder(order: Order) {
        soliteDao.updateOrder(order)
    }

    private fun deleteOrderProduct(orderProduct: OrderWithProduct) {
        for (item in orderProduct.products) {
            if (item.product != null) {

                val detail = soliteDao.getDetailOrders(
                    orderProduct.order.order.orderNo,
                    item.product!!.id
                )

                if (item.product!!.isMix) {
                    for (p in item.mixProducts) {

                        increaseStock(p.product.id, p.amount)

                        val variantMix = soliteDao.getOrderProductVariantMix(
                            detail.id,
                            p.product.id
                        )

                        for (variant in p.variants) {
                            val variantMixOrder =
                                soliteDao.getOrderMixProductVariant(
                                    variantMix.id,
                                    variant.id
                                )
                            soliteDao.deleteOrderMixProductVariant(variantMixOrder)
                        }

                        soliteDao.deleteOrderProductVariantMix(variantMix)
                    }
                } else {

                    increaseStock(item.product!!.id, (item.amount * item.product!!.portion))

                    for (variant in item.variants) {
                        val orderVariant =
                            soliteDao.getVariantOrder(detail.id, variant.id)
                        soliteDao.deleteOrderVariant(orderVariant)
                    }
                }

                soliteDao.deleteOrderDetail(detail)
            }
        }
    }

    private fun insertOrderProduct(order: OrderWithProduct) {
        for (item in order.products) {
            if (item.product != null) {

                val detail = OrderDetail(order.order.order.orderNo, item.product!!.id, item.amount)
                detail.id = soliteDao.insertDetailOrder(detail)

                if (item.product!!.isMix) {
                    for (p in item.mixProducts) {

                        soliteDao.decreaseAndGetProduct(p.product.id, p.amount)

                        val variantMix = OrderProductVariantMix(detail.id, p.product.id, p.amount)
                        variantMix.id = soliteDao.insertVariantMixOrder(variantMix)

                        for (variant in p.variants) {
                            val mixVariant = OrderMixProductVariant(variantMix.id, variant.id)
                            mixVariant.id =
                                soliteDao.insertMixVariantOrder(mixVariant)
                        }
                    }
                } else {

                    soliteDao.decreaseAndGetProduct(
                        item.product!!.id,
                        (item.amount * item.product!!.portion)
                    )

                    for (variant in item.variants) {
                        val orderVariant = OrderProductVariant(detail.id, variant.id)
                        soliteDao.insertVariantOrder(orderVariant)
                    }
                }
            }
        }
    }

    private fun increaseStock(idProduct: Long, amount: Int)
            : BatchWithObject<Product> {
        val product = soliteDao.increaseAndGetProduct(idProduct, amount)
        val doc = Firebase.firestore
            .collection(AppDatabase.DB_NAME)
            .document(AppDatabase.MAIN)
            .collection(Product.DB_NAME)
            .document(product.id.toString())
        return BatchWithObject(product, BatchWithData(doc, Product.toHashMap(product)))
    }

    override fun getUsers(userId: String): LiveData<Resource<User?>> {
        return object : NetworkBoundResource<User?, List<User>>(appExecutors) {
            override fun loadFromDB(): LiveData<User?> {
                return soliteDao.getUser(userId)
            }

        }.asLiveData()
    }
}
