package com.sosialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.data.source.local.entity.helper.RecapData
import com.sosialite.solite_pos.databinding.RvRecapBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.toRupiah

class RecapAdapter : RecyclerView.Adapter<RecapAdapter.ListViewHolder>() {

	var items: ArrayList<RecapData> = ArrayList()
		set(value) {
			if (field.isNotEmpty()){
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}

	val grandTotal: Int
	get() {
		var total = 0
		for (item in items){
			total += item.total
		}
		return total
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvRecapBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		if (position == items.size){
			holder.binding.tvRvRcName.text = "Grand Total : "
			holder.binding.tvRvRcTotal.text = toRupiah(grandTotal)
		}else{
			val r = items[position]

			holder.binding.tvRvRcName.text = r.name
			holder.binding.tvRvRcTotal.text = toRupiah(r.total)
		}
	}

	override fun getItemCount(): Int {
		return items.size+1
	}

	class ListViewHolder(var binding: RvRecapBinding) : RecyclerView.ViewHolder(binding.root)
}
