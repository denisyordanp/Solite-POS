package com.socialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.helper.ProductMixOrderDetail
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.databinding.RvItemOrderMixListBinding
import com.socialite.solite_pos.databinding.RvTotalItemOrderMixListBinding
import com.socialite.solite_pos.utils.config.FindProductOrderIndex

class ItemOrderMixListAdapter : RecyclerView.Adapter<ItemOrderMixListAdapter.BaseViewHolder<ProductOrderDetail>>() {

	var btnCallback: ((Boolean) -> Unit)? = null

	val items: ArrayList<ProductOrderDetail> = ArrayList()
	val sortedItems: ArrayList<ProductMixOrderDetail>
	get() {
		val items: ArrayList<ProductMixOrderDetail> = ArrayList()
		for (item in this.items){
			if (item.product != null){
				items.add(ProductMixOrderDetail(item.product!!, item.variants, item.amount))
			}
		}
		return items
	}

	val totalItem: Int
	get() {
		var total = 0
		for (item in items){
			if (item.type != ProductOrderDetail.GRAND_TOTAL){
				total += item.amount
			}
		}
		return total
	}

	fun addItem(detail: ProductOrderDetail){
		if (items.isEmpty()){
			if (detail.amount != 0){
				setData()
				add(detail)
			}
		}else{
			add(detail)
		}
	}

	private fun add(detail: ProductOrderDetail){
		val pos = FindProductOrderIndex.find(items, detail)
		if (pos != null){
			if (detail.amount == 0){
				delItem(pos)
				btnCallback?.invoke(false)
			}else{
				items[pos] = detail
				notifyItemChanged(pos)
				btnCallback?.invoke(true)
			}
		}else{
			if (detail.amount != 0){
				items.add(0, detail)
				notifyItemInserted(0)
				btnCallback?.invoke(true)
			}else{
				btnCallback?.invoke(false)
			}
		}
		notifyItemChanged(items.size-1)
	}

	private fun delItem(pos: Int){
		if (items.size == 2){
			items.clear()
			notifyDataSetChanged()
		}else{
			items.removeAt(pos)
			notifyItemRemoved(pos)
			notifyItemChanged(items.size-1)
		}
	}

	companion object{
		private const val VALUE_COLUMN = 1
		private const val TOTAL_COLUMN = 2
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ProductOrderDetail> {
		return when (viewType){
			VALUE_COLUMN -> ValueOrderColumnViewHolder(RvItemOrderMixListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
			TOTAL_COLUMN -> TotalOrderColumnViewHolder(RvTotalItemOrderMixListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
			else -> throw IllegalArgumentException("Invalid viewType")
		}
	}

	override fun onBindViewHolder(holder: BaseViewHolder<ProductOrderDetail>, position: Int) {
		holder.bind(items[position])
	}

	override fun getItemViewType(position: Int): Int {
		val item = items[position]
		return when(item.type){
			null -> VALUE_COLUMN
			else -> TOTAL_COLUMN
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	private fun setData(){
		items.add(ProductOrderDetail.grand)
	}

	inner class ValueOrderColumnViewHolder(private val binding: RvItemOrderMixListBinding) : BaseViewHolder<ProductOrderDetail>(binding.root) {
		override fun bind(detail: ProductOrderDetail) {

			val no = "$adapterPosition."
			val amount = "${detail.amount}x"
			val variants = StringBuilder()
			for (variant in detail.variants){
				if (variants.isNotEmpty()){
					variants.append(", ")
				}
				variants.append(variant.name)
			}

			binding.tvIoNo.text = no
			binding.tvIoAmount.text = amount
			binding.tvIoName.text = detail.product?.name
			binding.tvIoVariant.text = variants

			binding.btnOlDelete.setOnClickListener {
				if (items.size == 2){
					items.clear()
					notifyDataSetChanged()
				}else{
					items.remove(detail)
					notifyItemRemoved(adapterPosition)
					notifyItemChanged(items.size-1)
				}
				btnCallback?.invoke(true)
			}
		}
	}

	inner class TotalOrderColumnViewHolder(private val binding: RvTotalItemOrderMixListBinding) : BaseViewHolder<ProductOrderDetail>(binding.root) {
		override fun bind(detail: ProductOrderDetail) {
			binding.tvIoName.text = "Total : "
			binding.tvIoTotal.text = totalItem.toString()
		}
	}

	abstract class BaseViewHolder<ProductOrderDetail>(itemView: View) : RecyclerView.ViewHolder(itemView){
		abstract fun bind(detail: ProductOrderDetail)
	}
}
