package com.socialite.solite_pos.view.main.menu.bottom

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import com.socialite.solite_pos.data.source.remote.response.helper.StatusResponse
import com.socialite.solite_pos.databinding.FragmentPayBinding
import com.socialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.socialite.solite_pos.utils.config.MainConfig.Companion.toRupiah
import com.socialite.solite_pos.utils.tools.BottomSheet
import com.socialite.solite_pos.utils.tools.MessageBottom
import com.socialite.solite_pos.view.main.MainActivity
import com.socialite.solite_pos.view.main.menu.order.SelectPaymentsActivity
import com.socialite.solite_pos.view.viewmodel.MainViewModel


class PayFragment(private var order: OrderWithProduct?) : BottomSheetDialogFragment() {

	private lateinit var _binding: FragmentPayBinding
	private lateinit var viewModel: MainViewModel

	private var mainActivity: MainActivity? = null
	private var payment: Payment? = null

	private val cashPay: String
	get() = _binding.edtPayCash.text.toString()

	constructor(): this(null)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (activity != null){
			mainActivity = activity as MainActivity
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View {
		_binding = FragmentPayBinding.inflate(inflater, container, false)
		return _binding.root
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
		return BottomSheet.setBottom(bottomSheetDialog)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (mainActivity != null){

			viewModel = getViewModel(mainActivity!!)

			val total = "Total : ${toRupiah(order?.grandTotal)}"
			_binding.tvPayTotal.text = total

			_binding.btnPayMethod.setOnClickListener { getPayment(mainActivity!!) }
			_binding.btnPayPay.setOnClickListener { payBill() }
			_binding.btnPayCancel.setOnClickListener { dialog?.dismiss() }
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == SelectPaymentsActivity.RQ_PAYMENT && resultCode == Activity.RESULT_OK){
			payment = data?.getSerializableExtra(SelectPaymentsActivity.PAYMENT) as Payment
			if (payment != null){
				_binding.btnPayMethod.text = payment!!.name
				_binding.btnPayPay.isEnabled = true

				if (payment!!.isCash)
					_binding.layEdtPayCash.visibility = View.VISIBLE
				else
					_binding.layEdtPayCash.visibility = View.GONE
			}
		}
	}

	private fun payBill(){
		if (payment != null && order != null){
			if (payment!!.isCash){
				if (isCheck){
					MessageBottom(childFragmentManager)
						.setMessage("Pastikan sudah terima pembayaran sebelum proses. Proses pembayaran?")
						.setPositiveListener("Ya"){
							pay(payment!!, cashPay.toLong())
						}
						.setNegativeListener("Batal"){
							it?.dismiss()
						}.show()
				}
			}else{
				MessageBottom(childFragmentManager)
					.setMessage("Pastikan sudah terima pembayaran sebelum proses. Proses pembayaran?")
					.setPositiveListener("Ya"){
						pay(payment!!, order!!.grandTotal)
					}
					.setNegativeListener("Batal"){
						it?.dismiss()
					}.show()
			}
		}
	}

	private val isCheck: Boolean
	get() {
		return when {
			cashPay.isEmpty() -> {
				_binding.edtPayCash.error = "Tidak boleh kosong"
				false
			}
			cashPay.toInt() < order!!.grandTotal -> {
				_binding.edtPayCash.error = "Jumlah kurang dari total belanja"
				false
			}
			else -> {
				_binding.edtPayCash.error = null
				true
			}
		}
	}

	private fun getPayment(context: Context){
		startActivityForResult(Intent(context, SelectPaymentsActivity::class.java), SelectPaymentsActivity.RQ_PAYMENT)
	}

	private fun pay(payment: Payment, pay: Long){
		if (this.order != null){
			val paymentOrder = OrderPayment(this.order!!.order.order.orderNo, payment.id, pay)
			order!!.order = OrderData(order!!.order.order, order!!.order.customer, paymentOrder, payment)
			viewModel.insertPaymentOrder(paymentOrder){
				when(it.status){
					StatusResponse.SUCCESS -> {
						printBill(order!!)
					}
					else -> {}
				}
			}
		}
	}

	private fun printBill(order: OrderWithProduct){
		mainActivity?.setPay(order)
		dialog?.dismiss()
	}
}
