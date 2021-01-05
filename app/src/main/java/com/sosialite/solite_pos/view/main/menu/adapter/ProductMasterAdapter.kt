package com.sosialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.data.source.local.entity.helper.ProductWithCategory
import com.sosialite.solite_pos.databinding.RvProductMasterBinding

class ProductMasterAdapter : RecyclerView.Adapter<ProductMasterAdapter.ListViewHolder>() {

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
		holder.binding.tvRvPmName.text = p.product?.name
		holder.binding.tvRvPmPrice.text = p.product?.price.toString()
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(var binding: RvProductMasterBinding) : RecyclerView.ViewHolder(binding.root)
}
