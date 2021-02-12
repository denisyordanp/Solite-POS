package com.sosialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.sosialite.solite_pos.databinding.RvPurchaseBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.dateFormat
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.ldFormat
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.toRupiah

class PurchaseAdapter(private val fragmentManager: FragmentManager) : RecyclerView.Adapter<PurchaseAdapter.ListViewHolder>() {

	var items: ArrayList<PurchaseWithProduct> = ArrayList()
		set(value) {
			if (field.isNotEmpty()){
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}

	fun addPurchase(purchase: PurchaseWithProduct){
		items.add(0, purchase)
		notifyItemInserted(0)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvPurchaseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val p = items[position]

		holder.binding.tvRvPcDate.text = dateFormat(p.purchase.purchaseTime, ldFormat)
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
