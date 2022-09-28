package com.socialite.solite_pos.data.source.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.socialite.solite_pos.data.NetworkBoundResource
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.helper.Products
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseProductWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.VariantWithOptions
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderMixProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariantMix
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.helper.PurchaseWithSupplier
import com.socialite.solite_pos.data.source.local.entity.room.helper.VariantWithVariantMix
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.Outcome
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.Purchase
import com.socialite.solite_pos.data.source.local.entity.room.master.PurchaseProduct
import com.socialite.solite_pos.data.source.local.entity.room.master.User
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.LocalDataSource
import com.socialite.solite_pos.data.source.remote.response.entity.BatchWithData
import com.socialite.solite_pos.data.source.remote.response.entity.BatchWithObject
import com.socialite.solite_pos.data.source.remote.response.entity.DataProductResponse
import com.socialite.solite_pos.data.source.remote.response.entity.OrderProductResponse
import com.socialite.solite_pos.data.source.remote.response.entity.OrderResponse
import com.socialite.solite_pos.data.source.remote.response.entity.PurchaseProductResponse
import com.socialite.solite_pos.data.source.remote.response.entity.PurchaseResponse
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

    override val purchases: LiveData<Resource<List<PurchaseWithSupplier>>>
        get() {
            return object :
                NetworkBoundResource<List<PurchaseWithSupplier>, PurchaseResponse>(appExecutors) {
                override fun loadFromDB(): LiveData<List<PurchaseWithSupplier>> {
                    return localDataSource.soliteDao.getPurchases()
                }
            }.asLiveData()
        }

    override fun getPurchaseProducts(purchaseNo: String): LiveData<Resource<List<PurchaseProductWithProduct>>> {
        return object :
            NetworkBoundResource<List<PurchaseProductWithProduct>, PurchaseProductResponse>(
                appExecutors
            ) {
            override fun loadFromDB(): LiveData<List<PurchaseProductWithProduct>> {
                return localDataSource.soliteDao.getPurchasesProduct(purchaseNo)
            }
        }.asLiveData()
    }

    override fun newPurchase(data: PurchaseWithProduct, callback: (ApiResponse<Boolean>) -> Unit) {
        val batches: ArrayList<BatchWithData> = ArrayList()
        batches.add(insertPurchase(data.purchase).batch)

        for (item in data.purchaseProduct) {
            batches.add(insertPurchaseProduct(item).batch)
        }

        for (product in data.products) {
            if (product.purchaseProduct != null) {
                batches.add(
                    increaseStock(
                        product.purchaseProduct!!.idProduct,
                        product.purchaseProduct!!.amount
                    ).batch
                )
            }
        }
    }

    private fun insertPurchase(purchase: Purchase)
            : BatchWithObject<Purchase> {
        localDataSource.soliteDao.insertPurchase(purchase)
        val doc = Firebase.firestore
            .collection(AppDatabase.DB_NAME)
            .document(AppDatabase.MAIN)
            .collection(Purchase.DB_NAME)
            .document(purchase.purchaseNo)
        return BatchWithObject(purchase, BatchWithData(doc, Purchase.toHashMap(purchase)))
    }

    private fun insertPurchaseProduct(product: PurchaseProduct)
            : BatchWithObject<PurchaseProduct> {
        localDataSource.soliteDao.insertPurchaseProduct(product)
        val doc = Firebase.firestore
            .collection(AppDatabase.DB_NAME)
            .document(AppDatabase.MAIN)
            .collection(PurchaseProduct.DB_NAME)
            .document(product.id.toString())
        return BatchWithObject(product, BatchWithData(doc, PurchaseProduct.toHashMap(product)))
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

    override fun getProductList(idCategory: Long): LiveData<Resource<List<Products>>> {
        return object : NetworkBoundResource<List<Products>, DataProductResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<List<Products>> {
                return localDataSource.getProducts(idCategory)
            }
        }.asLiveData()
    }

    override fun getProducts(idCategory: Long): LiveData<Resource<List<ProductWithCategory>>> {
        return object :
            NetworkBoundResource<List<ProductWithCategory>, DataProductResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<List<ProductWithCategory>> {
                return localDataSource.soliteDao.getProducts(idCategory)
            }
        }.asLiveData()
    }

    override fun getProductVariantOptions(idProduct: Long): LiveData<Resource<List<VariantWithOptions>?>> {
        return object :
            NetworkBoundResource<List<VariantWithOptions>?, List<VariantProduct>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<VariantWithOptions>?> {
                return localDataSource.getProductVariantOptions(idProduct)
            }
        }.asLiveData()
    }

    override fun getVariantProduct(
        idProduct: Long,
        idVariantOption: Long
    ): LiveData<Resource<VariantProduct?>> {
        return object : NetworkBoundResource<VariantProduct?, List<VariantProduct>>(appExecutors) {
            override fun loadFromDB(): LiveData<VariantProduct?> {
                return localDataSource.soliteDao.getVariantProduct(idProduct, idVariantOption)
            }
        }.asLiveData()
    }

    override fun getVariantProductById(idProduct: Long): LiveData<Resource<VariantProduct?>> {
        return object : NetworkBoundResource<VariantProduct?, List<VariantProduct>>(appExecutors) {
            override fun loadFromDB(): LiveData<VariantProduct?> {
                return localDataSource.soliteDao.getVariantProductById(idProduct)
            }
        }.asLiveData()
    }

    override fun insertVariantProduct(data: VariantProduct, callback: (ApiResponse<Long>) -> Unit) {
        localDataSource.soliteDao.insertVariantProduct(data)
    }

    override fun removeVariantProduct(
        data: VariantProduct,
        callback: (ApiResponse<Boolean>) -> Unit
    ) {
        localDataSource.soliteDao.removeVariantProduct(data.idVariantOption, data.idProduct)
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

    override fun getProductWithCategories(category: Long): LiveData<Resource<List<ProductWithCategory>>> {
        return object :
            NetworkBoundResource<List<ProductWithCategory>, List<Product>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<ProductWithCategory>> {
                return localDataSource.soliteDao.getProductWithCategories(category)
            }
        }.asLiveData()
    }

    override fun insertProduct(data: Product, callback: (ApiResponse<Long>) -> Unit) {
        localDataSource.soliteDao.insertProduct(data)
    }

    override fun updateProduct(data: Product, callback: (ApiResponse<Boolean>) -> Unit) {
        localDataSource.soliteDao.insertProduct(data)
    }

    override fun getCategories(query: SimpleSQLiteQuery): LiveData<Resource<List<Category>>> {
        return object : NetworkBoundResource<List<Category>, List<Category>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<Category>> {
                return localDataSource.soliteDao.getCategories(query)
            }

        }.asLiveData()
    }

    override fun insertCategory(data: Category, callback: (ApiResponse<Long>) -> Unit) {
        localDataSource.soliteDao.insertCategory(data)
    }

    override fun updateCategory(data: Category, callback: (ApiResponse<Boolean>) -> Unit) {
        localDataSource.soliteDao.insertCategory(data)
    }

    override val variants: LiveData<Resource<List<Variant>>>
        get() {
            return object : NetworkBoundResource<List<Variant>, List<Variant>>(appExecutors) {
                override fun loadFromDB(): LiveData<List<Variant>> {
                    return localDataSource.soliteDao.getVariants()
                }

            }.asLiveData()
        }

    override fun insertVariant(data: Variant, callback: (ApiResponse<Long>) -> Unit) {
        localDataSource.soliteDao.insertVariant(data)
    }

    override fun updateVariant(data: Variant, callback: (ApiResponse<Boolean>) -> Unit) {
        localDataSource.soliteDao.insertVariant(data)
    }

    override fun getVariantOptions(query: SupportSQLiteQuery): LiveData<Resource<List<VariantOption>>> {
        return object :
            NetworkBoundResource<List<VariantOption>, List<VariantOption>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<VariantOption>> {
                return localDataSource.soliteDao.getVariantOptions(query)
            }
        }.asLiveData()
    }

    override fun insertVariantOption(data: VariantOption, callback: (ApiResponse<Long>) -> Unit) {
        localDataSource.soliteDao.insertVariantOption(data)
    }

    override fun updateVariantOption(
        data: VariantOption,
        callback: (ApiResponse<Boolean>) -> Unit
    ) {
        localDataSource.soliteDao.insertVariantOption(data)
    }

    override val customers: LiveData<Resource<List<Customer>>>
        get() {
            return object : NetworkBoundResource<List<Customer>, List<Customer>>(appExecutors) {
                override fun loadFromDB(): LiveData<List<Customer>> {
                    return localDataSource.soliteDao.getCustomers()
                }
            }.asLiveData()
        }

    override fun insertCustomer(data: Customer, callback: (ApiResponse<Long>) -> Unit) {
        localDataSource.soliteDao.insertCustomer(data)
    }

    override fun updateCustomer(data: Customer, callback: (ApiResponse<Boolean>) -> Unit) {
        localDataSource.soliteDao.updateCustomer(data)
    }

    override fun getOutcomes(date: String): LiveData<Resource<List<Outcome>>> {
        return object : NetworkBoundResource<List<Outcome>, List<Outcome>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<Outcome>> {
                return localDataSource.soliteDao.getOutcome(date)
            }
        }.asLiveData()
    }

    override fun insertOutcome(data: Outcome, callback: (ApiResponse<Long>) -> Unit) {
        localDataSource.soliteDao.insertOutcome(data)
    }

    override fun updateOutcome(data: Outcome, callback: (ApiResponse<Boolean>) -> Unit) {
        localDataSource.soliteDao.updateOutcome(data)
    }

    override fun getUsers(userId: String): LiveData<Resource<User?>> {
        return object : NetworkBoundResource<User?, List<User>>(appExecutors) {
            override fun loadFromDB(): LiveData<User?> {
                return localDataSource.soliteDao.getUser(userId)
            }

        }.asLiveData()
    }
}
