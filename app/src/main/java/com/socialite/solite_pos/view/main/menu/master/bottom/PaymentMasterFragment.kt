package com.socialite.solite_pos.view.main.menu.master.bottom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import com.socialite.solite_pos.databinding.FragmentPaymentMasterBinding
import com.socialite.solite_pos.utils.config.MainConfig
import com.socialite.solite_pos.utils.tools.BottomSheet
import com.socialite.solite_pos.view.viewmodel.MainViewModel

class PaymentMasterFragment(private val payment: Payment?) : BottomSheetDialogFragment() {

	constructor(): this(null)

	private lateinit var _binding: FragmentPaymentMasterBinding
	private lateinit var viewModel: MainViewModel

	private val name: String
		get() = _binding.edtPmName.text.toString().trim()
	private val desc: String
		get() = _binding.edtPmDesc.text.toString().trim()
	private val tax: String
		get() = _binding.edtPmTax.text.toString().trim()
	private val isCash: Boolean
	get() = _binding.cbPmCash.isChecked

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View {
		_binding = FragmentPaymentMasterBinding.inflate(inflater, container, false)
		return _binding.root
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
		return BottomSheet.setBottom(bottomSheetDialog)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){

			viewModel = MainConfig.getViewModel(activity!!)

			if (payment != null){
				setData()
			}else{
				_binding.btnPmSave.setOnClickListener {
					if (isCheck){
						saveData(getPayment(name, desc, tax.toFloat(), isCash, false))
					}
				}
			}
			_binding.btnPmCancel.setOnClickListener { dialog?.dismiss() }
		}
	}

	private val isCheck: Boolean
		get() {
			return when {
				name.isEmpty() -> {
					_binding.edtPmName.error = "Tidak boleh kosong"
					false
				}
				desc.isEmpty() -> {
					_binding.edtPmDesc.error = "Tidak boleh kosong"
					false
				}
				tax.isEmpty() -> {
					_binding.edtPmTax.error = "Tidak boleh kosong"
					false
				}
				else -> {
					true
				}
			}
		}

	private fun setData(){
		if (payment != null){
			_binding.edtPmName.setText(payment.name)
			_binding.edtPmDesc.setText(payment.desc)
			_binding.edtPmTax.setText(payment.tax.toString())
			_binding.cbPmCash.isChecked = payment.isCash
			_binding.btnPmSave.setOnClickListener {
				if (isCheck){
					updateData(getPayment(name, desc, tax.toFloat(), isCash, payment.isActive))
				}
			}
		}
	}

	private fun saveData(data: Payment){
		viewModel.insertPayment(data) {}
		dialog?.dismiss()
	}

	private fun updateData(data: Payment){
		viewModel.updatePayment(data) {}
		dialog?.dismiss()
	}

	private fun getPayment(name: String, desc: String, tax: Float, isCash: Boolean, isActive: Boolean?): Payment {
		return if (payment != null){
			if (isActive != null){
				Payment(payment.id, name, desc, tax, isCash, isActive)
			}else{
				Payment(payment.id, name, desc, tax, isCash, false)
			}
		}else{
			Payment(name, desc, tax, isCash, false)
		}
	}
}
