package com.socialite.solite_pos.adapters.recycleView.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.databinding.RvSugestionsBinding
import com.socialite.solite_pos.utils.config.RupiahUtils
import com.socialite.solite_pos.utils.tools.RecycleViewDiffUtils

class AmountSuggestionsAdapter(private val callback: (Int) -> Unit) :
	RecyclerView.Adapter<AmountSuggestionsAdapter.ListViewHolder>() {

	private var amounts: ArrayList<Int> = ArrayList()

	fun setAmounts(amounts: ArrayList<Int>) {
		val amountsDiffUtil = RecycleViewDiffUtils(this.amounts, amounts)
		val diffUtilResult = DiffUtil.calculateDiff(amountsDiffUtil)
		this.amounts = amounts
		diffUtilResult.dispatchUpdatesTo(this)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = RvSugestionsBinding.inflate(
			inflater,
			parent,
			false
		)
		return ListViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		holder.setDataToView(amounts[position])
	}

	override fun getItemCount(): Int {
		return amounts.size
	}

	inner class ListViewHolder(var binding: RvSugestionsBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun setDataToView(amount: Int) {
			setTextView(amount)
			setClickListener(amount)
		}

		private fun setTextView(amount: Int) {
			val str = RupiahUtils.toRupiah(amount.toLong())
			binding.tvRvSuggestion.text = str
		}

		private fun setClickListener(amount: Int) {
			binding.root.setOnClickListener {
				callback.invoke(amount)
			}
		}
	}
}
