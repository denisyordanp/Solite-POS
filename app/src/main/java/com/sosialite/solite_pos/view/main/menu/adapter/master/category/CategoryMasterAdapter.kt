package com.sosialite.solite_pos.view.main.menu.adapter.master.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.data.source.local.entity.room.master.Category
import com.sosialite.solite_pos.databinding.RvCategoryMasterBinding
import com.sosialite.solite_pos.view.main.menu.master.bottom.CategoryMasterFragment
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class CategoryMasterAdapter(
	private val viewModel: MainViewModel,
	private val fragmentManager: FragmentManager
	) : RecyclerView.Adapter<CategoryMasterAdapter.ListViewHolder>() {

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
		holder.binding.swCmStatus.isChecked = c.isActive

		holder.itemView.setOnClickListener {
			CategoryMasterFragment(c).show(fragmentManager, "detail-category")
		}
		holder.binding.swCmStatus.setOnCheckedChangeListener{ v, _ ->
			run {
				c.isActive = v.isChecked
				viewModel.updateCategory(c) {}
			}
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(var binding: RvCategoryMasterBinding) : RecyclerView.ViewHolder(binding.root)
}
