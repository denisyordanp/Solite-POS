package com.sosialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.sosialite.solite_pos.data.source.local.entity.room.helper.DataProduct
import com.sosialite.solite_pos.databinding.RvProductBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.toRupiah
import com.sosialite.solite_pos.view.main.menu.master.dialog.DetailOrderProductFragment

class ProductOrderAdapter(
		private val type: Int,
		private val fragmentManager: FragmentManager,
		private var callback: ((ProductOrderDetail) -> Unit)?
) : RecyclerView.Adapter<ProductOrderAdapter.ListViewHolder>() {

	var items: ArrayList<DataProduct> = ArrayList()
		set(value) {
			if (field.isNotEmpty()){
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val p = items[position]

		val price = if (type == DetailOrderProductFragment.ORDER){
			p.product.sellPrice
		}else{
			p.product.buyPrice
		}
		holder.binding.tvPmvName.text = p.product.name
		holder.setPrice(price)
		holder.itemView.setOnClickListener {
			DetailOrderProductFragment(type, p, callback).show(fragmentManager, "detail-order-product")
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(val binding: RvProductBinding) : RecyclerView.ViewHolder(binding.root){
		fun setPrice(price: Int?){
			if (price != null){
				binding.tvPmvPrice.visibility = View.VISIBLE
				binding.tvPmvPrice.text = toRupiah(price)
			}
		}
	}
}
