package com.socialite.solite_pos.view.main.menu.master.bottom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.socialite.solite_pos.databinding.FragmentVariantOptionBinding
import com.socialite.solite_pos.utils.tools.BottomSheet
import com.socialite.solite_pos.view.viewModel.ProductViewModel

class VariantOptionFragment(
	private val idVariant: Long,
	private val variant: VariantOption?
	) : BottomSheetDialogFragment() {

	constructor(variant: VariantOption) : this(variant.idVariant, variant)
	constructor(idVariant: Long?) : this(idVariant ?: 0L, null)
	constructor() : this(0, null)

	private lateinit var _binding: FragmentVariantOptionBinding
	private lateinit var viewModel: ProductViewModel


	private val name: String
		get() = _binding.edtVoName.text.toString().trim()
	private val desc: String
		get() = _binding.edtVoDesc.text.toString().trim()
	private val isCount: Boolean
		get() = _binding.cbVoMany.isChecked

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentVariantOptionBinding.inflate(inflater, container, false)
        return _binding.root
    }

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
		return BottomSheet.setBottom(bottomSheetDialog)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null) {

			viewModel = ProductViewModel.getMainViewModel(activity!!)

			if (variant != null) {
				setData()
			} else {
				_binding.btnVoSave.setOnClickListener {
					if (isCheck) {
						saveData(getOption(name, desc, isCount, false))
					}
				}
			}

			_binding.btnVoCancel.setOnClickListener { dialog?.dismiss() }
		}
	}

	private fun setData(){
		if (variant != null){
			_binding.edtVoName.setText(variant.name)
			_binding.edtVoDesc.setText(variant.desc)

			_binding.btnVoSave.setOnClickListener {
				if (isCheck){
					updateData(getOption(name, desc, isCount, variant.isActive))
				}
			}
		}
	}

	private val isCheck: Boolean
		get() {
			return when {
				name.isEmpty() -> {
					_binding.edtVoName.error = "Tidak boleh kosong"
					false
				}
				desc.isEmpty() -> {
					_binding.edtVoDesc.error = "Tidak boleh kosong"
					false
				}
				else -> {
					true
				}
			}
		}

	private fun saveData(data: VariantOption){
		viewModel.insertVariantOption(data) {}
		dialog?.dismiss()
	}

	private fun updateData(data: VariantOption){
		viewModel.updateVariantOption(data) {}
		dialog?.dismiss()
	}

	private fun getOption(name: String, desc: String, isCount: Boolean, isActive: Boolean?): VariantOption {
		return if (variant != null){
			if (isActive != null){
				VariantOption(variant.id, variant.idVariant, name, desc, isCount, false)
			}else{
				VariantOption(variant.id, variant.idVariant, name, desc, isCount, false)
			}
		}else{
			VariantOption(idVariant, name, desc, isCount, false)
		}
	}
}
