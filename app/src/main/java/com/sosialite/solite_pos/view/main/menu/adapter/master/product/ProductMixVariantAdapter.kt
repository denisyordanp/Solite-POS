package com.sosialite.solite_pos.view.main.menu.adapter.master.product

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

	var items: ArrayList<ProductWithCategory> = ArrayList()
		set(value) {
			if (field.isNotEmpty()){
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}

	var selected: ArrayList<Product> = ArrayList()
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
//		set image

//		holder.binding.btnPlMin.setOnClickListener {
//			if (order?.status == Order.NEED_PAY){
//				if (holder.firstAmount != null){
//					val amount = min(position, holder.binding.tvPlAmount)
//					if (amount <= holder.firstAmount!!){
//						holder.setMinButton(false)
//					}else{
//						holder.setMinButton(amount)
//					}
//				}
//			}else{
//				holder.setMinButton(min(position, holder.binding.tvPlAmount))
//			}
//
//		}
//		holder.binding.btnPlPlus.setOnClickListener {
//			holder.setMinButton(add(position, holder.binding.tvPlAmount))
//		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	inner class ListViewHolder(val binding: RvProductBinding) : RecyclerView.ViewHolder(binding.root){
		var firstAmount: Int? = null

		private var isChecked: Boolean = false

		fun setContent(position: Int){
			when(type){
				MASTER -> {
					binding.contPmvCount.visibility = View.GONE
					binding.cbPmvSelected.visibility = View.VISIBLE
					binding.root.setOnClickListener {
						if (!isChecked){
							if (items[position].product != null){
								addCallback?.invoke(items[position].product!!)
								selected.add(items[position].product!!)
							}
						}else{
							if (items[position].product != null){
								delCallback?.invoke(items[position].product!!)
								selected.remove(items[position].product!!)
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

		fun setSelected(product: Product?){
			for (item in selected){
				isSelected(item.id == product?.id)
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

//		fun setFirst(amount: Int){
//			if (firstAmount == null){
//				firstAmount = amount
//			}
//		}

//		fun setMinButton(am: Int){
//			if (am <= 0){
//				setMinButton(false)
//			}else{
//				setMinButton(true)
//			}
//		}

//		fun setMinButton(state: Boolean){
//			if (state){
//				binding.btnPlMin.visibility = View.VISIBLE
//			}else{
//				binding.btnPlMin.visibility = View.INVISIBLE
//			}
//		}
	}

	private fun add(pos: Int, view: TextView): Int{
//		val amount = items[pos].amount+1
//		items[pos].amount = amount
//		view.text = amount.toString()

//		callback?.invoke(true, items[pos])

//		return amount
		return 0
	}

	private fun min(pos: Int, view: TextView): Int{
//		val amount = items[pos].amount-1
//		items[pos].amount = amount
//		view.text = amount.toString()
//
//		callback?.invoke(false, items[pos])
//
//		return amount
		return 0
	}
}
