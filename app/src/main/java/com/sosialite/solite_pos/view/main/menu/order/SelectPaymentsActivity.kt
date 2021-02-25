package com.sosialite.solite_pos.view.main.menu.order

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosialite.solite_pos.data.source.local.entity.room.master.Payment
import com.sosialite.solite_pos.data.source.remote.response.helper.StatusResponse
import com.sosialite.solite_pos.databinding.ActivityPaymentsBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.sosialite.solite_pos.view.main.menu.adapter.PaymentsAdapter
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class SelectPaymentsActivity : SocialiteActivity() {

	private lateinit var _binding: ActivityPaymentsBinding
	private lateinit var adapter: PaymentsAdapter
	private lateinit var viewModel: MainViewModel

	companion object{
		const val PAYMENT: String = "payment"
		const val RQ_PAYMENT = 11
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityPaymentsBinding.inflate(layoutInflater)
		setContentView(_binding.root)

		viewModel = getViewModel(this)

		adapter = PaymentsAdapter{ setResult(it) }
		_binding.rvCustomerName.layoutManager = LinearLayoutManager(this)
		_binding.rvCustomerName.adapter = adapter

		getPayments()

		_binding.btnPmsCancel.setOnClickListener {
			setResult(RESULT_CANCELED)
			finish()
		}
	}

	private fun getPayments(){
		viewModel.getPayments {
			when(it.status){
				StatusResponse.SUCCESS -> {
					if (!it.body.isNullOrEmpty()){
						adapter.items = ArrayList(it.body)
					}
				}
				else -> {}
			}
		}
	}

	private fun setResult(payment: Payment){
		val data = Intent()
		data.putExtra(PAYMENT, payment)
		setResult(RESULT_OK, data)
		finish()
	}
}
