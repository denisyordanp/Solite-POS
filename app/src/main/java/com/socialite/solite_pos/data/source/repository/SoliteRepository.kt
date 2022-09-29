package com.socialite.solite_pos.data.source.repository

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.socialite.solite_pos.data.NetworkBoundResource
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.helper.VariantWithOptions
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderMixProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariantMix
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.helper.VariantWithVariantMix
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.User
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.LocalDataSource
import com.socialite.solite_pos.data.source.remote.response.entity.BatchWithData
import com.socialite.solite_pos.data.source.remote.response.entity.BatchWithObject
import com.socialite.solite_pos.data.source.remote.response.entity.OrderProductResponse
import com.socialite.solite_pos.data.source.remote.response.entity.OrderResponse
import com.socialite.solite_pos.data.source.remote.response.helper.ApiResponse
import com.socialite.solite_pos.utils.database.AppExecutors
import com.socialite.solite_pos.vo.Resource

class SoliteRepository private constructor(
//		private val remoteDataSource: RemoteDataSource,
    private val appExecutors: AppExecutors,
    private val localDataSource: LocalDataSource
) : SoliteDataSource {

    companion object {
        @Volatile
        private var INSTANCE: SoliteRepository? = null

        fun getInstance(
            appExecutors: AppExecutors,
            localDataSource: LocalDataSource
        ): SoliteRepository {
            if (INSTANCE == null) {
                synchronized(SoliteRepository::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = SoliteRepository(appExecutors, localDataSource)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getOrderList(status: Int, date: String): LiveData<Resource<List<OrderData>>> {
        return object : NetworkBoundResource<List<OrderData>, OrderResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<List<OrderData>> {
                return localDataSource.soliteDao.getOrdersByStatus(status, date)
            }
        }.asLiveData()
    }

    override fun getLocalOrders(status: Int, date: String): LiveData<List<OrderData>> {
        return localDataSource.soliteDao.getOrdersByStatus(status, date)
    }

    override fun getProductOrder(orderNo: String): LiveData<Resource<List<ProductOrderDetail>>> {
        return object :
            NetworkBoundResource<List<ProductOrderDetail>, OrderProductResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<List<ProductOrderDetail>> {
                return localDataSource.getProductOrder(orderNo)
            }
        }.asLiveData()
    }

    fun insertPaymentOrder(payment: OrderPayment): OrderPayment {
        val id = localDataSource.soliteDao.insertPaymentOrder(payment)
        payment.id = id
        return payment
    }

    override fun newOrder(order: OrderWithProduct) {
        localDataSource.soliteDao.insertOrder(order.order.order)
        insertOrderProduct(order)
    }

    private fun insertOrderProduct(order: OrderWithProduct) {
        for (item in order.products) {
            if (item.product != null) {

                val detail = OrderDetail(order.order.order.orderNo, item.product!!.id, item.amount)
                detail.id = localDataSource.soliteDao.insertDetailOrder(detail)

                if (item.product!!.isMix) {
                    for (p in item.mixProducts) {

                        localDataSource.soliteDao.decreaseAndGetProduct(p.product.id, p.amount)

                        val variantMix = OrderProductVariantMix(detail.id, p.product.id, p.amount)
                        variantMix.id = localDataSource.soliteDao.insertVariantMixOrder(variantMix)

                        for (variant in p.variants) {
                            val mixVariant = OrderMixProductVariant(variantMix.id, variant.id)
                            mixVariant.id =
                                localDataSource.soliteDao.insertMixVariantOrder(mixVariant)
                        }
                    }
                } else {

                    localDataSource.soliteDao.decreaseAndGetProduct(
                        item.product!!.id,
                        (item.amount * item.product!!.portion)
                    )

                    for (variant in item.variants) {
                        val orderVariant = OrderProductVariant(detail.id, variant.id)
                        localDataSource.soliteDao.insertVariantOrder(orderVariant)
                    }
                }
            }
        }
    }

    fun doneOrder(order: OrderWithProduct) {
        updateOrder(order.order.order)
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
        localDataSource.soliteDao.updateOrder(order)
    }

    private fun deleteOrderProduct(orderProduct: OrderWithProduct) {
        for (item in orderProduct.products) {
            if (item.product != null) {

                val detail = localDataSource.soliteDao.getDetailOrders(
                    orderProduct.order.order.orderNo,
                    item.product!!.id
                )

                if (item.product!!.isMix) {
                    for (p in item.mixProducts) {

                        increaseStock(p.product.id, p.amount)

                        val variantMix = localDataSource.soliteDao.getOrderProductVariantMix(
                            detail.id,
                            p.product.id
                        )

                        for (variant in p.variants) {
                            val variantMixOrder =
                                localDataSource.soliteDao.getOrderMixProductVariant(
                                    variantMix.id,
                                    variant.id
                                )
                            localDataSource.soliteDao.deleteOrderMixProductVariant(variantMixOrder)
                        }

                        localDataSource.soliteDao.deleteOrderProductVariantMix(variantMix)
                    }
                } else {

                    increaseStock(item.product!!.id, (item.amount * item.product!!.portion))

                    for (variant in item.variants) {
                        val orderVariant =
                            localDataSource.soliteDao.getVariantOrder(detail.id, variant.id)
                        localDataSource.soliteDao.deleteOrderVariant(orderVariant)
                    }
                }

                localDataSource.soliteDao.deleteOrderDetail(detail)
            }
        }
    }

    private fun increaseStock(idProduct: Long, amount: Int)
            : BatchWithObject<Product> {
        val product = localDataSource.soliteDao.increaseAndGetProduct(idProduct, amount)
        val doc = Firebase.firestore
            .collection(AppDatabase.DB_NAME)
            .document(AppDatabase.MAIN)
            .collection(Product.DB_NAME)
            .document(product.id.toString())
        return BatchWithObject(product, BatchWithData(doc, Product.toHashMap(product)))
    }

    override fun getProductVariantOptions(idProduct: Long): LiveData<Resource<List<VariantWithOptions>?>> {
        return object :
            NetworkBoundResource<List<VariantWithOptions>?, List<VariantProduct>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<VariantWithOptions>?> {
                return localDataSource.getProductVariantOptions(idProduct)
            }
        }.asLiveData()
    }

    override fun getVariantMixProductById(
        idVariant: Long,
        idProduct: Long
    ): LiveData<Resource<VariantMix?>> {
        return object : NetworkBoundResource<VariantMix?, List<VariantMix>>(appExecutors) {
            override fun loadFromDB(): LiveData<VariantMix?> {
                return localDataSource.soliteDao.getVariantMixProductById(idVariant, idProduct)
            }
        }.asLiveData()
    }

    override fun getVariantMixProduct(idVariant: Long): LiveData<Resource<VariantWithVariantMix>> {
        return object : NetworkBoundResource<VariantWithVariantMix, List<Product>>(appExecutors) {
            override fun loadFromDB(): LiveData<VariantWithVariantMix> {
                return localDataSource.soliteDao.getVariantMixProduct(idVariant)
            }
        }.asLiveData()
    }

    override fun insertVariantMix(data: VariantMix, callback: (ApiResponse<Long>) -> Unit) {
        localDataSource.soliteDao.insertVariantMix(data)
    }

    override fun removeVariantMix(data: VariantMix, callback: (ApiResponse<Boolean>) -> Unit) {
        localDataSource.soliteDao.removeVariantMix(data.id)
    }

    override fun getUsers(userId: String): LiveData<Resource<User?>> {
        return object : NetworkBoundResource<User?, List<User>>(appExecutors) {
            override fun loadFromDB(): LiveData<User?> {
                return localDataSource.soliteDao.getUser(userId)
            }

        }.asLiveData()
    }
}
