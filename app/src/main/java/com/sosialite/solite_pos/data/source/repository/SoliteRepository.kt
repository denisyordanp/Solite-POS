package com.sosialite.solite_pos.data.source.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sosialite.solite_pos.data.NetworkBoundResource
import com.sosialite.solite_pos.data.NetworkFunBound
import com.sosialite.solite_pos.data.NetworkGetBound
import com.sosialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.sosialite.solite_pos.data.source.local.entity.helper.PurchaseProductWithProduct
import com.sosialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.*
import com.sosialite.solite_pos.data.source.local.entity.room.helper.DataProduct
import com.sosialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.sosialite.solite_pos.data.source.local.entity.room.helper.VariantWithVariantMix
import com.sosialite.solite_pos.data.source.local.entity.room.master.*
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import com.sosialite.solite_pos.data.source.local.room.SoliteDao
import com.sosialite.solite_pos.data.source.remote.RemoteDataSource
import com.sosialite.solite_pos.data.source.remote.response.entity.BatchWithData
import com.sosialite.solite_pos.data.source.remote.response.entity.BatchWithObject
import com.sosialite.solite_pos.data.source.remote.response.entity.DataProductResponse
import com.sosialite.solite_pos.data.source.remote.response.helper.ApiResponse
import com.sosialite.solite_pos.data.source.remote.response.helper.StatusResponse
import com.sosialite.solite_pos.utils.database.AppExecutors
import com.sosialite.solite_pos.vo.Resource

