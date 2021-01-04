package com.sosialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.data.source.local.entity.helper.DetailOrder
import com.sosialite.solite_pos.data.source.local.entity.helper.Order
import com.sosialite.solite_pos.databinding.RvProductListBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.toRupiah

class ProductListAdapter(private var order: Order?) : RecyclerView.Adapter<ProductListAdapter.ListViewHolder>() {
	var callback: ((Boolean, DetailOrder) -> Unit)? = null

	var items: ArrayList<DetailOrder> = ArrayList()
		set(value) {
			if (field.isNotEmpty()){
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}

	constructor(): this(null)

	fun deleteData(code: Int){
		for ((i, v) in items.withIndex()){
			if (v.product != null){
				if (v.product!!.id == code){
					items[i].amount = 0
					notifyItemChanged(i)
				}
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val d = items[position]
		holder.setFirst(d.amount)

		holder.binding.tvPlName.text = d.product?.name
		holder.binding.tvPlPrice.text = toRupiah(d.product?.price)
		holder.binding.tvPlAmount.text = d.amount.toString()
		holder.setMinButton(d.amount)
//		set image

		holder.binding.btnPlMin.setOnClickListener {
			if (order?.status == Order.NEED_PAY){
				if (holder.firstAmount != null){
					val amount = min(position, holder.binding.tvPlAmount)
					if (amount <= holder.firstAmount!!){
						holder.setMinButton(false)
					}else{
						holder.setMinButton(amount)
					}
				}
			}else{
				holder.setMinButton(min(position, holder.binding.tvPlAmount))
			}

		}
		holder.binding.btnPlPlus.setOnClickListener {
			holder.setMinButton(add(position, holder.binding.tvPlAmount))
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(val binding: RvProductListBinding) : RecyclerView.ViewHolder(binding.root){
		var firstAmount: Int? = null

		fun setFirst(amount: Int){
			if (firstAmount == null){
				firstAmount = amount
			}
		}

		fun setMinButton(am: Int){
			if (am <= 0){
				setMinButton(false)
			}else{
				setMinButton(true)
			}
		}

		fun setMinButton(state: Boolean){
			if (state){
				binding.btnPlMin.visibility = View.VISIBLE
			}else{
				binding.btnPlMin.visibility = View.INVISIBLE
			}
		}
	}

	private fun add(pos: Int, view: TextView): Int{
		val amount = items[pos].amount+1
		items[pos].amount = amount
		view.text = amount.toString()

		callback?.invoke(true, items[pos])

		return amount
	}

	private fun min(pos: Int, view: TextView): Int{
		val amount = items[pos].amount-1
		items[pos].amount = amount
		view.text = amount.toString()

		callback?.invoke(false, items[pos])

		return amount
	}
}
