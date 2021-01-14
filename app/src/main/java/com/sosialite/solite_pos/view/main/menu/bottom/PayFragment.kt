package com.sosialite.solite_pos.view.main.menu.bottom

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
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import com.sosialite.solite_pos.data.source.local.entity.room.master.Payment
import com.sosialite.solite_pos.databinding.FragmentPayBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.utils.tools.BottomSheet
import com.sosialite.solite_pos.utils.tools.MessageBottom
import com.sosialite.solite_pos.view.main.MainActivity
import com.sosialite.solite_pos.view.main.PaymentsActivity
import com.sosialite.solite_pos.view.viewmodel.MainViewModel


class PayFragment(private var order: Order?) : BottomSheetDialogFragment() {

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

			_binding.btnPayMethod.setOnClickListener { getPayment(mainActivity!!) }
			_binding.btnPayPay.setOnClickListener { payBill() }
			_binding.btnPayCancel.setOnClickListener { dialog?.dismiss() }
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == PaymentsActivity.RQ_PAYMENT && resultCode == Activity.RESULT_OK){
			payment = data?.getSerializableExtra(PaymentsActivity.PAYMENT) as Payment
			if (payment != null && payment!!.isCash){
				_binding.layEdtPayCash.visibility = View.VISIBLE
			}
		}
	}

	private fun payBill(){
		if (payment != null){
			if (payment!!.isCash){
				if (isCheck){
					MessageBottom(childFragmentManager)
						.setMessage("Pastikan sudah terima pembayaran sebelum proses. Proses pembayaran?")
						.setPositiveListener("Ya"){
							printBill(cashPay)
						}
						.setNegativeListener("Batal"){
							it?.dismiss()
						}.show()
				}
			}else{
				MessageBottom(childFragmentManager)
					.setMessage("Pastikan sudah terima pembayaran sebelum proses. Proses pembayaran?")
					.setPositiveListener("Ya"){
						printBill(null)
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
//			cashPay.toInt() < order!!.totalPay -> {
//				_binding.edtPayCash.error = "Jumlah kurang dari total belanja"
//				false
//			}
			else -> {
				_binding.edtPayCash.error = null
				true
			}
		}
	}

	private fun getPayment(context: Context){
		startActivityForResult(Intent(context, PaymentsActivity::class.java), PaymentsActivity.RQ_PAYMENT)
	}

	private fun printBill(pay: String?){
		if (order != null){
//			order!!.pay = pay?.toInt() ?: order!!.totalPay
			mainActivity?.printBill?.payment = payment
			mainActivity?.printBill?.doPrint(order!!)
		}
	}
}
