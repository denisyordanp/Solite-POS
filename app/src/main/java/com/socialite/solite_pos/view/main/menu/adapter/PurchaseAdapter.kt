package com.socialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.socialite.solite_pos.databinding.RvPurchaseBinding
import com.socialite.solite_pos.utils.config.DateUtils.Companion.dateFormat
import com.socialite.solite_pos.utils.config.DateUtils.Companion.dateWithTimeFormat
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah

class PurchaseAdapter(private val fragmentManager: FragmentManager) : RecyclerView.Adapter<PurchaseAdapter.ListViewHolder>() {

	var items: ArrayList<PurchaseWithProduct> = ArrayList()
		set(value) {
			if (field.isNotEmpty()){
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvPurchaseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val p = items[position]

		holder.binding.tvRvPcDate.text = dateFormat(p.purchase.purchaseTime, dateWithTimeFormat)
		holder.binding.tvRvPcSupplier.text = p.supplier.name
		holder.binding.tvRvPcTotal.text = toRupiah(p.totalPurchase)

		holder.itemView.setOnClickListener {
//			DetailOutcomeFragment(o).show(fragmentManager, "detail-outcome")
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(var binding: RvPurchaseBinding) : RecyclerView.ViewHolder(binding.root)
}
