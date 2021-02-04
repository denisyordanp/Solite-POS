package com.sosialite.solite_pos.view.main.menu.master.bottom

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.data.source.local.entity.room.helper.DataProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.Category
import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import com.sosialite.solite_pos.databinding.FragmentProductMasterBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.utils.tools.BottomSheet
import com.sosialite.solite_pos.view.main.menu.master.ListMasterActivity
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class ProductMasterFragment(private val product: DataProduct?) : BottomSheetDialogFragment() {

	constructor(): this(null)

	private lateinit var _binding: FragmentProductMasterBinding
	private lateinit var viewModel: MainViewModel

	private var red: Int? = null
	private var white: Int? = null

	private val name: String
		get() = _binding.edtPmName.text.toString().trim()
	private val desc: String
		get() = _binding.edtPmDesc.text.toString().trim()
	private val portion: String
		get() = _binding.edtPmPortion.text.toString().trim()
	private val buyPrice: String
		get() = _binding.edtPmBuyPrice.text.toString().trim()
	private val sellPrice: String
		get() = _binding.edtPmSellPrice.text.toString().trim()

	private var category: Category? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductMasterBinding.inflate(inflater, container, false)
        return _binding.root
    }

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
		return BottomSheet.setBottom(bottomSheetDialog)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){

			red = ResourcesCompat.getColor(activity!!.resources, android.R.color.holo_red_light, null)
			white = ResourcesCompat.getColor(activity!!.resources, R.color.white, null)

			viewModel = getViewModel(activity!!)
			if (product != null){
				setData()
			}else{
				_binding.btnPmSave.setOnClickListener {
					if (isCheck && category != null){
						saveData(getProduct())
					}
				}
			}

			setButton()
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode == Activity.RESULT_OK){
			val category = data?.getSerializableExtra(ListMasterActivity.REQUEST_CODE) as Category?
			if (data != null){
				this.category = category
				_binding.btnPmCategory.text = category?.name
				_binding.btnPmCategory.setBackgroundColor(white!!)
			}
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
			portion.isEmpty() -> {
				_binding.edtPmPortion.error = "Tidak boleh kosong"
				false
			}
			portion.toInt() == 0 -> {
				_binding.edtPmPortion.error = "Tidak boleh 0"
				false
			}
			buyPrice.isEmpty() -> {
				_binding.edtPmBuyPrice.error = "Tidak boleh kosong"
				false
			}
			buyPrice.toInt() == 0 -> {
				_binding.edtPmBuyPrice.error = "Tidak boleh 0"
				false
			}
			sellPrice.isEmpty() -> {
				_binding.edtPmSellPrice.error = "Tidak boleh kosong"
				false
			}
			sellPrice.toInt() == 0 -> {
				_binding.edtPmSellPrice.error = "Tidak boleh 0"
				false
			}
			category == null -> {
				_binding.btnPmCategory.setBackgroundColor(red!!)
				false
			}
			else -> {
				true
			}
		}
	}

	private fun setData(){
		if (product?.category != null){
			category = product.category
			_binding.edtPmName.setText(product.product.name)
			_binding.edtPmDesc.setText(product.product.desc)
			_binding.edtPmBuyPrice.setText(product.product.buyPrice.toString())
			_binding.edtPmSellPrice.setText(product.product.sellPrice.toString())

			_binding.btnPmCategory.text = product.category.name
			val count = "${product.options.size} Varian terpilih"
			_binding.btnPmVariant.text = count

			_binding.btnPmSave.setOnClickListener {
				if (isCheck && category != null){
					updateData(getProduct())
				}
			}
		}
	}

	private fun setButton(){
		_binding.btnPmCancel.setOnClickListener { dialog?.dismiss() }

		_binding.btnPmCategory.setOnClickListener {
			startActivityForResult(
				Intent(activity!!, ListMasterActivity::class.java)
					.putExtra(ListMasterActivity.TYPE, ListMasterActivity.CATEGORY)
					.putExtra(ListMasterActivity.REQUEST_CODE, ListMasterActivity.RQ_CODE)
				, ListMasterActivity.RQ_CODE)
		}
		if (product != null){
			_binding.btnPmVariant.setOnClickListener {
				startActivityForResult(
					Intent(activity!!, ListMasterActivity::class.java)
						.putExtra(ListMasterActivity.TYPE, ListMasterActivity.VARIANT)
						.putExtra(ListMasterActivity.PRODUCT, product.product)
						.putExtra(ListMasterActivity.REQUEST_CODE, ListMasterActivity.RQ_CODE)
					, ListMasterActivity.RQ_CODE)
				dialog?.dismiss()
			}
		}else{
			_binding.btnPmVariant.visibility = View.GONE
		}
	}

	private fun updateData(data: Product){
		viewModel.updateProduct(data)
		dialog?.dismiss()
	}

	private fun saveData(data: Product){
		viewModel.insertProduct(data)
		dialog?.dismiss()
	}

	private fun getProduct(): Product{
		return if (product?.product != null){
			Product(product.product.id, name, category!!.id, desc, buyPrice.toInt(), sellPrice.toInt(), portion.toInt(), product.product.stock, product.product.isActive)
		}else{
			Product(name, category!!.id, desc, buyPrice.toInt(), sellPrice.toInt(), portion.toInt(), 0, false)
		}
	}
}
