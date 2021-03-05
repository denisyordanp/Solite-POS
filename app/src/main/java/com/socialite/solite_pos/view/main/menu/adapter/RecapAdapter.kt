package com.socialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.helper.RecapData
import com.socialite.solite_pos.databinding.RvRecapBinding
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah

class RecapAdapter : RecyclerView.Adapter<RecapAdapter.ListViewHolder>() {

	var items: ArrayList<RecapData> = ArrayList()
		set(value) {
			if (field.isNotEmpty()){
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}

	val grandTotal: Long
	get() {
		var total = 0L
		for (item in items){
			total += item.total
		}
		return total
	}

	fun getIncome(isCash: Boolean): Long {
		var total = 0L
		for (item in items){
			if (item.isCash != null){
				if (item.isCash!! == isCash) total += item.total
			}
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
			if (position == 0){
				holder.binding.vRvRcLine.visibility = View.INVISIBLE
			}
			val r = items[position]

			holder.binding.tvRvRcName.text = r.name
			holder.binding.tvRvRcDesc.text = r.desc
			holder.binding.tvRvRcTotal.text = toRupiah(r.total)
		}
	}

	override fun getItemCount(): Int {
		return items.size+1
	}

	class ListViewHolder(var binding: RvRecapBinding) : RecyclerView.ViewHolder(binding.root)
}
