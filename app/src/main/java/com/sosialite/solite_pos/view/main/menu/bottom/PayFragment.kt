package com.sosialite.solite_pos.view.main.menu.bottom

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sosialite.solite_pos.data.source.local.entity.helper.Order
import com.sosialite.solite_pos.databinding.FragmentPayBinding
import com.sosialite.solite_pos.utils.tools.BottomSheet
import com.sosialite.solite_pos.view.main.MainActivity
import java.text.ParseException


class PayFragment(private var order: Order?) : BottomSheetDialogFragment() {

	private lateinit var _binding: FragmentPayBinding
	private var mainActivity: MainActivity? = null

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

			setWatcher()

			_binding.btnPayPay.setOnClickListener { printBill(_binding.edtPayPay.text.toString()) }
			_binding.btnPayCancel.setOnClickListener { dialog?.dismiss() }
		}
	}

	private fun setWatcher(){
		_binding.edtPayPay.addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(s: Editable) {
				if (order != null){
					var input = 0
					if (s.isNotEmpty()){
						try {
							input = s.toString().toInt()
						}catch (e: ParseException){
							e.printStackTrace()
						}
					}
					if (input < order!!.totalPay){
						_binding.edtPayPay.error = "Jumlah kurang dari total belanja"
					}else{
						_binding.edtPayPay.error = null
					}
				}
			}
			override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
			override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
		})
	}

	private fun printBill(pay: String){
		if (order != null){
			order!!.pay = pay.toInt()
			mainActivity?.printBill(order!!)
		}
	}
}
