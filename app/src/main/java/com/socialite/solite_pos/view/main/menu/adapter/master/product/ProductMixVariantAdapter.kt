package com.socialite.solite_pos.view.main.menu.adapter.master.product

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.R
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.databinding.RvProductBinding
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.vo.Status

class ProductMixVariantAdapter(
		private val variant: Variant?,
	private val viewModel: MainViewModel,
		private val activity: FragmentActivity
	) : RecyclerView.Adapter<ProductMixVariantAdapter.ListViewHolder>() {

	var items: ArrayList<ProductWithCategory> = ArrayList()
		set(value) {
			if (field.isNotEmpty()){
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val p = items[position]

		holder.setContent(position, p.product)
		holder.binding.tvPmvName.text = p.product?.name
	}

	override fun getItemCount(): Int {
		return items.size
	}

	inner class ListViewHolder(val binding: RvProductBinding) : RecyclerView.ViewHolder(binding.root){

		private var isChecked: Boolean = false

		fun setContent(position: Int, product: Product?){
			if (product != null){
				binding.contPmvCount.visibility = View.GONE
				binding.cbPmvSelected.visibility = View.VISIBLE
				binding.cbPmvSelected.isEnabled = false
				if (variant != null){
					viewModel.getVariantMixProductById(variant.id, product.id).observe(activity){
						when(it.status){
							Status.LOADING -> {}
							Status.SUCCESS -> {
								if (it.data != null){
									isSelected(true)
									binding.root.setOnClickListener { _ ->
										delMix(it.data)
									}
								}else{
									isSelected(false)
									binding.root.setOnClickListener {
										addMix(product)
									}
								}
							}
							Status.ERROR -> {}
						}
					}
				}
			}
		}

		private fun addMix(product: Product){
			viewModel.insertVariantMix(VariantMix(variant!!.id, product.id)) {}
		}

		private fun delMix(mix: VariantMix){
			viewModel.removeVariantMix(mix) {}
		}

		private fun isSelected(state: Boolean){
			if (state){
				isChecked = true
				binding.cbPmvSelected.isChecked = true
				binding.contPmv.setCardBackgroundColor(ColorStateList.valueOf(ResourcesCompat.getColor(binding.root.resources, R.color.primary, null)))
			}else{
				isChecked = false
				binding.cbPmvSelected.isChecked = false
				binding.contPmv.setCardBackgroundColor(ColorStateList.valueOf(ResourcesCompat.getColor(binding.root.resources, R.color.white, null)))
			}
		}
	}
}
