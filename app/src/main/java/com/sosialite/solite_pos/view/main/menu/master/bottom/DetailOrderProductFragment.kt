package com.sosialite.solite_pos.view.main.menu.master.bottom

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.data.source.local.entity.helper.OptionWithRadioButton
import com.sosialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.sosialite.solite_pos.data.source.local.entity.room.helper.DataProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.Variant
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.sosialite.solite_pos.databinding.FragmentDetailOrderProductBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.setDialogFragment

class DetailOrderProductFragment(
		private val product: DataProduct,
		private var callback: ((ProductOrderDetail) -> Unit)?
		) : DialogFragment() {

	private lateinit var _binding: FragmentDetailOrderProductBinding

	private var amount: Int = 0
	private var radioColor: Int = 0
	private var arrayRg: ArrayList<OptionWithRadioButton> = ArrayList()
	private val variants: ArrayList<Variant>
	get() {
		var idVariant = 0
		val array: ArrayList<Variant> = ArrayList()
		for (item in product.options){
			if (item.variants.id != idVariant){
				idVariant = item.variants.id
				array.add(item.variants)
			}
		}
		return array
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View {
		_binding = FragmentDetailOrderProductBinding.inflate(inflater, container, false)
		setDialogFragment(dialog?.window)
		return _binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){

			radioColor = ResourcesCompat.getColor(activity!!.resources, R.color.primary, null)
			setData()

			_binding.btnCmCancel.setOnClickListener { dialog?.dismiss() }
			_binding.btnCmSave.setOnClickListener { setResult() }
			_binding.btnPlPlus.setOnClickListener { addAmount() }
			_binding.btnPlMin.setOnClickListener { minAmount() }
		}
	}

	private fun setData(){
		_binding.tvDopName.text = product.product.name

		setVariants()
	}

	private fun addAmount(){
		amount += 1
		_binding.tvPlAmount.text = amount.toString()
	}

	private fun minAmount(){
		amount -= 1
		_binding.tvPlAmount.text = amount.toString()
	}

	private fun setVariants(){
		if (activity != null){
			for (item in variants){
				addTextView(item.name)
				addRadioGroup(item.id)
			}
		}
	}

	private fun addRadioGroup(id: Int){
		if (activity != null){
			val rg = RadioGroup(activity)
			rg.orientation = RadioGroup.HORIZONTAL
			for (item in sortOptions(id, false)){
				val rb = RadioButton(activity)
				rb.text = item.name
				rb.id = View.generateViewId()
				rb.buttonTintList = ColorStateList.valueOf(radioColor)
				if (sortOptions(id, true).size == 1){
					if (item.isActive){
						rb.isChecked = true
					}
				}
				rg.addView(rb)
				if (!item.isActive){
					rb.isEnabled = false
				}
				arrayRg.add(OptionWithRadioButton(item, rb))
			}
			_binding.contVariant.addView(rg)
		}
	}

	private fun sortOptions(id: Int, isActive: Boolean): ArrayList<VariantOption>{
		val array: ArrayList<VariantOption> = ArrayList()
		for (item in product.options){
			if (item.options.idVariant == id){
				if (isActive){
					if (item.options.isActive){
						array.add(item.options)
					}
				}else{
					array.add(item.options)
				}
			}
		}
		return array
	}

	private fun addTextView(text: String){
		if (activity != null){
			val tvTitle = TextView(activity)
			tvTitle.layoutParams = ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT
			)
			tvTitle.text = text
			_binding.contVariant.addView(tvTitle)
		}
	}

	private fun setResult(){
		val arrayVariants: ArrayList<VariantOption> = ArrayList()
		for (i in arrayRg){
			if (i.radioButton.isChecked){
				arrayVariants.add(i.variantOption)
			}
		}

		val detail = ProductOrderDetail(product.product, arrayVariants, amount)
		callback?.invoke(detail)
		dialog?.dismiss()
	}
}
