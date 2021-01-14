package com.sosialite.solite_pos.view.main.menu.master.bottom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sosialite.solite_pos.data.source.local.entity.room.master.Category
import com.sosialite.solite_pos.databinding.FragmentCategoryMasterBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.utils.tools.BottomSheet
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class CategoryMasterFragment(private val category: Category?) : BottomSheetDialogFragment() {

	constructor(): this(null)

	private lateinit var _binding: FragmentCategoryMasterBinding
	private lateinit var viewModel: MainViewModel

	private val name: String
	get() = _binding.edtCmName.text.toString().trim()

	private val desc: String
	get() = _binding.edtCmDesc.text.toString().trim()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View {
		_binding = FragmentCategoryMasterBinding.inflate(inflater, container, false)
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

			if (category != null){
				setData()
			}else{
				_binding.btnCmSave.setOnClickListener {
					if (isCheck){
						saveData(getCategory(name, desc, false))
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
					updateData(getCategory(name, desc, category.isActive))
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
		viewModel.insertCategory(category)
		dialog?.dismiss()
	}

	private fun updateData(category: Category){
		viewModel.updateCategory(category)
		dialog?.dismiss()
	}

	private fun getCategory(name: String, desc: String, status: Boolean?): Category {
		return if (category != null){
			if (status != null){
				Category(category.id, name, desc, status)
			}else{
				Category(category.id, name, desc,false)
			}
		}else{
			Category(name, desc, false)
		}
	}
}
