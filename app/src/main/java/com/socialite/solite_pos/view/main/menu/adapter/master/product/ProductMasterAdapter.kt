package com.socialite.solite_pos.view.main.menu.adapter.master.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.databinding.RvProductMasterBinding
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah
import com.socialite.solite_pos.view.main.menu.master.bottom.ProductMasterFragment
import com.socialite.solite_pos.view.viewmodel.MainViewModel
import com.socialite.solite_pos.vo.Status

class ProductMasterAdapter(
		private val activity: FragmentActivity,
		private val viewModel: MainViewModel
		) : RecyclerView.Adapter<ProductMasterAdapter.ListViewHolder>() {

	var items: ArrayList<ProductWithCategory> = ArrayList()
		set(value) {
			if (field.isNotEmpty()){
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvProductMasterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val p = items[position]

		holder.binding.tvRvPmName.text = p.product.name
		holder.binding.tvRvPmBuyPrice.text = toRupiah(p.product.buyPrice)
		holder.binding.tvRvPmSellPrice.text = toRupiah(p.product.sellPrice)
		holder.binding.tvRvPmDesc.text = p.product.desc
		holder.setVariants(p.product.id)

		holder.itemView.setOnClickListener {
			ProductMasterFragment(p).show(activity.supportFragmentManager, "detail-product")
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	inner class ListViewHolder(var binding: RvProductMasterBinding) : RecyclerView.ViewHolder(binding.root){
		fun setVariants(idProduct: Long){
			viewModel.getProductVariantOptions(idProduct).observe(activity){
				var selected = ""
				when(it.status){
					Status.LOADING -> {
						selected = "... Varian terpilih"
					}
					Status.SUCCESS -> {
						if (it.data != null){
							var count = 0
							for (item in it.data){
								count += item.options.size
							}
							selected = "$count Varian terpilih"
						}
					}
					Status.ERROR -> { }
				}
				binding.tvRvPmVariant.text = selected
			}
		}
	}
}
