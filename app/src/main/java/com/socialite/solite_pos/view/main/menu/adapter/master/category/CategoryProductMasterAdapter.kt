package com.socialite.solite_pos.view.main.menu.adapter.master.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.databinding.RvCategoryMasterBinding

class CategoryProductMasterAdapter(
	private val callback: ((Category) -> Unit)
) : RecyclerView.Adapter<CategoryProductMasterAdapter.ListViewHolder>() {

	var items: ArrayList<Category> = ArrayList()
		set(value) {
			if (field.isNotEmpty()){
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvCategoryMasterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val c = items[position]
		holder.binding.tvRvCtName.text = c.name
		holder.binding.tvRvCtDesc.text = c.desc
		holder.binding.swCmStatus.visibility = View.GONE
		holder.itemView.setOnClickListener { callback.invoke(c) }
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(var binding: RvCategoryMasterBinding) : RecyclerView.ViewHolder(binding.root)
}
