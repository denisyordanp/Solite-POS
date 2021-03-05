package com.socialite.solite_pos.view.main.menu.order

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.data.source.remote.response.helper.StatusResponse
import com.socialite.solite_pos.databinding.ActivityCustomerNameBinding
import com.socialite.solite_pos.view.viewmodel.MainViewModel.Companion.getViewModel
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.view.main.menu.adapter.CustomerAdapter
import com.socialite.solite_pos.view.viewmodel.MainViewModel
import com.socialite.solite_pos.vo.Status
import java.util.*
import kotlin.collections.ArrayList

class SelectCustomerActivity : SocialiteActivity() {

	private lateinit var _binding: ActivityCustomerNameBinding
	private lateinit var adapter: CustomerAdapter
	private lateinit var viewModel: MainViewModel

	private val customers: ArrayList<Customer> = ArrayList()

	companion object{
		const val CUSTOMER: String = "customer"
		const val RC_COSTUMER = 10

		private val TAG = SelectCustomerActivity::class.qualifiedName
	}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
		_binding = ActivityCustomerNameBinding.inflate(layoutInflater)
        setContentView(_binding.root)

		viewModel = getViewModel(this)
		
		adapter = CustomerAdapter { setResult(it) }
		_binding.rvCustomerName.layoutManager = LinearLayoutManager(this)
		_binding.rvCustomerName.adapter = adapter

		getCustomer()

		_binding.btnCnCancel.setOnClickListener {
			setResult(RESULT_CANCELED)
			finish()
		}

		_binding.edtCnName.addTextChangedListener(object : TextWatcher{
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
			override fun afterTextChanged(s: Editable?) {}

			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
				setFilter(s.toString())
			}
		})
    }

	private fun setFilter(s: String?){
		if (!s.isNullOrEmpty()){
			val array: ArrayList<Customer> = ArrayList()
			for (user in customers){
				if (user.name.toLowerCase(Locale.getDefault()).contains(s.toLowerCase(Locale.getDefault()))){
					array.add(user)
				}
			}
			if (array.isEmpty()){
				val c = Customer("Tambah pelanggan baru")
				c.id = Customer.ID_ADD
				array.add(c)
			}
			adapter.setItems(array)
		}else{
			adapter.setItems(customers)
		}
	}

	private fun setResult(customer: Customer){
		val data = Intent()
		if (customer.id == Customer.ID_ADD){
			val newCustomer = Customer(nameCustomer)
			getIdCustomer(newCustomer){
				newCustomer.id
				data.putExtra(CUSTOMER, newCustomer)
				setResult(RESULT_OK, data)
				finish()
			}
		}else{
			data.putExtra(CUSTOMER, customer)
			setResult(RESULT_OK, data)
			finish()
		}
	}

	private val nameCustomer: String
		get() {
			val str = _binding.edtCnName.text.toString().trim { it <= ' ' }.toLowerCase(Locale.getDefault())
			val strArray = str.split(" ").toTypedArray()
			val builder = StringBuilder()
			for (s in strArray) {
				val cap = s.substring(0, 1).toUpperCase(Locale.getDefault()) + s.substring(1)
				builder.append(cap).append(" ")
			}
			return builder.toString()
		}

	private fun getCustomer(){
		viewModel.customers.observe(this) {
			when(it.status){
				Status.LOADING -> {}
				Status.SUCCESS -> {
					if (!it.data.isNullOrEmpty()){
						if (customers.isNotEmpty()){
							customers.clear()
						}
						customers.addAll(it.data)
					}
					setFilter(null)
				}
				Status.ERROR -> {}
			}
		}
	}

	private fun getIdCustomer(customer: Customer, callback: (Long) -> Unit){
		viewModel.insertCustomers(customer){
			if (it.status == StatusResponse.FINISH) callback.invoke(it.body!!)
			when(it.status){
				StatusResponse.SUCCESS ->
					Log.w(TAG, "Success upload data")
				StatusResponse.ERROR -> {
					Log.w(TAG, "Error upload data")
				}
				else -> {}
			}
		}
	}
}
