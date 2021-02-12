package com.sosialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.data.source.local.entity.room.master.Supplier
import com.sosialite.solite_pos.databinding.RvStringListBinding

class SelectSupplierAdapter(
		private val callback: (Supplier) -> Unit
) : RecyclerView.Adapter<SelectSupplierAdapter.ListViewHolder>(){

	var items: ArrayList<Supplier> = ArrayList()
	set(value) {
		if (field.isNotEmpty()){
			field.clear()
		}
		field.addAll(value)
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvStringListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val s = items[position]
		val text = "${s.name} \n ${s.address}".trimIndent()

		holder.binding.tvRvSt.text = text
		holder.binding.root.setOnClickListener { callback.invoke(s) }
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(val binding: RvStringListBinding) : RecyclerView.ViewHolder(binding.root)
}
