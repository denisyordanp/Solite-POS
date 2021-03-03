package com.socialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.R
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.databinding.RvStringListBinding

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

		if (user.id == Customer.ID_ADD){
			holder.setAdd(true)
		}else{
			holder.setAdd(false)
		}
		holder.binding.tvRvSt.text = user.name
		holder.itemView.setOnClickListener { callback.invoke(user) }
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(val binding: RvStringListBinding) : RecyclerView.ViewHolder(binding.root){
		fun setAdd(state: Boolean){
			if (state){
				val add = ResourcesCompat.getDrawable(binding.root.resources, R.drawable.ic_add_circle, null)
				binding.tvRvSt.setCompoundDrawablesRelativeWithIntrinsicBounds(add, null,null,null)
			}else{
				binding.tvRvSt.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,null,null)
			}
		}
	}
}
