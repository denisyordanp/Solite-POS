package com.socialite.solite_pos.data.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.socialite.solite_pos.data.source.local.entity.room.bridge.*
import com.socialite.solite_pos.data.source.local.entity.room.master.*
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.remote.response.entity.*
import com.socialite.solite_pos.data.source.remote.response.helper.ApiResponse
import com.socialite.solite_pos.data.source.remote.response.helper.StatusResponse

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
					data.order = rOrders.body ?: emptyList()

					result.addSource(payments){ rPayments ->
						when (rPayments.status) {
							StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
								result.removeSource(payments)
								data.payments = rPayments.body ?: emptyList()

								result.addSource(orderPayments){ rOrderPayments ->
									when (rOrderPayments.status) {
										StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
											result.removeSource(orderPayments)
											data.orderPayment = rOrderPayments.body ?: emptyList()

											result.addSource(customers){ rCustomers ->
												when (rCustomers.status) {
													StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
														result.removeSource(customers)
														data.customer = rCustomers.body ?: emptyList()
														result.value = ApiResponse.success(data)
													}
													else -> {
														result.value = ApiResponse.error(null)
													}
												}
											}
										}
										else -> {
											result.value = ApiResponse.error(null)
										}
									}
								}
							}
							else -> {
								result.value = ApiResponse.error(null)
							}
						}
					}
				}
				else -> {
					result.value = ApiResponse.error(null)
				}
			}
		}
		return result
	}

	fun getProductOrder(): LiveData<ApiResponse<OrderProductResponse>> {
		val result: MediatorLiveData<ApiResponse<OrderProductResponse>> = MediatorLiveData()
		val data = OrderProductResponse()
		result.addSource(dataProducts){ rProducts ->
			when (rProducts.status) {
				StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
					result.removeSource(dataProducts)
					data.products = rProducts.body ?: DataProductResponse()

					result.addSource(details){ rDetail ->
						when (rDetail.status) {
							StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
								result.removeSource(details)
								data.details = rDetail.body ?: emptyList()

								result.addSource(orderMixVariants){ rMixes ->
									when (rMixes.status) {
										StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
											result.removeSource(orderMixVariants)
											data.mixOrders = rMixes.body ?: emptyList()

											result.addSource(orderMixProductsVariant){ rOrderMixProductsVariant ->
												when (rOrderMixProductsVariant.status) {
													StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
														result.removeSource(orderMixProductsVariant)
														data.mixVariants = rOrderMixProductsVariant.body ?: emptyList()

														result.addSource(orderVariants){ rOrderVariants ->
															when (rOrderVariants.status) {
																StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
																	result.removeSource(orderVariants)
																	data.variants = rOrderVariants.body ?: emptyList()
																	result.value = ApiResponse.success(data)
																}
																else -> {
																	result.value = ApiResponse.error(null)
																}
															}
														}

													}
													else -> {
														result.value = ApiResponse.error(null)
													}
												}
											}
										}
										else -> {
											result.value = ApiResponse.error(null)
										}
									}
								}
							}
							else -> {
								result.value = ApiResponse.error(null)
							}
						}
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

	private val details: LiveData<ApiResponse<List<OrderDetail>>>
		get() {
			val result: MutableLiveData<ApiResponse<List<OrderDetail>>> = MutableLiveData()
			db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(OrderDetail.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = OrderDetail.toListClass(it)
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

	private val orderMixVariants: LiveData<ApiResponse<List<OrderProductVariantMix>>>
		get() {
			val result: MutableLiveData<ApiResponse<List<OrderProductVariantMix>>> = MutableLiveData()
			db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(OrderProductVariantMix.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = OrderProductVariantMix.toListClass(it)
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

	private val orderMixProductsVariant: LiveData<ApiResponse<List<OrderMixProductVariant>>>
		get() {
			val result: MutableLiveData<ApiResponse<List<OrderMixProductVariant>>> = MutableLiveData()
			db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(OrderMixProductVariant.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = OrderMixProductVariant.toListClass(it)
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

	private val orderVariants: LiveData<ApiResponse<List<OrderProductVariant>>>
		get() {
			val result: MutableLiveData<ApiResponse<List<OrderProductVariant>>> = MutableLiveData()
			db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(OrderProductVariant.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = OrderProductVariant.toListClass(it)
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

	val purchaseDetails: LiveData<ApiResponse<PurchaseResponse>>
	get() {
		val result: MediatorLiveData<ApiResponse<PurchaseResponse>> = MediatorLiveData()
		val data = PurchaseResponse()
		result.addSource(suppliers){ rSuppliers ->
			when (rSuppliers.status) {
				StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
					result.removeSource(suppliers)
					data.suppliers = rSuppliers.body ?: emptyList()

					result.addSource(purchases){ rPurchases ->
						when (rPurchases.status) {
							StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
								result.removeSource(purchases)
								data.purchases = rPurchases.body ?: emptyList()
								result.value = ApiResponse.success(data)

							}
							else -> {
								result.value = ApiResponse.error(null)
							}
						}
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

	val purchaseProductsWithProducts: LiveData<ApiResponse<PurchaseProductResponse>>
	get() {
		val result: MediatorLiveData<ApiResponse<PurchaseProductResponse>> = MediatorLiveData()
		val data = PurchaseProductResponse()
		result.addSource(purchaseProducts){ rPurchaseProducts ->
			when (rPurchaseProducts.status) {
				StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
					result.removeSource(purchaseProducts)
					data.purchases = rPurchaseProducts.body ?: emptyList()

					result.addSource(dataProducts){ rProducts ->
						when (rProducts.status) {
							StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
								result.removeSource(dataProducts)
								data.products = rProducts.body ?: DataProductResponse()
								result.value = ApiResponse.success(data)

							}
							else -> {
								result.value = ApiResponse.error(null)
							}
						}
					}
				}
				else -> {
					result.value = ApiResponse.error(null)
				}
			}
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
					data.categories = categories.body ?: emptyList()

					result.addSource(getVariants()){ variants ->
						when (variants.status) {
							StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
								result.removeSource(getVariants())
								data.variants = variants.body ?: emptyList()

								result.addSource(variantOptions){ options ->
									when (options.status) {
										StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
											result.removeSource(variantOptions)
											data.variantOptions = options.body ?: emptyList()

											result.addSource(getProducts()){ products ->
												when (products.status) {
													StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
														result.removeSource(getProducts())

														data.products = products.body ?: emptyList()
														result.value = ApiResponse.success(data)
													}
													else -> {
														result.value = ApiResponse.error(null)
													}
												}
											}
										}
										else -> {
											result.value = ApiResponse.error(null)
										}
									}
								}
							}
							else -> {
								result.value = ApiResponse.error(null)
							}
						}
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

	val variantMixes: LiveData<ApiResponse<List<VariantMix>>>
	get() {
		val result: MutableLiveData<ApiResponse<List<VariantMix>>> = MutableLiveData()
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(VariantMix.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = VariantMix.toListClass(it)
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

	fun deleteVariantProduct(product: VariantProduct, callback: (ApiResponse<Boolean>) -> Unit) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(VariantProduct.DB_NAME)
				.document(product.id.toString())
				.delete()
				.addOnSuccessListener {
					callback.invoke(ApiResponse.success(true))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
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
