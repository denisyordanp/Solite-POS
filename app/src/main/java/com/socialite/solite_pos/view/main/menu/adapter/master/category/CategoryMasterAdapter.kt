package com.socialite.solite_pos.view.main.menu.adapter.master.category

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.databinding.RvCategoryMasterBinding
import com.socialite.solite_pos.view.main.menu.master.bottom.CategoryMasterFragment
import com.socialite.solite_pos.view.viewModel.ProductViewModel

class CategoryMasterAdapter(private val activity: FragmentActivity) :
	RecyclerView.Adapter<CategoryMasterAdapter.ListViewHolder>() {

	private var viewModel: ProductViewModel = ProductViewModel.getMainViewModel(activity)

	var items: ArrayList<Category> = ArrayList()
		@SuppressLint("NotifyDataSetChanged")
		set(value) {
			if (field.isNotEmpty()) {
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
			CategoryMasterFragment(c).show(activity.supportFragmentManager, "detail-category")
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
