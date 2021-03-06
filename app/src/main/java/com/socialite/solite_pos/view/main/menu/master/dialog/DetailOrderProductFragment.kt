package com.socialite.solite_pos.view.main.menu.master.dialog

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import com.socialite.solite_pos.R
import com.socialite.solite_pos.data.source.local.entity.helper.OptionWithRadioButton
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.helper.VariantView
import com.socialite.solite_pos.data.source.local.entity.helper.VariantWithOptions
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.socialite.solite_pos.databinding.FragmentDetailOrderProductBinding
import com.socialite.solite_pos.databinding.ItemAmountLayoutBinding
import com.socialite.solite_pos.utils.config.CustomDialogFragment
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import com.socialite.solite_pos.vo.Status

class DetailOrderProductFragment(
        private val type: Int,
        private val product: Product,
        private var callback: ((ProductOrderDetail) -> Unit)?,
) : CustomDialogFragment() {

    private lateinit var _binding: FragmentDetailOrderProductBinding
	private lateinit var _amount: ItemAmountLayoutBinding
	private lateinit var viewModel: ProductViewModel

    companion object {
        const val MIX: Int = 0
        const val ORDER: Int = 1
        const val PURCHASE: Int = 2
    }

    private var amount: Int = 0
    private var maxAmount: Long = 0
	private var radioColor: Int = 0
	private var textError: Int = 0

	private var arrayRg: ArrayList<VariantView> = ArrayList()
	private val values: String
	get() {
		return _binding.edtPlValue.text.toString().trim()
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View {
        _binding = FragmentDetailOrderProductBinding.inflate(inflater, container, false)
        _amount = _binding.contPlPlusMinus
        return _binding.root
    }

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null) {

			radioColor = ResourcesCompat.getColor(activity!!.resources, R.color.primary, null)
			textError = ResourcesCompat.getColor(activity!!.resources, R.color.red, null)

			viewModel = ProductViewModel.getMainViewModel(activity!!)

			setData()

			_binding.btnCmCancel.setOnClickListener { dialog?.dismiss() }
			_binding.btnCmSave.setOnClickListener {
				if (type == PURCHASE) {
					if (validate()) {
						setResult()
					}
				} else {
                    setResult()
                }
            }
            _amount.btnPlPlus.setOnClickListener { addAmount() }
            _amount.btnPlMin.setOnClickListener { minAmount() }
        }
	}

	private fun setData() {
        _binding.tvDopName.text = product.name

        when (type) {
            ORDER -> {
                setVariants()
                _binding.tvPlStock.visibility = View.VISIBLE

                maxAmount = product.getStockPortion()
                _binding.tvPlStock.text = "Sisa : $maxAmount Porsi"

            }
            PURCHASE -> {
                _amount.root.visibility = View.GONE
                _binding.contPlValue.visibility = View.VISIBLE
                _binding.tvPlStock.visibility = View.GONE
            }
            MIX -> {
                setVariants()
                _amount.root.visibility = View.VISIBLE
                _binding.contPlValue.visibility = View.GONE
                _binding.tvPlStock.visibility = View.VISIBLE

                maxAmount = product.stock
                _binding.tvPlStock.text = "Sisa : $maxAmount Pcs"
            }
		}
	}

	private fun validate(): Boolean{
		if (values.isEmpty()){
			_binding.edtPlValue.error = "Tidak boleh kosong"
		}else{
			return true
		}
		return false
	}

	private fun addAmount(){
		if (amount < maxAmount) {
            amount += 1
            _amount.tvPlAmount.text = amount.toString()
        }
	}

	private fun minAmount(){
		if (amount > 0) {
            amount -= 1
            _amount.tvPlAmount.text = amount.toString()
        }
	}

	private fun setVariants(){
		if (activity != null) {
            viewModel.getProductVariantOptions(product.id).observe(activity!!) {
                when (it.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        if (!it.data.isNullOrEmpty()) {
                            for (item in it.data) {
                                addVariantView(item)
                            }
                        }
                    }
                    Status.ERROR -> {
                    }
				}
			}
		}
	}

	private fun addVariantView(variants: VariantWithOptions){
		if (activity == null || variants.variant == null) return

		_binding.contVariant.addView(addTextView(variants.variant!!.name, activity!!))
		val rg = RadioGroup(activity)
		if (variants.options.size > 2)
			rg.orientation = RadioGroup.VERTICAL
		else
			rg.orientation = RadioGroup.HORIZONTAL

		val optRd: ArrayList<OptionWithRadioButton> = ArrayList()
		for (item in variants.options){
			val rb = RadioButton(activity)
			rb.text = item.name
			rb.id = View.generateViewId()
			rb.buttonTintList = ColorStateList.valueOf(radioColor)
			if (variants.options.size == 1){
				if (item.isActive){
					rb.isChecked = true
				}
			}
			rg.addView(rb)
			if (!item.isActive){
				rb.isEnabled = false
			}
			optRd.add(OptionWithRadioButton(item, rb))
		}
		val tvError = addTextView("Pilih salah satu", activity!!)
		tvError.visibility = View.GONE
		tvError.setTextColor(textError)

		arrayRg.add(VariantView(variants.variant!!, tvError, rg, optRd))
		_binding.contVariant.addView(rg)
		_binding.contVariant.addView(tvError)
	}

	private fun addTextView(text: String, activity: FragmentActivity): TextView{
		val tvTitle = TextView(activity)
		tvTitle.layoutParams = ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT
		)
		tvTitle.text = text
		return tvTitle
	}

	private fun setResult(){
		var isOkay = type == PURCHASE
		val arrayVariants: ArrayList<VariantOption> = ArrayList()
		if (arrayRg.isNotEmpty()){
			for (item in arrayRg){
				if (item.radioGroup.checkedRadioButtonId == -1){
					isOkay = false
					item.textError.visibility = View.VISIBLE
				}else{
					isOkay = true
					for (item2 in item.options){
						if (item2.radioButton.isChecked){
							arrayVariants.add(item2.variantOption)
						}
					}
				}
			}
		}else{
			isOkay = true
		}

		if (isOkay){
			val detail = if (type == PURCHASE) {
                ProductOrderDetail.createProduct(product, arrayVariants, values.toInt())
            }else {
                ProductOrderDetail.createProduct(product, arrayVariants, amount)
            }
			callback?.invoke(detail)
			dialog?.dismiss()
		}
	}
}
