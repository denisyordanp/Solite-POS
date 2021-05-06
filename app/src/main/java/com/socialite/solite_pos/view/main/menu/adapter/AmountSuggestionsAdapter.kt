package com.socialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.databinding.RvSugestionsBinding
import com.socialite.solite_pos.utils.config.RupiahUtils

class AmountSuggestionsAdapter(private val callback: (Int) -> Unit) :
	RecyclerView.Adapter<AmountSuggestionsAdapter.ListViewHolder>() {

	var items: ArrayList<Int> = ArrayList()
		set(value) {
			if (field.isNotEmpty()) {
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(
			RvSugestionsBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val s = items[position]

		val str = RupiahUtils.toRupiah(s.toLong())
		holder.binding.tvRvSuggestion.text = str

		holder.itemView.setOnClickListener {
			callback.invoke(s)
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(var binding: RvSugestionsBinding) : RecyclerView.ViewHolder(binding.root)
}
