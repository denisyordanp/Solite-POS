package com.sosialite.solite_pos.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.*
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import com.sosialite.solite_pos.data.source.remote.response.entity.BatchWithData
import com.sosialite.solite_pos.data.source.remote.response.entity.DataProductResponse
import com.sosialite.solite_pos.data.source.remote.response.entity.OrderResponse
import com.sosialite.solite_pos.data.source.remote.response.entity.PurchaseResponse
import com.sosialite.solite_pos.data.source.remote.response.helper.ApiResponse
import com.sosialite.solite_pos.data.source.remote.response.helper.StatusResponse

class RemoteDataSource {

	private val db = Firebase.firestore

	companion object {
		private var INSTANCE: RemoteDataSource? = null

		fun connect(): RemoteDataSource {
			if (INSTANCE == null) {
				INSTANCE = RemoteDataSource()
			}
			return INSTANCE!!
		}
	}

	fun getOrderDetail(): LiveData<ApiResponse<OrderResponse>> {
		val result: MediatorLiveData<ApiResponse<OrderResponse>> = MediatorLiveData()
		val data = OrderResponse()
		result.addSource(orders){ rOrders ->
			when (rOrders.status) {
				StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
					result.removeSource(orders)
					if (!rOrders.body.isNullOrEmpty()){
						data.order = rOrders.body

						result.addSource(payments){ rPayments ->
							when (rPayments.status) {
								StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
									result.removeSource(payments)
									if (!rPayments.body.isNullOrEmpty()){
										data.payments = rPayments.body

										result.addSource(orderPayments){ rOrderPayments ->
											when (rOrderPayments.status) {
												StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
													result.removeSource(orderPayments)
													if (!rOrderPayments.body.isNullOrEmpty()){
														data.orderPayment = rOrderPayments.body

														result.addSource(customers){ rCustomers ->
															when (rCustomers.status) {
																StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
																	result.removeSource(customers)
																	if (!rCustomers.body.isNullOrEmpty()){
																		data.customer = rCustomers.body

																		result.addSource(dataProducts){ product ->
																			when (product.status) {
																				StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
																					result.removeSource(dataProducts)
																					if (product.body != null){
																						data.products = product.body
																						result.value = ApiResponse.success(data)
																					}else{
																						result.value = ApiResponse.empty(null)
																					}
																				}
																				else -> {
																					result.value = ApiResponse.error(null)
																				}
																			}
																		}

																	}else{
																		result.value = ApiResponse.empty(null)
																	}
																}
																else -> {
																	result.value = ApiResponse.error(null)
																}
															}
														}

													}else{
														result.value = ApiResponse.empty(null)
													}
												}
												else -> {
													result.value = ApiResponse.error(null)
												}
											}
										}

									}else{
										result.value = ApiResponse.empty(null)
									}
								}
								else -> {
									result.value = ApiResponse.error(null)
								}
							}
						}

					}else{
						result.value = ApiResponse.empty(null)
					}
				}
				else -> {
					result.value = ApiResponse.error(null)
				}
			}
		}
		return result
	}

	private val orders: LiveData<ApiResponse<List<Order>>>
	get() {
		val result: MutableLiveData<ApiResponse<List<Order>>> = MutableLiveData()
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Order.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = Order.toListClass(it)
					if (data.isNullOrEmpty())
						result.value = ApiResponse.empty(null)
					else
						result.value = ApiResponse.success(data)
				}
				.addOnFailureListener {
					result.value = ApiResponse.error(null)
				}
		return result
	}

	fun setOrder(order: Order, callback: (ApiResponse<Boolean>) -> Unit) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Order.DB_NAME)
				.document(order.orderNo)
				.set(Order.toHashMap(order))
				.addOnSuccessListener {
					callback.invoke(ApiResponse.success(true))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
	}

	private val orderPayments: LiveData<ApiResponse<List<OrderPayment>>>
	get() {
		val result: MutableLiveData<ApiResponse<List<OrderPayment>>> = MutableLiveData()
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(OrderPayment.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = OrderPayment.toListClass(it)
					if (data.isNullOrEmpty())
						result.value = ApiResponse.empty(null)
					else
						result.value = ApiResponse.success(data)
				}
				.addOnFailureListener {
					result.value = ApiResponse.error(null)
				}
		return result
	}

	fun setOrderPayment(order: OrderPayment, callback: (ApiResponse<Boolean>) -> Unit) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(OrderPayment.DB_NAME)
				.document(order.id.toString())
				.set(OrderPayment.toHashMap(order))
				.addOnSuccessListener {
					callback.invoke(ApiResponse.success(true))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
	}

	val customers: LiveData<ApiResponse<List<Customer>>>
	get() {
		val result: MutableLiveData<ApiResponse<List<Customer>>> = MutableLiveData()
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Customer.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = Customer.toListClass(it)
					if (data.isNullOrEmpty())
						result.value = ApiResponse.empty(null)
					else
						result.value = ApiResponse.success(data)
				}
				.addOnFailureListener {
					result.value = ApiResponse.error(null)
				}
		return result
	}

	fun setCustomer(customer: Customer, callback: (ApiResponse<Boolean>) -> Unit) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Customer.DB_NAME)
				.document(customer.id.toString())
				.set(Customer.toHashMap(customer))
				.addOnSuccessListener {
					callback.invoke(ApiResponse.success(true))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
	}

	val purchaseDetails: LiveData<ApiResponse<PurchaseResponse>>
	get() {
		val result: MediatorLiveData<ApiResponse<PurchaseResponse>> = MediatorLiveData()
		val data = PurchaseResponse()
		result.addSource(suppliers){ rSuppliers ->
			when (rSuppliers.status) {
				StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
					result.removeSource(suppliers)
					if (rSuppliers.body != null){
						data.suppliers = rSuppliers.body

						result.addSource(purchases){ rPurchases ->
							when (rPurchases.status) {
								StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
									result.removeSource(purchases)
									if (rPurchases.body != null){
										data.purchases = rPurchases.body

										result.addSource(dataProducts){ rDataProducts ->
											when (rDataProducts.status) {
												StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
													result.removeSource(dataProducts)
													if (rDataProducts.body != null){
														data.products = rDataProducts.body

														result.addSource(purchaseProducts){ rPurchaseProducts ->
															when (rPurchaseProducts.status) {
																StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
																	result.removeSource(purchaseProducts)
																	if (rPurchaseProducts.body != null){
																		data.purchaseProducts = rPurchaseProducts.body
																		result.value = ApiResponse.success(data)
																	}else{
																		result.value = ApiResponse.empty(null)
																	}
																}
																else -> {
																	result.value = ApiResponse.error(null)
																}
															}
														}

													}else{
														result.value = ApiResponse.empty(null)
													}
												}
												else -> {
													result.value = ApiResponse.error(null)
												}
											}
										}

									}else{
										result.value = ApiResponse.empty(null)
									}
								}
								else -> {
									result.value = ApiResponse.error(null)
								}
							}
						}

					}else{
						result.value = ApiResponse.empty(null)
					}
				}
				else -> {
					result.value = ApiResponse.error(null)
				}
			}
		}
		return result
	}

	private val purchases: LiveData<ApiResponse<List<Purchase>>>
	get() {
		val result: MutableLiveData<ApiResponse<List<Purchase>>> = MutableLiveData()
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Purchase.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = Purchase.toListClass(it)
					if (data.isNullOrEmpty())
						result.value = ApiResponse.empty(null)
					else
						result.value = ApiResponse.success(data)
				}
				.addOnFailureListener {
					result.value = ApiResponse.error(null)
				}
		return result
	}

	private val purchaseProducts: LiveData<ApiResponse<List<PurchaseProduct>>>
		get() {
			val result: MutableLiveData<ApiResponse<List<PurchaseProduct>>> = MutableLiveData()
			db.collection(AppDatabase.DB_NAME)
					.document(AppDatabase.MAIN)
					.collection(PurchaseProduct.DB_NAME)
					.get()
					.addOnSuccessListener {
						val data = PurchaseProduct.toListClass(it)
						if (data.isNullOrEmpty())
							result.value = ApiResponse.empty(null)
						else
							result.value = ApiResponse.success(data)
					}
					.addOnFailureListener {
						result.value = ApiResponse.error(null)
					}
			return result
		}

	val outcomes: LiveData<ApiResponse<List<Outcome>>>
	get() {
		val result: MutableLiveData<ApiResponse<List<Outcome>>> = MutableLiveData()
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Outcome.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = Outcome.toListClass(it)
					if (data.isNullOrEmpty())
						result.value = ApiResponse.empty(null)
					else
						result.value = ApiResponse.success(data)
				}
				.addOnFailureListener {
					result.value = ApiResponse.error(null)
				}
		return result
	}

	fun setOutcome(outcome: Outcome, callback: (ApiResponse<Boolean>) -> Unit) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Outcome.DB_NAME)
				.document(outcome.id.toString())
				.set(Outcome.toHashMap(outcome))
				.addOnSuccessListener {
					callback.invoke(ApiResponse.success(true))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
	}

	val variantOptions: LiveData<ApiResponse<List<VariantOption>>>
	get() {
		val result: MutableLiveData<ApiResponse<List<VariantOption>>> = MutableLiveData()
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(VariantOption.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = VariantOption.toListClass(it)
					if (data.isNullOrEmpty())
						result.value = ApiResponse.empty(null)
					else
						result.value = ApiResponse.success(data)
				}
				.addOnFailureListener {
					result.value = ApiResponse.error(null)
				}
		return result
	}

	fun setVariantOption(option: VariantOption, callback: (ApiResponse<Boolean>) -> Unit) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(VariantOption.DB_NAME)
				.document(option.id.toString())
				.set(VariantOption.toHashMap(option))
				.addOnSuccessListener {
					callback.invoke(ApiResponse.success(true))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
	}

	fun getVariants(): LiveData<ApiResponse<List<Variant>>> {
		val result: MutableLiveData<ApiResponse<List<Variant>>> = MutableLiveData()
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Variant.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = Variant.toListClass(it)
					if (data.isNullOrEmpty())
						result.value = ApiResponse.empty(null)
					else
						result.value = ApiResponse.success(data)
				}
				.addOnFailureListener {
					result.value = ApiResponse.error(null)
				}
		return result
	}

	fun setVariant(variant: Variant, callback: (ApiResponse<Boolean>) -> Unit) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Variant.DB_NAME)
				.document(variant.id.toString())
				.set(Variant.toHashMap(variant))
				.addOnSuccessListener {
					callback.invoke(ApiResponse.success(true))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
	}

	fun getCategories(): LiveData<ApiResponse<List<Category>>> {
		val result: MutableLiveData<ApiResponse<List<Category>>> = MutableLiveData()
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Category.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = Category.toListClass(it)
					if (data.isNullOrEmpty())
						result.value = ApiResponse.empty(null)
					else
						result.value = ApiResponse.success(data)
				}
				.addOnFailureListener {
					result.value = ApiResponse.error(null)
				}
		return result
	}

	fun setCategory(category: Category, callback: (ApiResponse<Boolean>) -> Unit) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Category.DB_NAME)
				.document(category.id.toString())
				.set(Category.toHashMap(category))
				.addOnSuccessListener {
					callback.invoke(ApiResponse.success(true))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
	}

	val dataProducts: LiveData<ApiResponse<DataProductResponse>>
	get() {
		val result: MediatorLiveData<ApiResponse<DataProductResponse>> = MediatorLiveData()
		val data = DataProductResponse()
		result.addSource(getCategories()){ categories ->
			when (categories.status) {
				StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
					result.removeSource(getCategories())
					if (!categories.body.isNullOrEmpty()){
						data.categories = categories.body

						result.addSource(getProducts()){ products ->
							when (products.status) {
								StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
									result.removeSource(getProducts())
									if (!products.body.isNullOrEmpty()){
										data.products = products.body

										result.addSource(getVariants()){ variants ->
											when (variants.status) {
												StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
													result.removeSource(getVariants())
													if (!variants.body.isNullOrEmpty()){
														data.variants = variants.body

														result.addSource(variantOptions){
															when (it.status) {
																StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
																	result.removeSource(variantOptions)
																	if (!it.body.isNullOrEmpty()){
																		data.variantOptions = it.body
																		result.value = ApiResponse.success(data)
																	}else{
																		result.value = ApiResponse.empty(null)
																	}
																}
																else -> {
																	result.value = ApiResponse.error(null)
																}
															}
														}

													}else{
														result.value = ApiResponse.empty(null)
													}
												}
												else -> {
													result.value = ApiResponse.error(null)
												}
											}
										}

									}else{
										result.value = ApiResponse.empty(null)
									}
								}
								else -> {
									result.value = ApiResponse.error(null)
								}
							}
						}

					}else{
						result.value = ApiResponse.empty(null)
					}
				}
				else -> {
					result.value = ApiResponse.error(null)
				}
			}
		}
		return result
	}

	fun getProducts(): LiveData<ApiResponse<List<Product>>> {
		val result: MutableLiveData<ApiResponse<List<Product>>> = MutableLiveData()
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Product.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = Product.toListClass(it)
					if (data.isNullOrEmpty())
						result.value = ApiResponse.empty(null)
					else
						result.value = ApiResponse.success(data)
				}
				.addOnFailureListener {
					result.value = ApiResponse.error(null)
				}
		return result
	}

	fun setProduct(product: Product, callback: (ApiResponse<Boolean>) -> Unit) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Product.DB_NAME)
				.document(product.id.toString())
				.set(Product.toHashMap(product))
				.addOnSuccessListener {
					callback.invoke(ApiResponse.success(true))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
	}

	fun setVariantMix(mix: VariantMix, callback: (ApiResponse<Boolean>) -> Unit) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(VariantMix.DB_NAME)
				.document(mix.id.toString())
				.set(VariantMix.toHashMap(mix))
				.addOnSuccessListener {
					callback.invoke(ApiResponse.success(true))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
	}

	fun getVariantProducts(): LiveData<ApiResponse<List<VariantProduct>>> {
		val result: MutableLiveData<ApiResponse<List<VariantProduct>>> = MutableLiveData()
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(VariantProduct.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = VariantProduct.toListClass(it)
					if (data.isNullOrEmpty())
						result.value = ApiResponse.empty(null)
					else
						result.value = ApiResponse.success(data)
				}
				.addOnFailureListener {
					result.value = ApiResponse.error(null)
				}
		return result
	}

	fun setVariantProduct(product: VariantProduct, callback: (ApiResponse<Boolean>) -> Unit) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(VariantProduct.DB_NAME)
				.document(product.id.toString())
				.set(VariantProduct.toHashMap(product))
				.addOnSuccessListener {
					callback.invoke(ApiResponse.success(true))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
	}

	fun deleteVariantMix(mix: VariantMix, callback: (ApiResponse<Boolean>) -> Unit) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(VariantMix.DB_NAME)
				.document(mix.id.toString())
				.delete()
				.addOnSuccessListener {
					callback.invoke(ApiResponse.success(true))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
	}

	val suppliers: LiveData<ApiResponse<List<Supplier>>>
	get() {
		val result: MutableLiveData<ApiResponse<List<Supplier>>> = MutableLiveData()
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Supplier.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = Supplier.toListClass(it)
					if (data.isNullOrEmpty())
						result.value = ApiResponse.empty(null)
					else
						result.value = ApiResponse.success(data)
				}
				.addOnFailureListener {
					result.value = ApiResponse.error(null)
				}
		return result
	}

	fun setSupplier(supplier: Supplier, callback: (ApiResponse<Boolean>) -> Unit) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Supplier.DB_NAME)
				.document(supplier.id.toString())
				.set(Supplier.toHashMap(supplier))
				.addOnSuccessListener {
					callback.invoke(ApiResponse.success(true))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
	}

	val payments: LiveData<ApiResponse<List<Payment>>>
	get() {
		val result: MutableLiveData<ApiResponse<List<Payment>>> = MutableLiveData()
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Payment.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = Payment.toListClass(it)
					if (data.isNullOrEmpty())
						result.value = ApiResponse.empty(null)
					else
						result.value = ApiResponse.success(data)
				}
				.addOnFailureListener {
					result.value = ApiResponse.error(null)
				}
		return result
	}

	fun setPayment(payment: Payment, callback: (ApiResponse<Boolean>) -> Unit) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Payment.DB_NAME)
				.document(payment.id.toString())
				.set(Payment.toHashMap(payment))
				.addOnSuccessListener {
					callback.invoke(ApiResponse.success(true))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
	}

	fun batch(batches: ArrayList<BatchWithData>, callback: (ApiResponse<Boolean>) -> Unit){
		db.runBatch {
			for (batch in batches){
				it.set(batch.doc, batch.hashMap)
			}
		}.addOnCompleteListener {
			if (it.isSuccessful){
				callback.invoke(ApiResponse.success(true))
			}else{
				callback.invoke(ApiResponse.error(null))
			}
		}
	}
}
