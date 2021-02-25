package com.sosialite.solite_pos.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.sosialite.solite_pos.data.source.local.entity.room.helper.DataProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.*
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import com.sosialite.solite_pos.data.source.remote.response.entity.BatchWithData
import com.sosialite.solite_pos.data.source.remote.response.entity.DataProductResponse
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

	fun getCustomer(callback: ((ApiResponse<List<Customer>>) -> Unit)) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Customer.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = Customer.toListClass(it)
					if (data.isNullOrEmpty())
						callback.invoke(ApiResponse.empty(null))
					else
						callback.invoke(ApiResponse.success(data))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
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

	fun getOutcomes(callback: ((ApiResponse<List<Outcome>>) -> Unit)) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Outcome.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = Outcome.toListClass(it)
					if (data.isNullOrEmpty())
						callback.invoke(ApiResponse.empty(null))
					else
						callback.invoke(ApiResponse.success(data))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
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

	fun getVariantOptions(): LiveData<ApiResponse<List<VariantOption>>> {
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

	fun getVariants(callback: ((ApiResponse<List<Variant>>) -> Unit)) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Variant.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = Variant.toListClass(it)
					if (data.isNullOrEmpty())
						callback.invoke(ApiResponse.empty(null))
					else
						callback.invoke(ApiResponse.success(Variant.toListClass(it)))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
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
						result.value = ApiResponse.success(data)
					else
						result.value = ApiResponse.empty(null)
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

	fun getDataProduct(): LiveData<ApiResponse<DataProductResponse>> {
		val result: MediatorLiveData<ApiResponse<DataProductResponse>> = MediatorLiveData()
		val data = DataProductResponse()
		result.addSource(getCategories()){categories ->
			when (categories.status) {
				StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
					result.removeSource(getCategories())
					if (!categories.body.isNullOrEmpty()){
						data.categories = categories.body
					}
					result.addSource(getProducts()){ products ->
						when (products.status) {
							StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
								result.removeSource(getProducts())
								if (!products.body.isNullOrEmpty()){
									data.products = products.body
								}
								result.addSource(getVariantOptions()){
									when (it.status) {
										StatusResponse.SUCCESS, StatusResponse.EMPTY -> {
											result.removeSource(getVariantOptions())
											if (!it.body.isNullOrEmpty()){
												data.variants = it.body
											}
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
						result.value = ApiResponse.success(data)
					else
						result.value = ApiResponse.empty(null)
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

	fun getSuppliers(callback: ((ApiResponse<List<Supplier>>) -> Unit)) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Supplier.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = Supplier.toListClass(it)
					if (data.isNullOrEmpty())
						callback.invoke(ApiResponse.empty(null))
					else
						callback.invoke(ApiResponse.success(data))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
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

	fun getPayments(callback: ((ApiResponse<List<Payment>>) -> Unit)) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Payment.DB_NAME)
				.get()
				.addOnSuccessListener {
					val data = Payment.toListClass(it)
					if (data.isNullOrEmpty())
						callback.invoke(ApiResponse.empty(null))
					else
						callback.invoke(ApiResponse.success(data))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
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