class SoliteRepository private constructor(
		private val remoteDataSource: RemoteDataSource,
		private val soliteDao: SoliteDao,
		private val appExecutors: AppExecutors
) : SoliteDataSource {

	companion object {
		@Volatile
		private var INSTANCE: SoliteRepository? = null

		fun getInstance(remoteData: RemoteDataSource, soliteDao: SoliteDao, appExecutors: AppExecutors): SoliteRepository {
			if (INSTANCE == null) {
				synchronized(SoliteRepository::class.java) {
					if (INSTANCE == null) {
						INSTANCE = SoliteRepository(remoteData, soliteDao, appExecutors)
					}
				}
			}
			return INSTANCE!!
		}
	}

	override fun getOrderDetail(status: Int, date: String): List<OrderWithProduct>{
		val orders = soliteDao.getOrdersByStatus(status, date)
		val list: ArrayList<OrderWithProduct> = ArrayList()
		for (item in orders){
			list.add(soliteDao.getOrderWithProduct(item))
		}
		return list
	}

	override fun insertPaymentOrder(payment: OrderPayment): OrderWithProduct{
		soliteDao.insertPaymentOrder(payment)
		return soliteDao.getOrderDetail(payment.orderNO)
	}

	override fun newOrder(order: OrderWithProduct, callback: (ApiResponse<Boolean>) -> Unit) {

		val batches: ArrayList<BatchWithData> = ArrayList()

		batches.add(insertOrder(order.order).batch)
		for (item in order.products){
			if (item.product != null){

				val detail = insertDetailOrder(
						OrderDetail(order.order.orderNo, item.product!!.id, item.amount)
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

//		soliteDao.insertOrder(order.order)
//		for (item in order.products){
//			if (item.product != null){
//
//				val idOrder = soliteDao.insertDetailOrder(OrderDetail(order.order.orderNo, item.product!!.id, item.amount))
//
//				if (item.product!!.isMix){
//					for (p in item.mixProducts){
//						soliteDao.decreaseProductStock(p.product.id, p.amount)
//
//						val idMix = soliteDao.insertVariantMixOrder(OrderProductVariantMix(idOrder, p.product.id, p.amount))
//
//						for (variant in p.variants){
//							soliteDao.insertMixVariantOrder(OrderMixProductVariant(idMix, variant.id))
//						}
//					}
//				}else{
//					soliteDao.decreaseProductStock(item.product!!.id, (item.amount * item.product!!.portion))
//
//					for (variant in item.variants){
//						soliteDao.insertVariantOrder(OrderProductVariant(idOrder, variant.id))
//					}
//				}
//			}
//		}
	}

	private fun insertOrder(order: Order)
	: BatchWithObject<Order> {
		soliteDao.insertOrder(order)
		val doc = Firebase.firestore
				.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Order.DB_NAME)
				.document(order.orderNo)
		return BatchWithObject(order, BatchWithData(doc, Order.toHashMap(order)))
	}

	private fun insertDetailOrder(detail: OrderDetail)
	: BatchWithObject<OrderDetail> {
		detail.id = soliteDao.insertDetailOrder(detail)
		val doc = Firebase.firestore
				.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(OrderDetail.DB_NAME)
				.document(detail.id.toString())
		return BatchWithObject(detail, BatchWithData(doc, OrderDetail.toHashMap(detail)))
	}

	private fun decreaseStock(idProduct: Long, amount: Int)
	: BatchWithObject<Product> {
		val product = soliteDao.decreaseAndGetProduct(idProduct, amount)
		val doc = Firebase.firestore
				.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Product.DB_NAME)
				.document(product.id.toString())
		return BatchWithObject(product, BatchWithData(doc, Product.toHashMap(product)))
	}

	private fun insertVariantMixOrder(variant: OrderProductVariantMix)
	: BatchWithObject<OrderProductVariantMix> {
		variant.id = soliteDao.insertVariantMixOrder(variant)
		val doc = Firebase.firestore
				.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(OrderProductVariantMix.DB_NAME)
				.document(variant.id.toString())
		return BatchWithObject(variant, BatchWithData(doc, OrderProductVariantMix.toHashMap(variant)))
	}

	private fun insertMixVariantOrder(mixVariant: OrderMixProductVariant)
	: BatchWithObject<OrderMixProductVariant> {
		mixVariant.id = soliteDao.insertMixVariantOrder(mixVariant)
		val doc = Firebase.firestore
				.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(OrderMixProductVariant.DB_NAME)
				.document(mixVariant.id.toString())
		return BatchWithObject(mixVariant, BatchWithData(doc, OrderMixProductVariant.toHashMap(mixVariant)))
	}

	private fun insertVariantOrder(variant: OrderProductVariant)
	: BatchWithObject<OrderProductVariant> {
		variant.id = soliteDao.insertVariantOrder(variant)
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
				soliteDao.updateOrder(order)
				return order
			}

			override fun createCall(savedData: Order, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setOrder(savedData, inCallback)
			}

			override fun successUpload(before: Order) {
				before.isUploaded = true
				soliteDao.updateOrder(before)
				callback.invoke(ApiResponse.success(true))
			}

			override fun dbFinish(savedData: Order) {
				callback.invoke(ApiResponse.finish(true))
			}
		}
	}

	override fun cancelOrder(order: OrderWithProduct){
		soliteDao.updateOrder(order.order)
		for (item in order.products){
			if (item.product != null){

				if (item.product!!.isMix){
					for (p in item.mixProducts){
						soliteDao.increaseProductStock(p.product.id, p.amount)
					}
				}else{
					soliteDao.increaseProductStock(item.product!!.id, (item.amount * item.product!!.portion))
				}
			}
		}
	}

	override fun getPurchase(): List<PurchaseWithProduct> {
		val purchases = soliteDao.getPurchases()
		val list: ArrayList<PurchaseWithProduct> = ArrayList()
		for (purchase in purchases){
			val purchaseProduct = soliteDao.getPurchasesProduct(purchase.purchaseNo)
			val supplier = soliteDao.getSupplierById(purchase.idSupplier)
			val array: ArrayList<PurchaseProductWithProduct> = ArrayList()
			for (products in purchaseProduct){
				val product = soliteDao.getProduct(products.idProduct)
				array.add(PurchaseProductWithProduct(products, product))
			}
			list.add(PurchaseWithProduct(purchase, supplier, array))
		}
		return list
	}

	override fun newPurchase(data: PurchaseWithProduct) {
		soliteDao.insertPurchase(data.purchase)
		soliteDao.insertPurchaseProduct(data.purchaseProduct)
		for (product in data.products){
			if (product.purchaseProduct != null){
				soliteDao.increaseProductStock(product.purchaseProduct!!.idProduct, product.purchaseProduct!!.amount)
			}
		}
	}

	override fun getDataProduct(idCategory: Long): LiveData<Resource<List<DataProduct>>> {
		return object : NetworkBoundResource<List<DataProduct>, DataProductResponse>(appExecutors){
			override fun loadFromDB(): LiveData<List<DataProduct>> {
				return soliteDao.getDataProduct(idCategory)
			}

			override fun shouldFetch(data: List<DataProduct>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(): LiveData<ApiResponse<DataProductResponse>> {
				return remoteDataSource.getDataProduct()
			}

			override fun saveCallResult(data: DataProductResponse?) {
				if (data != null){
					soliteDao.insertProducts(data.products)
					soliteDao.insertCategories(data.categories)
					soliteDao.insertVariantOptions(data.variants)
				}
			}
		}.asLiveData()
	}

	override fun getVariantProduct(idProduct: Long, idVariantOption: Long): List<VariantProduct>{
		return soliteDao.getVariantProduct(idProduct, idVariantOption)
	}

	override fun getVariantProductById(idProduct: Long): VariantProduct?{
		return soliteDao.getVariantProductById(idProduct)
	}

	override fun insertVariantProduct(data: VariantProduct, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, VariantProduct, Long>(callback) {
			override fun dbOperation(): VariantProduct{
				val id = soliteDao.insertVariantProduct(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: VariantProduct, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setVariantProduct(savedData, inCallback)
			}

			override fun successUpload(before: VariantProduct) {
				before.isUploaded = true
				soliteDao.updateVariantProduct(before)
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
				soliteDao.removeVariantProduct(data.idVariantOption, data.idProduct)
				return data
			}

			override fun createCall(savedData: VariantProduct, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setVariantProduct(savedData, inCallback)
			}

			override fun successUpload(before: VariantProduct) {
				callback.invoke(ApiResponse.success(true))
			}

			override fun dbFinish(savedData: VariantProduct) {
				callback.invoke(ApiResponse.finish(true))
			}
		}
	}

	override fun getLiveVariantMixProduct(idVariant: Long): LiveData<Resource<VariantWithVariantMix>>{
		return object : NetworkBoundResource<VariantWithVariantMix, List<Product>>(appExecutors){
			override fun loadFromDB(): LiveData<VariantWithVariantMix> {
				return soliteDao.getLiveVariantMixProduct(idVariant)
			}

			override fun shouldFetch(data: VariantWithVariantMix): Boolean {
				return data.products.isNullOrEmpty()
			}

			override fun createCall(): LiveData<ApiResponse<List<Product>>> {
				return remoteDataSource.getProducts()
			}

			override fun saveCallResult(data: List<Product>?) {
				if (!data.isNullOrEmpty()) soliteDao.insertProducts(data)
			}
		}.asLiveData()
	}

	override fun getVariantMixProduct(idVariant: Long): VariantWithVariantMix {
		return soliteDao.getVariantMixProduct(idVariant)
	}

	override fun insertVariantMix(data: VariantMix, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, VariantMix, Long>(callback) {
			override fun dbOperation(): VariantMix{
				val id = soliteDao.insertVariantMix(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: VariantMix, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setVariantMix(savedData, inCallback)
			}

			override fun successUpload(before: VariantMix) {
				before.isUploaded = true
				soliteDao.updateVariantMix(before)
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
				soliteDao.removeVariantMix(data.idVariant, data.idProduct)
				return data
			}

			override fun createCall(savedData: VariantMix, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.deleteVariantMix(savedData, inCallback)
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
				return soliteDao.getProductWithCategories(category)
			}

			override fun shouldFetch(data: List<ProductWithCategory>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(): LiveData<ApiResponse<List<Product>>> {
				return remoteDataSource.getProducts()
			}

			override fun saveCallResult(data: List<Product>?) {
				if (!data.isNullOrEmpty()) soliteDao.insertProducts(data)
			}

		}.asLiveData()
	}

	override fun insertProduct(data: Product, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, Product, Long>(callback) {
			override fun dbOperation(): Product{
				val id = soliteDao.insertProduct(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: Product, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setProduct(savedData, inCallback)
			}

			override fun successUpload(before: Product) {
				before.isUploaded = true
				soliteDao.updateProduct(before)
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
				soliteDao.insertProduct(data)
				return data
			}

			override fun createCall(savedData: Product, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setProduct(savedData, inCallback)
			}

			override fun successUpload(before: Product) {
				before.isUploaded = true
				soliteDao.updateProduct(before)
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
				return soliteDao.getCategories(query)
			}

			override fun shouldFetch(data: List<Category>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(): LiveData<ApiResponse<List<Category>>> {
				return remoteDataSource.getCategories()
			}

			override fun saveCallResult(data: List<Category>?) {
				if (!data.isNullOrEmpty()) soliteDao.insertCategories(data)
			}

		}.asLiveData()
	}

	override fun insertCategory(data: Category, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, Category, Long>(callback) {
			override fun dbOperation(): Category{
				val id = soliteDao.insertCategory(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: Category, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setCategory(savedData, inCallback)
			}

			override fun successUpload(before: Category) {
				before.isUploaded = true
				soliteDao.updateCategory(before)
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
				soliteDao.insertCategory(data)
				return data
			}

			override fun createCall(savedData: Category, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setCategory(savedData, inCallback)
			}

			override fun successUpload(before: Category) {
				before.isUploaded = true
				soliteDao.updateCategory(before)
				callback.invoke(ApiResponse.success(true))
			}

			override fun dbFinish(savedData: Category) {
				callback.invoke(ApiResponse.finish(true))
			}
		}
	}

	override fun getVariants(callback: (ApiResponse<List<Variant>>) -> Unit){
		object : NetworkGetBound<List<Variant>>(callback){
			override fun dbOperation(): List<Variant> {
				return soliteDao.getVariants()
			}

			override fun shouldCall(data: List<Variant>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(inCallback: (ApiResponse<List<Variant>>) -> Unit) {
				remoteDataSource.getVariants(inCallback)
			}

			override fun successCall(result: List<Variant>) {
				soliteDao.insertVariants(result)
			}

			override fun dbFinish(savedData: ApiResponse<List<Variant>>) {
				callback.invoke(savedData)
			}
		}
	}

	override fun insertVariant(data: Variant, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, Variant, Long>(callback) {
			override fun dbOperation(): Variant{
				val id = soliteDao.insertVariant(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: Variant, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setVariant(savedData, inCallback)
			}

			override fun successUpload(before: Variant) {
				before.isUploaded = true
				soliteDao.updateVariant(before)
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
				soliteDao.insertVariant(data)
				return data
			}

			override fun createCall(savedData: Variant, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setVariant(savedData, inCallback)
			}

			override fun successUpload(before: Variant) {
				before.isUploaded = true
				soliteDao.updateVariant(before)
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
				return soliteDao.getVariantOptions(query)
			}

			override fun shouldFetch(data: List<VariantOption>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(): LiveData<ApiResponse<List<VariantOption>>> {
				return remoteDataSource.getVariantOptions()
			}

			override fun saveCallResult(data: List<VariantOption>?) {
				if (!data.isNullOrEmpty()) soliteDao.insertVariantOptions(data)
			}

		}.asLiveData()
	}

	override fun insertVariantOption(data: VariantOption, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, VariantOption, Long>(callback) {
			override fun dbOperation(): VariantOption{
				val id = soliteDao.insertVariantOption(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: VariantOption, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setVariantOption(savedData, inCallback)
			}

			override fun successUpload(before: VariantOption) {
				before.isUploaded = true
				soliteDao.updateVariantOption(before)
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
				soliteDao.insertVariantOption(data)
				return data
			}

			override fun createCall(savedData: VariantOption, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setVariantOption(savedData, inCallback)
			}

			override fun successUpload(before: VariantOption) {
				before.isUploaded = true
				soliteDao.updateVariantOption(before)
				callback.invoke(ApiResponse.success(true))
			}

			override fun dbFinish(savedData: VariantOption) {
				callback.invoke(ApiResponse.finish(true))
			}
		}
	}

	override fun getCustomers(callback: (ApiResponse<List<Customer>>) -> Unit){
		object : NetworkGetBound<List<Customer>>(callback){
			override fun dbOperation(): List<Customer> {
				return soliteDao.getCustomers()
			}

			override fun shouldCall(data: List<Customer>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(inCallback: (ApiResponse<List<Customer>>) -> Unit) {
				remoteDataSource.getCustomer(inCallback)
			}

			override fun successCall(result: List<Customer>) {
				soliteDao.insertCustomers(result)
			}

			override fun dbFinish(savedData: ApiResponse<List<Customer>>) {
				callback.invoke(savedData)
			}
		}
	}

	override fun insertCustomer(data: Customer, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, Customer, Long>(callback) {
			override fun dbOperation(): Customer{
				val id = soliteDao.insertCustomer(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: Customer, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setCustomer(savedData, inCallback)
			}

			override fun successUpload(before: Customer) {
				before.isUploaded = true
				soliteDao.updateCustomer(before)
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
				soliteDao.updateCustomer(data)
				return data
			}

			override fun createCall(savedData: Customer, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setCustomer(savedData, inCallback)
			}

			override fun successUpload(before: Customer) {
				before.isUploaded = true
				soliteDao.updateCustomer(before)
				callback.invoke(ApiResponse.success(true))
			}

			override fun dbFinish(savedData: Customer) {
				callback.invoke(ApiResponse.finish(true))
			}
		}
	}

	override fun getSuppliers(callback: (ApiResponse<List<Supplier>>) -> Unit){
		object : NetworkGetBound<List<Supplier>>(callback){
			override fun dbOperation(): List<Supplier> {
				return soliteDao.getSuppliers()
			}

			override fun shouldCall(data: List<Supplier>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(inCallback: (ApiResponse<List<Supplier>>) -> Unit) {
				remoteDataSource.getSuppliers(inCallback)
			}

			override fun successCall(result: List<Supplier>) {
				soliteDao.insertSuppliers(result)
			}

			override fun dbFinish(savedData: ApiResponse<List<Supplier>>) {
				callback.invoke(savedData)
			}
		}
	}

	override fun insertSupplier(data: Supplier, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, Supplier, Long>(callback) {
			override fun dbOperation(): Supplier{
				val id = soliteDao.insertSupplier(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: Supplier, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setSupplier(savedData, inCallback)
			}

			override fun successUpload(before: Supplier) {
				before.isUploaded = true
				soliteDao.updateSupplier(before)
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
				soliteDao.updateSupplier(data)
				return data
			}

			override fun createCall(savedData: Supplier, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setSupplier(savedData, inCallback)
			}

			override fun successUpload(before: Supplier) {
				before.isUploaded = true
				soliteDao.updateSupplier(before)
				callback.invoke(ApiResponse.success(true))
			}

			override fun dbFinish(savedData: Supplier) {
				callback.invoke(ApiResponse.finish(true))
			}
		}
	}

	override fun getPayments(callback: (ApiResponse<List<Payment>>) -> Unit){
		object : NetworkGetBound<List<Payment>>(callback){
			override fun dbOperation(): List<Payment> {
				return soliteDao.getPayments()
			}

			override fun shouldCall(data: List<Payment>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(inCallback: (ApiResponse<List<Payment>>) -> Unit) {
				remoteDataSource.getPayments(inCallback)
			}

			override fun successCall(result: List<Payment>) {
				soliteDao.insertPayments(result)
			}

			override fun dbFinish(savedData: ApiResponse<List<Payment>>) {
				callback.invoke(savedData)
			}
		}
	}

	override fun insertPayment(data: Payment, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, Payment, Long>(callback) {
			override fun dbOperation(): Payment{
				val id = soliteDao.insertPayment(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: Payment, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setPayment(savedData, inCallback)
			}

			override fun successUpload(before: Payment) {
				before.isUploaded = true
				soliteDao.updatePayment(before)
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
				soliteDao.updatePayment(data)
				return data
			}

			override fun createCall(savedData: Payment, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setPayment(savedData, inCallback)
			}

			override fun successUpload(before: Payment) {
				before.isUploaded = true
				soliteDao.updatePayment(before)
				callback.invoke(ApiResponse.success(true))
			}

			override fun dbFinish(savedData: Payment) {
				callback.invoke(ApiResponse.finish(true))
			}
		}
	}

	override fun getOutcomes(date: String, callback: (ApiResponse<List<Outcome>>) -> Unit) {
		object : NetworkGetBound<List<Outcome>>(callback){
			override fun dbOperation(): List<Outcome> {
				return soliteDao.getOutcome(date)
			}

			override fun shouldCall(data: List<Outcome>): Boolean {
				return data.isNullOrEmpty()
			}

			override fun createCall(inCallback: (ApiResponse<List<Outcome>>) -> Unit) {
				remoteDataSource.getOutcomes(inCallback)
			}

			override fun successCall(result: List<Outcome>) {
				soliteDao.insertOutcomes(result)
			}

			override fun dbFinish(savedData: ApiResponse<List<Outcome>>) {
				callback.invoke(savedData)
			}
		}
	}

	override fun insertOutcome(data: Outcome, callback: (ApiResponse<Long>) -> Unit) {
		object : NetworkFunBound<Boolean, Outcome, Long>(callback) {
			override fun dbOperation(): Outcome{
				val id = soliteDao.insertOutcome(data)
				data.id = id
				return data
			}

			override fun createCall(savedData: Outcome, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setOutcome(savedData, inCallback)
			}

			override fun successUpload(before: Outcome) {
				before.isUploaded = true
				soliteDao.updateOutcome(before)
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
				soliteDao.updateOutcome(data)
				return data
			}

			override fun createCall(savedData: Outcome, inCallback: (ApiResponse<Boolean>) -> Unit) {
				return remoteDataSource.setOutcome(savedData, inCallback)
			}

			override fun successUpload(before: Outcome) {
				before.isUploaded = true
				soliteDao.updateOutcome(before)
				callback.invoke(ApiResponse.success(true))
			}

			override fun dbFinish(savedData: Outcome) {
				callback.invoke(ApiResponse.finish(true))
			}
		}
	}

	override fun fillData(){
		soliteDao.fillData()
	}
}
