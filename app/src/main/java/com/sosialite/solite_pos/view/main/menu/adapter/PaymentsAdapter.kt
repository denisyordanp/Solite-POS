package com.sosialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.data.source.local.entity.room.master.Payment
import com.sosialite.solite_pos.databinding.RvPaymentsListBinding

class PaymentsAdapter(private val callback: (Payment) -> Unit) : RecyclerView.Adapter<PaymentsAdapter.ListViewHolder>() {

	var items: ArrayList<Payment> = ArrayList()
		set(value) {
			if (field.isNotEmpty()){
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvPaymentsListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val p = items[position]

		val tax = "Pajak +${p.tax}%"
		holder.binding.tvRvPmsName.text = p.name
		holder.binding.tvRvPmsDesc.text = p.desc
		holder.binding.tvRvPmsTax.text = tax

		holder.itemView.setOnClickListener {
			callback.invoke(p)
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(var binding: RvPaymentsListBinding) : RecyclerView.ViewHolder(binding.root)
}
