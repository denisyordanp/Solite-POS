package com.sosialite.solite_pos.data.source.remote

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sosialite.solite_pos.data.source.local.entity.room.master.Customer
import com.sosialite.solite_pos.data.source.local.entity.room.master.Outcome
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import com.sosialite.solite_pos.data.source.remote.response.helper.ApiResponse

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

	fun getCustomer(callback: ((ApiResponse<List<Customer>?>) -> Unit)) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Customer.DB_NAME)
				.get()
				.addOnSuccessListener {
					callback.invoke(ApiResponse.success(Customer.convertToListClass(it)))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
	}

	fun addCustomer(customer: Customer, callback: (ApiResponse<Boolean>) -> Unit) {
		db.collection(AppDatabase.DB_NAME)
				.document(AppDatabase.MAIN)
				.collection(Customer.DB_NAME)
				.document(customer.id.toString())
				.set(Customer.convertToHashMap(customer))
				.addOnSuccessListener {
					callback.invoke(ApiResponse.success(true))
				}
				.addOnFailureListener {
					callback.invoke(ApiResponse.error(null))
				}
	}

//	fun addOutcome(outcome: Outcome, callback: ((ApiResponse<Boolean?>) -> Unit)) {
//		db.collection(AppDatabase.DB_NAME)
//				.document(AppDatabase.MAIN)
//				.collection(Outcome.DB_NAME)
//				.document(outcome.id.toString())
//				.set(Outcome.convertToHashMap(customer))
//				.addOnSuccessListener {
//					callback.invoke(ApiResponse.success(true))
//				}
//				.addOnFailureListener {
//					callback.invoke(ApiResponse.error(null, null))
//				}
//	}

	fun batch(){
//		val customer = Customer(3, "Cristine")
//		val customer1 = Customer(4, "Ajay")
//		val ref1 = db.collection(AppDatabase.DB_NAME)
//				.document(AppDatabase.MAIN)
//				.collection(Customer.DB_NAME)
//				.document(customer.id.toString())
//		val ref2 = db.collection(AppDatabase.DB_NAME)
//				.document(AppDatabase.MAIN)
//				.collection(Customer.DB_NAME)
//				.document(customer1.id.toString())
//
//		db.runBatch {
//			it.set(ref1, Customer.convertToHashMap(customer))
//			it.set(ref2, Customer.convertToHashMap(customer1))
//		}.addOnCompleteListener {
//			Log.w("TESTINGDATA", "$it")
//		}
	}
}
