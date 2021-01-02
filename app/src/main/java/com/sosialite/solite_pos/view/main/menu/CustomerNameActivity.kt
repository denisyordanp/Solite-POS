package com.sosialite.solite_pos.view.main.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosialite.solite_pos.data.source.local.entity.Customer
import com.sosialite.solite_pos.databinding.ActivityCustomerNameBinding
import com.sosialite.solite_pos.view.main.menu.adapter.CustomerAdapter

class CustomerNameActivity : AppCompatActivity() {

	private lateinit var _binding: ActivityCustomerNameBinding
	private lateinit var adapter: CustomerAdapter

	companion object{
		const val CUSTOMER: String = "customer"
		const val RQ_COSTUMER = 10
	}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
		_binding = ActivityCustomerNameBinding.inflate(layoutInflater)
        setContentView(_binding.root)

		adapter = CustomerAdapter { setResult(it) }
		_binding.rvCustomerName.layoutManager = LinearLayoutManager(this)
		_binding.rvCustomerName.adapter = adapter
		adapter.setItems(getCustomer())

		_binding.btnCnCancel.setOnClickListener {
			setResult(RESULT_CANCELED)
			finish()
		}
    }

	private fun setResult(customer: Customer){
		val data = Intent()
		data.putExtra(CUSTOMER, customer)
		setResult(RESULT_OK, data)
		finish()
	}

	private fun getCustomer(): ArrayList<Customer>{
		val arrayList: ArrayList<Customer> = ArrayList()
		arrayList.add(Customer(1, "Aci"))
		arrayList.add(Customer(2, "Denis"))
		arrayList.add(Customer(3, "Eva"))
		arrayList.add(Customer(4, "Linda"))
		arrayList.add(Customer(5, "Kolter"))
		arrayList.add(Customer(6, "Alfren"))
		arrayList.add(Customer(7, "Oca"))
		return arrayList
	}
}
