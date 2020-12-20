package com.sosialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.data.source.local.entity.helper.Order
import com.sosialite.solite_pos.databinding.RvOrderListBinding

class OrderListAdapter : RecyclerView.Adapter<OrderListAdapter.ListViewHolder>() {
	private val items: ArrayList<Order> = ArrayList()

	fun setItems(items: ArrayList<Order>){
		if (this.items.isNotEmpty()){
			this.items.clear()
		}
		this.items.addAll(items)
		notifyDataSetChanged()
	}

	fun addItem(item: Order){
		items.add(0, item)
		notifyItemInserted(0)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val order = items[position]

		holder.binding.tvOrName.text = order.name
		holder.binding.tvOrTime.text = order.strFinishCook
		holder.binding.tvOrTotalItem.text = order.totalItem.toString()
		holder.binding.tvOrTotalPay.text = order.totalPay.toString()

		holder.itemView.setOnClickListener {
//			toDetailOrder
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(val binding: RvOrderListBinding) : RecyclerView.ViewHolder(binding.root)
}
