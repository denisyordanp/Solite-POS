package com.sosialite.solite_pos.view.main.menu.adapter.master.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.data.source.local.entity.room.helper.DataProduct
import com.sosialite.solite_pos.data.source.local.entity.room.helper.VariantWithOption
import com.sosialite.solite_pos.databinding.RvProductMasterBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.toRupiah
import com.sosialite.solite_pos.view.main.menu.master.bottom.ProductMasterFragment

class ProductMasterAdapter(private val fragmentManager: FragmentManager) : RecyclerView.Adapter<ProductMasterAdapter.ListViewHolder>() {

	var items: ArrayList<DataProduct> = ArrayList()
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
		holder.setVariants(p.options)

		holder.itemView.setOnClickListener {
			ProductMasterFragment(p).show(fragmentManager, "detail-product")
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	inner class ListViewHolder(var binding: RvProductMasterBinding) : RecyclerView.ViewHolder(binding.root){
		fun setVariants(variants: List<VariantWithOption>){
			val count = "${variants.size} Varian terpilih"
			binding.tvRvPmVariant.text = count
		}
	}
}
