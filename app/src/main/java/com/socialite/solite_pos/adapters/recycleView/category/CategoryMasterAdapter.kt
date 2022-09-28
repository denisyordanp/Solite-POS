package com.socialite.solite_pos.adapters.recycleView.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.databinding.RvCategoryMasterBinding
import com.socialite.solite_pos.utils.tools.RecycleViewDiffUtils
import com.socialite.solite_pos.view.main.menu.master.bottom.CategoryMasterFragment
import com.socialite.solite_pos.view.viewModel.ProductViewModel

class CategoryMasterAdapter(private val activity: FragmentActivity) :
	RecyclerView.Adapter<CategoryMasterAdapter.ListViewHolder>() {

	private var viewModel: ProductViewModel = ProductViewModel.getMainViewModel(activity)

	private var categories: ArrayList<Category> = ArrayList()

	fun setCategories(categories: ArrayList<Category>) {
		val categoriesDiffUtil = RecycleViewDiffUtils(this.categories, categories)
		val diffUtilResult = DiffUtil.calculateDiff(categoriesDiffUtil)
		this.categories = categories
		diffUtilResult.dispatchUpdatesTo(this)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = RvCategoryMasterBinding.inflate(
			inflater,
			parent,
			false
		)
		return ListViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		holder.setDataToView(categories[position])
	}

	override fun getItemCount(): Int {
		return categories.size
	}

	inner class ListViewHolder(var binding: RvCategoryMasterBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun setDataToView(category: Category) {
			setDataTextView(category)
			setClickListener(category)
			setOnCheckedListener(category)
		}

		private fun setDataTextView(category: Category) {
			binding.tvRvCtName.text = category.name
			binding.tvRvCtDesc.text = category.desc
			binding.swCmStatus.isChecked = category.isActive
		}

		private fun setClickListener(category: Category) {
			binding.root.setOnClickListener {
				CategoryMasterFragment(category).show(
					activity.supportFragmentManager,
					"detail-category"
				)
			}
		}

		private fun setOnCheckedListener(category: Category) {
			binding.swCmStatus.setOnCheckedChangeListener { v, _ ->
				run {
					category.isActive = v.isChecked
					viewModel.updateCategory(category)
				}
			}
		}
	}
}
