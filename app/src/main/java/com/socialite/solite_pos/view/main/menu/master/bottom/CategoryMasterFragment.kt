package com.socialite.solite_pos.view.main.menu.master.bottom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.databinding.FragmentCategoryMasterBinding
import com.socialite.solite_pos.utils.tools.BottomSheet
import com.socialite.solite_pos.view.viewModel.ProductViewModel

class CategoryMasterFragment(private val category: Category?) : BottomSheetDialogFragment() {

	constructor() : this(null)

	private lateinit var _binding: FragmentCategoryMasterBinding
	private lateinit var viewModel: ProductViewModel

	private val name: String
		get() = _binding.edtCmName.text.toString().trim()
	private val desc: String
		get() = _binding.edtCmDesc.text.toString().trim()
	private val isStock: Boolean
		get() = _binding.cbCmStock.isChecked

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentCategoryMasterBinding.inflate(inflater, container, false)
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

			if (category != null) {
				setData()
			} else {
				_binding.btnCmSave.setOnClickListener {
					if (isCheck) {
						saveData(getCategory(true))
					}
				}
			}

			_binding.btnCmCancel.setOnClickListener { dialog?.dismiss() }
		}
	}

	private fun setData(){
		if (category != null){
			_binding.edtCmName.setText(category.name)
			_binding.edtCmDesc.setText(category.desc)
			_binding.btnCmSave.setOnClickListener {
				if (isCheck){
					updateData(getCategory(false))
				}
			}
		}
	}

	private val isCheck: Boolean
	get() {
		return when {
			name.isEmpty() -> {
				_binding.edtCmName.error = "Tidak boleh kosong"
				false
			}
			desc.isEmpty() -> {
				_binding.edtCmDesc.error = "Tidak boleh kosong"
				false
			}
			else -> {
				true
			}
		}
	}

	private fun saveData(category: Category){
		viewModel.insertCategory(category) {}
		dialog?.dismiss()
	}

	private fun updateData(category: Category){
		viewModel.updateCategory(category) {}
		dialog?.dismiss()
	}

	private fun getCategory(isNew: Boolean): Category {
		return if (category != null){
			if (!isNew){
				Category(category.id, name, desc, isStock, category.isActive)
			}else{
				Category(category.id, name, desc, isStock, false)
			}
		}else{
			Category(name, desc, isStock, false)
		}
	}
}
