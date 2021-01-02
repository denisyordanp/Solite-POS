package com.sosialite.solite_pos.view.main.menu.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.data.source.local.entity.helper.DetailOrder
import com.sosialite.solite_pos.databinding.RvProductListBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.toRupiah

class ProductListAdapter : RecyclerView.Adapter<ProductListAdapter.ListViewHolder>() {
	private val items: ArrayList<DetailOrder> = ArrayList()
	var callback: ((Boolean, DetailOrder) -> Unit)? = null

	fun setItems(items: ArrayList<DetailOrder>){
		if (this.items.isNotEmpty()){
			this.items.clear()
		}
		this.items.addAll(items)
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val d = items[position]

		holder.binding.tvPlName.text = d.product?.name
		holder.binding.tvPlPrice.text = toRupiah(d.product?.price)
		holder.binding.tvPlAmount.text = d.amount.toString()
//		set image

		holder.binding.btnPlPlus.setOnClickListener {
			add(position, holder.binding.tvPlAmount)
		}
		holder.binding.btnPlMin.setOnClickListener {
			min(position, holder.binding.tvPlAmount)
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(val binding: RvProductListBinding) : RecyclerView.ViewHolder(binding.root)

	private fun add(pos: Int, view: TextView){
		val amount = items[pos].amount+1
		items[pos].amount = amount
		view.text = amount.toString()

		callback?.invoke(true, items[pos])
	}

	private fun min(pos: Int, view: TextView){
		val amount = items[pos].amount-1
		items[pos].amount = amount
		view.text = amount.toString()

		callback?.invoke(false, items[pos])
	}
}
