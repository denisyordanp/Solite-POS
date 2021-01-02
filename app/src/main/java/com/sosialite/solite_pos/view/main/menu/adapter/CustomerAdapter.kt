package com.sosialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.data.source.local.entity.Customer
import com.sosialite.solite_pos.databinding.RvStringListBinding

class CustomerAdapter(private val callback: (Customer) -> Unit) : RecyclerView.Adapter<CustomerAdapter.ListViewHolder>(){

	private val items: ArrayList<Customer> = ArrayList()

	fun setItems(items: ArrayList<Customer>){
		if (this.items.isNotEmpty()){
			this.items.clear()
		}
		this.items.addAll(items)
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvStringListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val user = items[position]

		holder.binding.tvRvSt.text = user.name
		holder.itemView.setOnClickListener { callback.invoke(user) }
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(val binding: RvStringListBinding) : RecyclerView.ViewHolder(binding.root)
}
