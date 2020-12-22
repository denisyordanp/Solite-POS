package com.sosialite.solite_pos.view.main.menu.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.data.source.local.entity.helper.Order
import com.sosialite.solite_pos.databinding.RvOrderListBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.thousand

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

		val totalItem = "Banyaknya ${order.totalItem} barang"
		val totalPay = "Rp. ${thousand(order.totalPay)}"

		holder.binding.tvOrName.text = order.name
		holder.binding.tvOrTotalItem.text = totalItem
		holder.binding.tvOrTotalPay.text = totalPay
		setCookTime(holder.binding.tvOrTime, order.strFinishCook)

		holder.itemView.setOnClickListener {
//			toDetailOrder
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(val binding: RvOrderListBinding) : RecyclerView.ViewHolder(binding.root)

	private fun setCookTime(view: TextView, time: String?){
		if(time.isNullOrEmpty()){
			view.setBackgroundColor(Color.RED)
			view.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(view.resources, R.drawable.ic_warning, null), null,null,null)
		}
		view.text = time
	}
}
