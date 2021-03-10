package com.socialite.solite_pos.view.main.menu.adapter.master

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import com.socialite.solite_pos.databinding.RvCategoryMasterBinding
import com.socialite.solite_pos.view.main.menu.master.bottom.PaymentMasterFragment
import com.socialite.solite_pos.view.viewModel.MainViewModel

class PaymentMasterAdapter(
	private val viewModel: MainViewModel,
	private val fragmentManager: FragmentManager
	) : RecyclerView.Adapter<PaymentMasterAdapter.ListViewHolder>() {

	var items: ArrayList<Payment> = ArrayList()
		set(value) {
			if (field.isNotEmpty()){
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvCategoryMasterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val p = items[position]

		val title = when(p.tax){
			0f -> p.name
			else -> "${p.name}, Pajak ${p.tax}%"
		}
		holder.binding.tvRvCtName.text = title
		holder.binding.tvRvCtDesc.text = p.desc
		holder.binding.swCmStatus.isChecked = p.isActive

		holder.itemView.setOnClickListener {
			PaymentMasterFragment(p).show(fragmentManager, "detail-payment")
		}
		holder.binding.swCmStatus.setOnCheckedChangeListener{ v, _ ->
			run {
				p.isActive = v.isChecked
				viewModel.updatePayment(p) {}
			}
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(var binding: RvCategoryMasterBinding) : RecyclerView.ViewHolder(binding.root)
}
