package com.sosialite.solite_pos.view.main.menu.bottom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sosialite.solite_pos.data.source.local.entity.room.master.Outcome
import com.sosialite.solite_pos.databinding.FragmentDetailOutcomeBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.currentDate
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.dateFormat
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.ldFormat
import com.sosialite.solite_pos.utils.tools.BottomSheet
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class DetailOutcomeFragment(private val outcome: Outcome?) : BottomSheetDialogFragment() {

	constructor(): this(null)

	private lateinit var _binding: FragmentDetailOutcomeBinding
	private lateinit var viewModel: MainViewModel

	private val name: String
		get() = _binding.edtOcName.text.toString().trim()
	private val desc: String
		get() = _binding.edtOcDesc.text.toString().trim()
	private val amount: String
		get() = _binding.edtOcAmount.text.toString().trim()
	private val price: String
		get() = _binding.edtOcPrice.text.toString().trim()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View {
		_binding = FragmentDetailOutcomeBinding.inflate(inflater, container, false)
		return _binding.root
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
		return BottomSheet.setBottom(bottomSheetDialog)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){
			viewModel = getViewModel(activity!!)

			if (outcome != null){
				setData()
			}else{
				_binding.btnOcSave.setOnClickListener {
					if (isCheck){
						saveData(getOutcome(name, desc, amount.toInt(), price.toInt(), currentDate))
					}
				}
			}
		}
	}

	private fun setData(){
		if (outcome != null){
			_binding.tvOcDate.text = dateFormat(outcome.date, ldFormat)
			_binding.edtOcName.setText(outcome.name)
			_binding.edtOcDesc.setText(outcome.desc)
			_binding.edtOcAmount.setText(outcome.amount)
			_binding.edtOcPrice.setText(outcome.price)
			_binding.btnOcSave.setOnClickListener {
				if (isCheck){
					updateData(getOutcome(name, desc, amount.toInt(), price.toInt(), currentDate))
				}
			}
		}
	}

	private val isCheck: Boolean
		get() {
			return when {
				name.isEmpty() -> {
					_binding.edtOcName.error = "Tidak boleh kosong"
					false
				}
				desc.isEmpty() -> {
					_binding.edtOcDesc.error = "Tidak boleh kosong"
					false
				}
				amount.isEmpty() -> {
					_binding.edtOcAmount.error = "Tidak boleh kosong"
					false
				}
				amount.toInt() == 0 -> {
					_binding.edtOcAmount.error = "Tidak boleh 0"
					false
				}
				price.isEmpty() -> {
					_binding.edtOcPrice.error = "Tidak boleh kosong"
					false
				}
				price.toInt() == 0 -> {
					_binding.edtOcPrice.error = "Tidak boleh 0"
					false
				}
				else -> {
					true
				}
			}
		}

	private fun saveData(outcome: Outcome){
		viewModel.insertOutcome(outcome)
		dialog?.dismiss()
	}

	private fun updateData(outcome: Outcome){
		viewModel.updateOutcome(outcome)
		dialog?.dismiss()
	}

	private fun getOutcome(name: String, desc: String, amount: Int, price: Int, date: String): Outcome {
		return if (outcome != null){
			Outcome(outcome.id, name, desc, price, amount, date)
		}else{
			Outcome(name, desc, price, amount, date)
		}
	}
}
