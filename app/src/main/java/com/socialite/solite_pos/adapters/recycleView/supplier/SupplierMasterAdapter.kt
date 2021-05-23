package com.socialite.solite_pos.adapters.recycleView.supplier

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.room.master.Supplier
import com.socialite.solite_pos.databinding.RvSupplierMasterBinding
import com.socialite.solite_pos.view.main.menu.master.bottom.SupplierMasterFragment

class SupplierMasterAdapter(
	private val fragmentManager: FragmentManager
	) : RecyclerView.Adapter<SupplierMasterAdapter.ListViewHolder>() {

	var items: ArrayList<Supplier> = ArrayList()
		set(value) {
			if (field.isNotEmpty()){
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvSupplierMasterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val p = items[position]

		holder.binding.tvRvSmName.text = p.name
		holder.binding.tvRvSmPhone.text = p.phone
		holder.binding.tvRvSmDesc.text = p.desc
		holder.binding.tvRvSmAddress.text = p.address

		holder.itemView.setOnClickListener {
			SupplierMasterFragment(p).show(fragmentManager, "detail-supplier")
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(var binding: RvSupplierMasterBinding) : RecyclerView.ViewHolder(binding.root)
}
