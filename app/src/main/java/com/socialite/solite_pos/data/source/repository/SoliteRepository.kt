package com.socialite.solite_pos.data.source.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.socialite.solite_pos.data.NetworkBoundResource
import com.socialite.solite_pos.data.NetworkFunBound
import com.socialite.solite_pos.data.source.local.entity.helper.*
import com.socialite.solite_pos.data.source.local.entity.room.bridge.*
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.helper.PurchaseWithSupplier
import com.socialite.solite_pos.data.source.local.entity.room.helper.VariantWithVariantMix
import com.socialite.solite_pos.data.source.local.entity.room.master.*
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.LocalDataSource
import com.socialite.solite_pos.data.source.remote.RemoteDataSource
import com.socialite.solite_pos.data.source.remote.response.entity.*
import com.socialite.solite_pos.data.source.remote.response.helper.ApiResponse
import com.socialite.solite_pos.utils.database.AppExecutors
import com.socialite.solite_pos.vo.Resource

class SoliteRepository private constructor(
		private val remoteDataSource: RemoteDataSource,
		private val appExecutors: AppExecutors,
		private val localDataSource: LocalDataSource
) : SoliteDataSource {

	companion object {
		@Volatile
		private var INSTANCE: SoliteRepository? = null

		fun getInstance(remoteData: RemoteDataSource, appExecutors: AppExecutors, localDataSource: LocalDataSource): SoliteRepository {
			if (INSTANCE == null) {
				synchronized(SoliteRepository::class.java) {
					if (INSTANCE == null) {
						INSTANCE = SoliteRepository(remoteData, appExecutors, localDataSource)
					}
				}
			}
			return INSTANCE!!
		}
	}

	override fun getOrderList(status: Int, date: String): LiveData<Resource<List<OrderData>>> {
		return object : NetworkBoundResource<List<OrderData>, OrderResponse>(appExecutors){
			override fun loadFromDB(): LiveData<List<OrderData>> {
				return localDataSource.soliteDao.getOrdersByStatus(status, date)
			}

			override fun shouldFetch(data: List<OrderData>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(): LiveData<ApiResponse<OrderResponse>> {
				return remoteDataSource.getOrderDetail()
			}

			override fun saveCallResult(data: OrderResponse?) {
				if (data != null){
					localDataSource.soliteDao.insertCustomers(data.customer)
					localDataSource.soliteDao.insertOrders(data.order)
					localDataSource.soliteDao.insertPayments(data.payments)
					localDataSource.soliteDao.insertPaymentOrders(data.orderPayment)
				}
			}
		}.asLiveData()
	}

    override fun getProductOrder(orderNo: String): LiveData<Resource<List<ProductOrderDetail>>> {
        return object : NetworkBoundResource<List<ProductOrderDetail>, OrderProductResponse>(appExecutors){
            override fun loadFromDB(): LiveData<List<ProductOrderDetail>> {
                return localDataSource.getProductOrder(orderNo)
            }

            override fun shouldFetch(data: List<ProductOrderDetail>): Boolean {
                return data.isNullOrEmpty()
            }

            override fun createCall(): LiveData<ApiResponse<OrderProductResponse>> {
                return remoteDataSource.getProductOrder()
            }

            override fun saveCallResult(data: OrderProductResponse?) {
                if (data != null){
                    insertDataProduct(data.products)

                    localDataSource.soliteDao.insertDetailOrders(data.details)
                    localDataSource.soliteDao.insertVariantMixOrders(data.mixOrders)
                    localDataSource.soliteDao.insertMixVariantOrders(data.mixVariants)
                    localDataSource.soliteDao.insertVariantOrders(data.variants)
                }
            }
        }.asLiveData()
    }

    private fun insertDataProduct(product: DataProductResponse){
		localDataSource.soliteDao.insertCategories(product.categories)
		localDataSource.soliteDao.insertProducts(product.products)
		localDataSource.soliteDao.insertVariants(product.variants)
		localDataSource.soliteDao.insertVariantOptions(product.variantOptions)
	}

	override fun insertPaymentOrder(payment: OrderPayment, callback: (ApiResponse<LiveData<OrderData>>) -> Unit) {
		object : NetworkFunBound<Boolean, LiveData<OrderData>, LiveData<OrderData>>(callback) {
			var id = 0L
			override fun dbOperation(): LiveData<OrderData>{
				this.id = localDataSource.soliteDao.insertPaymentOrder(payment)
				return localDataSource.soliteDao.getOrdersByNo(payment.orderNO)
			}

			override fun createCall(savedData: LiveData<OrderData>, inCallback: (ApiResponse<Boolean>) -> Unit) {
				payment.id = id
				return remoteDataSource.setOrderPayment(payment, inCallback)
			}

			override fun successUpload(before: LiveData<OrderData>) {
				payment.id = id
				payment.isUpload = true
				localDataSource.soliteDao.updatePaymentOrder(payment)
				callback.invoke(ApiResponse.success(before))
			}

			override fun dbFinish(savedData: LiveData<OrderData>) {
				callback.invoke(ApiResponse.finish(savedData))
			}
		}
	}

	override fun newOrder(order: OrderWithProduct, callback: (ApiResponse<Boolean>) -> Unit) {

		val batches: ArrayList<BatchWithData> = ArrayList()

		batches.add(insertOrder(order.order.order).batch)
        for (item in order.products){
            if (item.product != null){

                val detail = insertDetailOrder(
                    OrderDetail(order.order.order.orderNo, item.product!!.id, item.amount)
                )
                batches.add(detail.batch)
                val idOrder = detail.data.id

                if (item.product!!.isMix){
                    for (p in item.mixProducts){

                        batches.add(decreaseStock(p.product.id, p.amount).batch)

                        val variantMix = insertVariantMixOrder(
                            OrderProductVariantMix(idOrder, p.product.id, p.amount)
                        )
                        batches.add(variantMix.batch)
                        val idMix = variantMix.data.id

                        for (variant in p.variants){
                            batches.add(
                                insertMixVariantOrder(OrderMixProductVariant(idMix, variant.id))
                                    .batch
                            )
                        }
                    }
                }else{
                    batches.add(
                        decreaseStock(item.product!!.id, (item.amount * item.product!!.portion))
                            .batch
                    )

                    for (variant in item.variants){
                        batches.add(
                            insertVariantOrder(OrderProductVariant(idOrder, variant.id))
                                .batch
                        )
                    }
                }
            }
        }

		remoteDataSource.batch(batches, callback)
	}

	private fun insertOrder(order: Order)
	: BatchWithObject<Order> {
		localDataSource.soliteDao.insertOrder(order)
		val doc = Firebase.firestore
				.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Order.DB_NAME)
				.document(order.orderNo)
		return BatchWithObject(order, BatchWithData(doc, Order.toHashMap(order)))
	}

	private fun insertDetailOrder(detail: OrderDetail)
	: BatchWithObject<OrderDetail> {
		detail.id = localDataSource.soliteDao.insertDetailOrder(detail)
		val doc = Firebase.firestore
				.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(OrderDetail.DB_NAME)
				.document(detail.id.toString())
		return BatchWithObject(detail, BatchWithData(doc, OrderDetail.toHashMap(detail)))
	}

	private fun decreaseStock(idProduct: Long, amount: Int)
	: BatchWithObject<Product> {
		val product = localDataSource.soliteDao.decreaseAndGetProduct(idProduct, amount)
		val doc = Firebase.firestore
				.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Product.DB_NAME)
				.document(product.id.toString())
		return BatchWithObject(product, BatchWithData(doc, Product.toHashMap(product)))
	}

	private fun insertVariantMixOrder(variant: OrderProductVariantMix)
	: BatchWithObject<OrderProductVariantMix> {
		variant.id = localDataSource.soliteDao.insertVariantMixOrder(variant)
		val doc = Firebase.firestore
				.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(OrderProductVariantMix.DB_NAME)
				.document(variant.id.toString())
		return BatchWithObject(variant, BatchWithData(doc, OrderProductVariantMix.toHashMap(variant)))
	}

	private fun insertMixVariantOrder(mixVariant: OrderMixProductVariant)
	: BatchWithObject<OrderMixProductVariant> {
		mixVariant.id = localDataSource.soliteDao.insertMixVariantOrder(mixVariant)
		val doc = Firebase.firestore
				.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(OrderMixProductVariant.DB_NAME)
				.document(mixVariant.id.toString())
		return BatchWithObject(mixVariant, BatchWithData(doc, OrderMixProductVariant.toHashMap(mixVariant)))
	}

	private fun insertVariantOrder(variant: OrderProductVariant)
	: BatchWithObject<OrderProductVariant> {
		variant.id = localDataSource.soliteDao.insertVariantOrder(variant)
		val doc = Firebase.firestore
				.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(OrderProductVariant.DB_NAME)
				.document(variant.id.toString())
		return BatchWithObject(variant, BatchWithData(doc, OrderProductVariant.toHashMap(variant)))
	}

	override fun updateOrder(order: Order, callback: (ApiResponse<Boolean>) -> Unit) {
		object : NetworkFunBound<Boolean, Order, Boolean>(callback) {
			override fun dbOperation(): Order{
				order.isUploaded = false
				localDataSource.soliteDao.updateOrder(order)
				return order
			}

			override fun createCall(savedData: Order, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setOrder(savedData, inCallback)
			}

			override fun successUpload(before: Order) {
				before.isUploaded = true
				localDataSource.soliteDao.updateOrder(before)
				callback.invoke(ApiResponse.success(true))
			}

			override fun dbFinish(savedData: Order) {
				callback.invoke(ApiResponse.finish(true))
			}
		}
	}

	override fun cancelOrder(order: OrderWithProduct, callback: (ApiResponse<Boolean>) -> Unit) {
		val batches: ArrayList<BatchWithData> = ArrayList()
		batches.add(updateOrder(order.order.order).batch)
        for (item in order.products){
            if (item.product != null){

                if (item.product!!.isMix){
                    for (p in item.mixProducts){
                        batches.add(increaseStock(p.product.id, p.amount).batch)
                    }
                }else{
                    batches.add(increaseStock(item.product!!.id, (item.amount * item.product!!.portion)).batch)
                }
            }
        }
		remoteDataSource.batch(batches, callback)
	}

	private fun updateOrder(order: Order)
			: BatchWithObject<Order> {
		localDataSource.soliteDao.updateOrder(order)
		val doc = Firebase.firestore
				.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Order.DB_NAME)
				.document(order.orderNo)
		return BatchWithObject(order, BatchWithData(doc, Order.toHashMap(order)))
	}

	override val purchases: LiveData<Resource<List<PurchaseWithSupplier>>>
	get() {
		return object : NetworkBoundResource<List<PurchaseWithSupplier>, PurchaseResponse>(appExecutors){
			override fun loadFromDB(): LiveData<List<PurchaseWithSupplier>> {
				return localDataSource.soliteDao.getPurchases()
			}

			override fun shouldFetch(data: List<PurchaseWithSupplier>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(): LiveData<ApiResponse<PurchaseResponse>> {
				return remoteDataSource.purchaseDetails
			}

			override fun saveCallResult(data: PurchaseResponse?) {
				if (data != null){
					localDataSource.soliteDao.insertSuppliers(data.suppliers)
					localDataSource.soliteDao.insertPurchases(data.purchases)
				}
			}
		}.asLiveData()
	}

	override fun getPurchaseProducts(purchaseNo: String): LiveData<Resource<List<PurchaseProductWithProduct>>> {
		return object : NetworkBoundResource<List<PurchaseProductWithProduct>, PurchaseProductResponse>(appExecutors){
			override fun loadFromDB(): LiveData<List<PurchaseProductWithProduct>> {
				return localDataSource.soliteDao.getPurchasesProduct(purchaseNo)
			}

			override fun shouldFetch(data: List<PurchaseProductWithProduct>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(): LiveData<ApiResponse<PurchaseProductResponse>> {
				return remoteDataSource.purchaseProductsWithProducts
			}

			override fun saveCallResult(data: PurchaseProductResponse?) {
				if (data != null){
					insertDataProduct(data.products)
					localDataSource.soliteDao.insertPurchaseProducts(data.purchases)
				}
			}
		}.asLiveData()
	}

	override fun newPurchase(data: PurchaseWithProduct, callback: (ApiResponse<Boolean>) -> Unit) {
		val batches: ArrayList<BatchWithData> = ArrayList()
		batches.add(insertPurchase(data.purchase).batch)

		for (item in data.purchaseProduct){
			batches.add(insertPurchaseProduct(item).batch)
		}

		for (product in data.products){
			if (product.purchaseProduct != null){
				batches.add(increaseStock(product.purchaseProduct!!.idProduct, product.purchaseProduct!!.amount).batch)
			}
		}

		remoteDataSource.batch(batches, callback)
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
		return object : NetworkBoundResource<List<Products>, DataProductResponse>(appExecutors){
			override fun loadFromDB(): LiveData<List<Products>> {
				return localDataSource.getProducts(idCategory)
			}

			override fun shouldFetch(data: List<Products>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(): LiveData<ApiResponse<DataProductResponse>> {
				return remoteDataSource.dataProducts
			}

			override fun saveCallResult(data: DataProductResponse?) {
				if (data != null){
					insertDataProduct(data)
				}
			}
		}.asLiveData()
	}

	override fun getProducts(idCategory: Long): LiveData<Resource<List<ProductWithCategory>>> {
		return object : NetworkBoundResource<List<ProductWithCategory>, DataProductResponse>(appExecutors){
			override fun loadFromDB(): LiveData<List<ProductWithCategory>> {
				return localDataSource.soliteDao.getProducts(idCategory)
			}

			override fun shouldFetch(data: List<ProductWithCategory>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(): LiveData<ApiResponse<DataProductResponse>> {
				return remoteDataSource.dataProducts
			}

			override fun saveCallResult(data: DataProductResponse?) {
				if (data != null){
					insertDataProduct(data)
				}
			}
		}.asLiveData()
	}

	override fun getProductVariantOptions(idProduct: Long): LiveData<Resource<List<VariantWithOptions>?>> {
		return object : NetworkBoundResource<List<VariantWithOptions>?, List<VariantProduct>>(appExecutors){
			override fun loadFromDB(): LiveData<List<VariantWithOptions>?> {
				return localDataSource.getProductVariantOptions(idProduct)
			}

			override fun shouldFetch(data: List<VariantWithOptions>?): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(): LiveData<ApiResponse<List<VariantProduct>>> {
				return remoteDataSource.getVariantProducts()
			}

			override fun saveCallResult(data: List<VariantProduct>?) {
				if (!data.isNullOrEmpty()){
					localDataSource.soliteDao.insertVariantProducts(data)
				}
			}
		}.asLiveData()
	}

	override fun getVariantProduct(idProduct: Long, idVariantOption: Long): LiveData<Resource<VariantProduct?>> {
		return object : NetworkBoundResource<VariantProduct?, List<VariantProduct>>(appExecutors){
			override fun loadFromDB(): LiveData<VariantProduct?> {
				return localDataSource.soliteDao.getVariantProduct(idProduct, idVariantOption)
			}

			override fun shouldFetch(data: VariantProduct?): Boolean {
				return data == null
			}

			override fun createCall(): LiveData<ApiResponse<List<VariantProduct>>> {
				return remoteDataSource.getVariantProducts()
			}

			override fun saveCallResult(data: List<VariantProduct>?) {
				if (!data.isNullOrEmpty()) localDataSource.soliteDao.insertVariantProducts(data)
			}
		}.asLiveData()
	}

	override fun getVariantProductById(idProduct: Long): LiveData<Resource<VariantProduct?>> {
		return object : NetworkBoundResource<VariantProduct?, List<VariantProduct>>(appExecutors){
			override fun loadFromDB(): LiveData<VariantProduct?> {
				return localDataSource.soliteDao.getVariantProductById(idProduct)
			}

			override fun shouldFetch(data: VariantProduct?): Boolean {
				return data == null
			}

			override fun createCall(): LiveData<ApiResponse<List<VariantProduct>>> {
				return remoteDataSource.getVariantProducts()
			}

			override fun saveCallResult(data: List<VariantProduct>?) {
				if (!data.isNullOrEmpty()) localDataSource.soliteDao.insertVariantProducts(data)
			}
		}.asLiveData()
	}

	override fun insertVariantProduct(data: VariantProduct, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, VariantProduct, Long>(callback) {
			override fun dbOperation(): VariantProduct{
				val id = localDataSource.soliteDao.insertVariantProduct(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: VariantProduct, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setVariantProduct(savedData, inCallback)
			}

			override fun successUpload(before: VariantProduct) {
				before.isUploaded = true
				localDataSource.soliteDao.updateVariantProduct(before)
				callback.invoke(ApiResponse.success(before.id))
			}

			override fun dbFinish(savedData: VariantProduct) {
				callback.invoke(ApiResponse.finish(savedData.id))
			}
		}
	}

	override fun removeVariantProduct(data: VariantProduct, callback: (ApiResponse<Boolean>) -> Unit) {
		object : NetworkFunBound<Boolean, VariantProduct, Boolean>(callback) {
			override fun dbOperation(): VariantProduct{
				localDataSource.soliteDao.removeVariantProduct(data.idVariantOption, data.idProduct)
				return data
			}

			override fun createCall(savedData: VariantProduct, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.deleteVariantProduct(data, inCallback)
			}

			override fun successUpload(before: VariantProduct) {
				callback.invoke(ApiResponse.success(true))
			}

			override fun dbFinish(savedData: VariantProduct) {
				callback.invoke(ApiResponse.finish(true))
			}
		}
	}

	override fun getVariantMixProductById(idVariant: Long, idProduct: Long): LiveData<Resource<VariantMix?>> {
		return object : NetworkBoundResource<VariantMix?, List<VariantMix>>(appExecutors){
			override fun loadFromDB(): LiveData<VariantMix?> {
				return localDataSource.soliteDao.getVariantMixProductById(idVariant, idProduct)
			}

			override fun shouldFetch(data: VariantMix?): Boolean {
				return data == null
			}

			override fun createCall(): LiveData<ApiResponse<List<VariantMix>>> {
				return remoteDataSource.variantMixes
			}

			override fun saveCallResult(data: List<VariantMix>?) {
				if (!data.isNullOrEmpty()) localDataSource.soliteDao.insertVariantMixes(data)
			}
		}.asLiveData()
	}

	override fun getVariantMixProduct(idVariant: Long): LiveData<Resource<VariantWithVariantMix>>{
		return object : NetworkBoundResource<VariantWithVariantMix, List<Product>>(appExecutors){
			override fun loadFromDB(): LiveData<VariantWithVariantMix> {
				return localDataSource.soliteDao.getVariantMixProduct(idVariant)
			}

			override fun shouldFetch(data: VariantWithVariantMix): Boolean {
				return data.products.isNullOrEmpty()
			}

			override fun createCall(): LiveData<ApiResponse<List<Product>>> {
				return remoteDataSource.getProducts()
			}

			override fun saveCallResult(data: List<Product>?) {
				if (!data.isNullOrEmpty()) localDataSource.soliteDao.insertProducts(data)
			}
		}.asLiveData()
	}

	override fun insertVariantMix(data: VariantMix, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, VariantMix, Long>(callback) {
			override fun dbOperation(): VariantMix{
				val id = localDataSource.soliteDao.insertVariantMix(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: VariantMix, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setVariantMix(savedData, inCallback)
			}

			override fun successUpload(before: VariantMix) {
				before.isUploaded = true
				localDataSource.soliteDao.updateVariantMix(before)
				callback.invoke(ApiResponse.success(before.id))
			}

			override fun dbFinish(savedData: VariantMix) {
				callback.invoke(ApiResponse.finish(savedData.id))
			}
		}
	}

	override fun removeVariantMix(data: VariantMix, callback: (ApiResponse<Boolean>) -> Unit) {
		object : NetworkFunBound<Boolean, VariantMix, Boolean>(callback) {
			override fun dbOperation(): VariantMix{
				localDataSource.soliteDao.removeVariantMix(data.id)
				return data
			}

			override fun createCall(savedData: VariantMix, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.deleteVariantMix(data, inCallback)
			}

			override fun successUpload(before: VariantMix) {
				callback.invoke(ApiResponse.success(true))
			}

			override fun dbFinish(savedData: VariantMix) {
				callback.invoke(ApiResponse.finish(true))
			}
		}
	}

	override fun getProductWithCategories(category: Long): LiveData<Resource<List<ProductWithCategory>>> {
		return object : NetworkBoundResource<List<ProductWithCategory>, List<Product>>(appExecutors){
			override fun loadFromDB(): LiveData<List<ProductWithCategory>> {
				return localDataSource.soliteDao.getProductWithCategories(category)
			}

			override fun shouldFetch(data: List<ProductWithCategory>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(): LiveData<ApiResponse<List<Product>>> {
				return remoteDataSource.getProducts()
			}

			override fun saveCallResult(data: List<Product>?) {
				if (!data.isNullOrEmpty()) localDataSource.soliteDao.insertProducts(data)
			}

		}.asLiveData()
	}

	override fun insertProduct(data: Product, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, Product, Long>(callback) {
			override fun dbOperation(): Product{
				val id = localDataSource.soliteDao.insertProduct(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: Product, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setProduct(savedData, inCallback)
			}

			override fun successUpload(before: Product) {
				before.isUploaded = true
				localDataSource.soliteDao.updateProduct(before)
				callback.invoke(ApiResponse.success(before.id))
			}

			override fun dbFinish(savedData: Product) {
				callback.invoke(ApiResponse.finish(savedData.id))
			}
		}
	}

	override fun updateProduct(data: Product, callback: (ApiResponse<Boolean>) -> Unit){
		object : NetworkFunBound<Boolean, Product, Boolean>(callback) {
			override fun dbOperation(): Product{
				data.isUploaded = false
				localDataSource.soliteDao.insertProduct(data)
				return data
			}

			override fun createCall(savedData: Product, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setProduct(savedData, inCallback)
			}

			override fun successUpload(before: Product) {
				before.isUploaded = true
				localDataSource.soliteDao.updateProduct(before)
				callback.invoke(ApiResponse.success(true))
			}

			override fun dbFinish(savedData: Product) {
				callback.invoke(ApiResponse.finish(true))
			}
		}
	}

	override fun getCategories(query: SimpleSQLiteQuery): LiveData<Resource<List<Category>>>{
		return object : NetworkBoundResource<List<Category>, List<Category>>(appExecutors){
			override fun loadFromDB(): LiveData<List<Category>> {
				return localDataSource.soliteDao.getCategories(query)
			}

			override fun shouldFetch(data: List<Category>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(): LiveData<ApiResponse<List<Category>>> {
				return remoteDataSource.getCategories()
			}

			override fun saveCallResult(data: List<Category>?) {
				if (!data.isNullOrEmpty()) localDataSource.soliteDao.insertCategories(data)
			}

		}.asLiveData()
	}

	override fun insertCategory(data: Category, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, Category, Long>(callback) {
			override fun dbOperation(): Category{
				val id = localDataSource.soliteDao.insertCategory(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: Category, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setCategory(savedData, inCallback)
			}

			override fun successUpload(before: Category) {
				before.isUploaded = true
				localDataSource.soliteDao.updateCategory(before)
				callback.invoke(ApiResponse.success(before.id))
			}

			override fun dbFinish(savedData: Category) {
				callback.invoke(ApiResponse.finish(savedData.id))
			}
		}
	}

	override fun updateCategory(data: Category, callback: (ApiResponse<Boolean>) -> Unit) {
		object : NetworkFunBound<Boolean, Category, Boolean>(callback) {
			override fun dbOperation(): Category{
				data.isUploaded = false
				localDataSource.soliteDao.insertCategory(data)
				return data
			}

			override fun createCall(savedData: Category, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setCategory(savedData, inCallback)
			}

			override fun successUpload(before: Category) {
				before.isUploaded = true
				localDataSource.soliteDao.updateCategory(before)
				callback.invoke(ApiResponse.success(true))
			}

			override fun dbFinish(savedData: Category) {
				callback.invoke(ApiResponse.finish(true))
			}
		}
	}

	override val variants: LiveData<Resource<List<Variant>>>
	get() {
		return object : NetworkBoundResource<List<Variant>, List<Variant>>(appExecutors){
			override fun loadFromDB(): LiveData<List<Variant>> {
				return localDataSource.soliteDao.getVariants()
			}

			override fun shouldFetch(data: List<Variant>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(): LiveData<ApiResponse<List<Variant>>> {
				return remoteDataSource.getVariants()
			}

			override fun saveCallResult(data: List<Variant>?) {
				if (!data.isNullOrEmpty()) localDataSource.soliteDao.insertVariants(data)
			}

		}.asLiveData()
	}

	override fun insertVariant(data: Variant, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, Variant, Long>(callback) {
			override fun dbOperation(): Variant{
				val id = localDataSource.soliteDao.insertVariant(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: Variant, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setVariant(savedData, inCallback)
			}

			override fun successUpload(before: Variant) {
				before.isUploaded = true
				localDataSource.soliteDao.updateVariant(before)
				callback.invoke(ApiResponse.success(before.id))
			}

			override fun dbFinish(savedData: Variant) {
				callback.invoke(ApiResponse.finish(savedData.id))
			}
		}
	}

	override fun updateVariant(data: Variant, callback: (ApiResponse<Boolean>) -> Unit) {
		object : NetworkFunBound<Boolean, Variant, Boolean>(callback) {
			override fun dbOperation(): Variant{
				data.isUploaded = true
				localDataSource.soliteDao.insertVariant(data)
				return data
			}

			override fun createCall(savedData: Variant, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setVariant(savedData, inCallback)
			}

			override fun successUpload(before: Variant) {
				before.isUploaded = true
				localDataSource.soliteDao.updateVariant(before)
				callback.invoke(ApiResponse.success(true))
			}

			override fun dbFinish(savedData: Variant) {
				callback.invoke(ApiResponse.finish(true))
			}
		}
	}

	override fun getVariantOptions(query: SupportSQLiteQuery): LiveData<Resource<List<VariantOption>>>{
		return object : NetworkBoundResource<List<VariantOption>, List<VariantOption>>(appExecutors){
			override fun loadFromDB(): LiveData<List<VariantOption>> {
				return localDataSource.soliteDao.getVariantOptions(query)
			}

			override fun shouldFetch(data: List<VariantOption>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(): LiveData<ApiResponse<List<VariantOption>>> {
				return remoteDataSource.variantOptions
			}

			override fun saveCallResult(data: List<VariantOption>?) {
				if (!data.isNullOrEmpty()) localDataSource.soliteDao.insertVariantOptions(data)
			}

		}.asLiveData()
	}

	override fun insertVariantOption(data: VariantOption, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, VariantOption, Long>(callback) {
			override fun dbOperation(): VariantOption{
				val id = localDataSource.soliteDao.insertVariantOption(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: VariantOption, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setVariantOption(savedData, inCallback)
			}

			override fun successUpload(before: VariantOption) {
				before.isUploaded = true
				localDataSource.soliteDao.updateVariantOption(before)
				callback.invoke(ApiResponse.success(before.id))
			}

			override fun dbFinish(savedData: VariantOption) {
				callback.invoke(ApiResponse.finish(savedData.id))
			}
		}
	}

	override fun updateVariantOption(data: VariantOption, callback: (ApiResponse<Boolean>) -> Unit) {
		object : NetworkFunBound<Boolean, VariantOption, Boolean>(callback) {
			override fun dbOperation(): VariantOption{
				data.isUploaded = false
				localDataSource.soliteDao.insertVariantOption(data)
				return data
			}

			override fun createCall(savedData: VariantOption, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setVariantOption(savedData, inCallback)
			}

			override fun successUpload(before: VariantOption) {
				before.isUploaded = true
				localDataSource.soliteDao.updateVariantOption(before)
				callback.invoke(ApiResponse.success(true))
			}

			override fun dbFinish(savedData: VariantOption) {
				callback.invoke(ApiResponse.finish(true))
			}
		}
	}

	override val customers: LiveData<Resource<List<Customer>>>
		get() {
			return object : NetworkBoundResource<List<Customer>, List<Customer>>(appExecutors){
				override fun loadFromDB(): LiveData<List<Customer>> {
					return localDataSource.soliteDao.getCustomers()
				}

				override fun shouldFetch(data: List<Customer>): Boolean {
					return data.isNullOrEmpty()
				}

				override fun createCall(): LiveData<ApiResponse<List<Customer>>> {
					return remoteDataSource.customers
				}

				override fun saveCallResult(data: List<Customer>?) {
					if (!data.isNullOrEmpty()) localDataSource.soliteDao.insertCustomers(data)
				}
			}.asLiveData()
		}

	override fun insertCustomer(data: Customer, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, Customer, Long>(callback) {
			override fun dbOperation(): Customer{
				val id = localDataSource.soliteDao.insertCustomer(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: Customer, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setCustomer(savedData, inCallback)
			}

			override fun successUpload(before: Customer) {
				before.isUploaded = true
				localDataSource.soliteDao.updateCustomer(before)
				callback.invoke(ApiResponse.success(before.id))
			}

			override fun dbFinish(savedData: Customer) {
				callback.invoke(ApiResponse.finish(savedData.id))
			}
		}
	}

	override fun updateCustomer(data: Customer, callback: (ApiResponse<Boolean>) -> Unit) {
		object : NetworkFunBound<Boolean, Customer, Boolean>(callback) {
			override fun dbOperation(): Customer{
				data.isUploaded = false
				localDataSource.soliteDao.updateCustomer(data)
				return data
			}

			override fun createCall(savedData: Customer, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setCustomer(savedData, inCallback)
			}

			override fun successUpload(before: Customer) {
				before.isUploaded = true
				localDataSource.soliteDao.updateCustomer(before)
				callback.invoke(ApiResponse.success(true))
			}

			override fun dbFinish(savedData: Customer) {
				callback.invoke(ApiResponse.finish(true))
			}
		}
	}

	override val suppliers: LiveData<Resource<List<Supplier>>>
	get() {
		return object : NetworkBoundResource<List<Supplier>, List<Supplier>>(appExecutors){
			override fun loadFromDB(): LiveData<List<Supplier>> {
				return localDataSource.soliteDao.getSuppliers()
			}

			override fun shouldFetch(data: List<Supplier>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(): LiveData<ApiResponse<List<Supplier>>> {
				return remoteDataSource.suppliers
			}

			override fun saveCallResult(data: List<Supplier>?) {
				if (!data.isNullOrEmpty()) localDataSource.soliteDao.insertSuppliers(data)
			}
		}.asLiveData()
	}

	override fun insertSupplier(data: Supplier, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, Supplier, Long>(callback) {
			override fun dbOperation(): Supplier{
				val id = localDataSource.soliteDao.insertSupplier(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: Supplier, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setSupplier(savedData, inCallback)
			}

			override fun successUpload(before: Supplier) {
				before.isUploaded = true
				localDataSource.soliteDao.updateSupplier(before)
				callback.invoke(ApiResponse.success(before.id))
			}

			override fun dbFinish(savedData: Supplier) {
				callback.invoke(ApiResponse.finish(savedData.id))
			}
		}
	}

	override fun updateSupplier(data: Supplier, callback: (ApiResponse<Boolean>) -> Unit) {
		object : NetworkFunBound<Boolean, Supplier, Boolean>(callback) {
			override fun dbOperation(): Supplier{
				data.isUploaded = false
				localDataSource.soliteDao.updateSupplier(data)
				return data
			}

			override fun createCall(savedData: Supplier, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setSupplier(savedData, inCallback)
			}

			override fun successUpload(before: Supplier) {
				before.isUploaded = true
				localDataSource.soliteDao.updateSupplier(before)
				callback.invoke(ApiResponse.success(true))
			}

			override fun dbFinish(savedData: Supplier) {
				callback.invoke(ApiResponse.finish(true))
			}
		}
	}

	override val payments: LiveData<Resource<List<Payment>>>
	get() {
		return object : NetworkBoundResource<List<Payment>, List<Payment>>(appExecutors){
			override fun loadFromDB(): LiveData<List<Payment>> {
				return localDataSource.soliteDao.getPayments()
			}

			override fun shouldFetch(data: List<Payment>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(): LiveData<ApiResponse<List<Payment>>> {
				return remoteDataSource.payments
			}

			override fun saveCallResult(data: List<Payment>?) {
				if (!data.isNullOrEmpty()) localDataSource.soliteDao.insertPayments(data)
			}

		}.asLiveData()
	}

	override fun insertPayment(data: Payment, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, Payment, Long>(callback) {
			override fun dbOperation(): Payment{
				val id = localDataSource.soliteDao.insertPayment(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: Payment, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setPayment(savedData, inCallback)
			}

			override fun successUpload(before: Payment) {
				before.isUploaded = true
				localDataSource.soliteDao.updatePayment(before)
				callback.invoke(ApiResponse.success(before.id))
			}

			override fun dbFinish(savedData: Payment) {
				callback.invoke(ApiResponse.finish(savedData.id))
			}
		}
	}

	override fun updatePayment(data: Payment, callback: (ApiResponse<Boolean>) -> Unit) {
		object : NetworkFunBound<Boolean, Payment, Boolean>(callback) {
			override fun dbOperation(): Payment{
				data.isUploaded = false
				localDataSource.soliteDao.updatePayment(data)
				return data
			}

			override fun createCall(savedData: Payment, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setPayment(savedData, inCallback)
			}

			override fun successUpload(before: Payment) {
				before.isUploaded = true
				localDataSource.soliteDao.updatePayment(before)
				callback.invoke(ApiResponse.success(true))
			}

			override fun dbFinish(savedData: Payment) {
				callback.invoke(ApiResponse.finish(true))
			}
		}
	}

	override fun getOutcomes(date: String): LiveData<Resource<List<Outcome>>> {
		return object : NetworkBoundResource<List<Outcome>, List<Outcome>>(appExecutors){
			override fun loadFromDB(): LiveData<List<Outcome>> {
				return localDataSource.soliteDao.getOutcome(date)
			}

			override fun shouldFetch(data: List<Outcome>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(): LiveData<ApiResponse<List<Outcome>>> {
				return remoteDataSource.outcomes
			}

			override fun saveCallResult(data: List<Outcome>?) {
				if (!data.isNullOrEmpty()) localDataSource.soliteDao.insertOutcomes(data)
			}

		}.asLiveData()
	}

	override fun insertOutcome(data: Outcome, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, Outcome, Long>(callback) {
			override fun dbOperation(): Outcome{
				val id = localDataSource.soliteDao.insertOutcome(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: Outcome, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setOutcome(savedData, inCallback)
			}

			override fun successUpload(before: Outcome) {
				before.isUploaded = true
				localDataSource.soliteDao.updateOutcome(before)
				callback.invoke(ApiResponse.success(before.id))
			}

			override fun dbFinish(savedData: Outcome) {
				callback.invoke(ApiResponse.finish(savedData.id))
			}
		}
	}

	override fun updateOutcome(data: Outcome, callback: (ApiResponse<Boolean>) -> Unit) {
		object : NetworkFunBound<Boolean, Outcome, Boolean>(callback) {
			override fun dbOperation(): Outcome{
				data.isUploaded = false
				localDataSource.soliteDao.updateOutcome(data)
				return data
			}

			override fun createCall(savedData: Outcome, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setOutcome(savedData, inCallback)
			}

			override fun successUpload(before: Outcome) {
				before.isUploaded = true
				localDataSource.soliteDao.updateOutcome(before)
				callback.invoke(ApiResponse.success(true))
			}

			override fun dbFinish(savedData: Outcome) {
				callback.invoke(ApiResponse.finish(true))
			}
		}
	}

	override fun getUsers(userId: String): LiveData<Resource<User?>> {
		return object : NetworkBoundResource<User?, List<User>>(appExecutors) {
			override fun loadFromDB(): LiveData<User?> {
				return localDataSource.soliteDao.getUser(userId)
			}

			override fun shouldFetch(data: User?): Boolean {
				return data == null
			}

			override fun createCall(): LiveData<ApiResponse<List<User>>> {
				return remoteDataSource.users
			}

			override fun saveCallResult(data: List<User>?) {
				if (!data.isNullOrEmpty()) localDataSource.soliteDao.insertUsers(data)
			}

		}.asLiveData()
	}
}
