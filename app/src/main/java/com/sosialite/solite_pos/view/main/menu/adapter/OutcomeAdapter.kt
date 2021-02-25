package com.sosialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.data.source.local.entity.room.master.Outcome
import com.sosialite.solite_pos.databinding.RvOutcomeBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.currentDate
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.dateFormat
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.sdFormat
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.toRupiah
import com.sosialite.solite_pos.view.main.menu.bottom.DetailOutcomeFragment

class OutcomeAdapter(private val fragmentManager: FragmentManager) : RecyclerView.Adapter<OutcomeAdapter.ListViewHolder>() {

	var items: ArrayList<Outcome> = ArrayList()
		set(value) {
			if (field.isNotEmpty()){
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}

	fun addItem(outcome: Outcome){
		items.add(outcome)
		notifyItemInserted(0)
	}

	fun editItem(outcome: Outcome){
		for ((i, item) in items.withIndex()){
			if (item.id == outcome.id){
				items[i] = outcome
				notifyItemChanged(i)
			}
		}
	}

	private val grandTotal: Long
	get() {
		var total = 0L
		for (item in items){
			total += item.total
		}
		return total
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvOutcomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		if (position == 0){
			holder.binding.tvRvOcName.text = "Total pengeluaran : "
			holder.binding.tvRvOcDesc.text = dateFormat(currentDate, sdFormat)
			holder.binding.tvRvOcTotal.text = toRupiah(grandTotal)
		}else{
			val o = items[position-1]
			val name = "${o.amount}x ${o.name}"

			holder.binding.tvRvOcName.text = name
			holder.binding.tvRvOcDesc.text = o.desc
			holder.binding.tvRvOcTotal.text = toRupiah(o.total)

			holder.itemView.setOnClickListener {
				DetailOutcomeFragment(o).show(fragmentManager, "detail-outcome")
			}
		}
	}

	override fun getItemCount(): Int {
		return items.size+1
	}

	class ListViewHolder(var binding: RvOutcomeBinding) : RecyclerView.ViewHolder(binding.root)
}
