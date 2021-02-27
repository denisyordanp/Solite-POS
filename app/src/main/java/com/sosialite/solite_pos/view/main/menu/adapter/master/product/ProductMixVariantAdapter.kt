package com.sosialite.solite_pos.view.main.menu.adapter.master.product

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import com.sosialite.solite_pos.databinding.RvProductBinding

class ProductMixVariantAdapter(
	private val type: Int,
	) : RecyclerView.Adapter<ProductMixVariantAdapter.ListViewHolder>() {

	companion object{
		const val MASTER = 1
		const val ORDER = 2
	}

	var addCallback: ((Product) -> Unit)? = null
	var delCallback: ((Product) -> Unit)? = null

	var selected: ArrayList<Product> = ArrayList()
		set(value) {
			if (field.isNotEmpty()){
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}
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

		holder.setContent(position)
		holder.binding.tvPmvName.text = p.product?.name
		holder.setSelected(p.product)
	}

	override fun getItemCount(): Int {
		return items.size
	}

	inner class ListViewHolder(val binding: RvProductBinding) : RecyclerView.ViewHolder(binding.root){

		private var isChecked: Boolean = false

		fun setContent(position: Int){
			when(type){
				MASTER -> {
					binding.contPmvCount.visibility = View.GONE
					binding.cbPmvSelected.visibility = View.VISIBLE
					binding.cbPmvSelected.isEnabled = false
					binding.root.setOnClickListener {
						if (!isChecked){
							if (items[position].product != null){
								addProduct(items[position].product!!)
							}
						}else{
							if (items[position].product != null){
								delProduct(items[position].product!!)
							}
						}
						notifyItemChanged(position)
					}
					binding.cbPmvSelected.setOnCheckedChangeListener{ _, b ->
						isSelected(b)
					}
				}
				ORDER -> {
					binding.contPmvCount.visibility = View.VISIBLE
					binding.cbPmvSelected.visibility = View.GONE
				}
			}
		}

		private fun addProduct(product: Product){
			addCallback?.invoke(product)
			selected.add(product)
		}

		private fun delProduct(product: Product){
			delCallback?.invoke(product)
			selected.remove(product)
		}

		fun setSelected(product: Product?){
			for (item in selected){
				if (item.id == product?.id){
					isSelected(true)
					break
				}
			}
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
