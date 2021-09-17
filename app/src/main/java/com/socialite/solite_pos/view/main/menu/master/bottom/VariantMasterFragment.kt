package com.socialite.solite_pos.view.main.menu.master.bottom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.databinding.FragmentVariantMasterBinding
import com.socialite.solite_pos.utils.tools.BottomSheetView
import com.socialite.solite_pos.view.viewModel.ProductViewModel

class VariantMasterFragment(private val variant: Variant?) : BottomSheetDialogFragment() {

	constructor(): this(null)

	private lateinit var _binding: FragmentVariantMasterBinding
	private lateinit var viewModel: ProductViewModel

	private val name: String
		get() = _binding.edtVmName.text.toString().trim()
	private val isMix: Boolean
		get() = _binding.cbVmMix.isChecked
	private val isMust: Boolean
		get() = _binding.cbVmMust.isChecked
	private val optionTYpe: Int
	get() {
		return when{
			_binding.rbVmOne.isChecked -> Variant.ONE_OPTION
			_binding.rbVmMultiple.isChecked -> Variant.MULTIPLE_OPTION
			else -> Variant.ONE_OPTION
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View {
		_binding = FragmentVariantMasterBinding.inflate(inflater, container, false)
		return _binding.root
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
		return BottomSheetView.setBottom(bottomSheetDialog)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){

			viewModel = ProductViewModel.getMainViewModel(activity!!)

			if (variant != null){
				setData()
			}else{
				_binding.btnVmSave.setOnClickListener {
					if (isCheck){
						saveData(getVariant(name, optionTYpe, isMust, isMix))
					}
				}
			}
			_binding.btnVmCancel.setOnClickListener { dialog?.dismiss() }
		}
	}

	private val isCheck: Boolean
		get() {
			return when {
				name.isEmpty() -> {
					_binding.edtVmName.error = "Tidak boleh kosong"
					false
				}
				else -> {
					true
				}
			}
		}

	private fun setData(){
		if (variant != null){
			_binding.edtVmName.setText(variant.name)
			_binding.cbVmMix.isChecked = variant.isMix
//			_binding.cbVmMust.isChecked = variant.isMust
			when (variant.type) {
				Variant.ONE_OPTION -> _binding.rbVmOne.isChecked = true
				Variant.MULTIPLE_OPTION -> _binding.rbVmMultiple.isChecked = true
			}

			_binding.btnVmSave.setOnClickListener {
				if (isCheck){
					updateData(getVariant(name, optionTYpe, isMust, variant.isMix))
				}
			}
		}
	}

	private fun saveData(variant: Variant){
		viewModel.insertVariants(variant) {}
		dialog?.dismiss()
	}

	private fun updateData(variant: Variant){
		viewModel.updateVariant(variant) {}
		dialog?.dismiss()
	}

	private fun getVariant(name: String, type: Int, isMust: Boolean, isMix: Boolean): Variant {
		return if (variant != null){
			Variant(variant.id, name, type, isMust, isMix)
		}else{
			Variant(name, type, isMust, isMix)
		}
	}
}
