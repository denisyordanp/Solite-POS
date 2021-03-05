package com.socialite.solite_pos.view.main.menu.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.databinding.RvProductBinding
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.view.main.menu.master.dialog.DetailOrderProductFragment
import com.socialite.solite_pos.view.main.menu.order.SelectMixVariantOrderActivity

class ProductOrderAdapter(
		private val type: Int,
		private val activity: SocialiteActivity,
		private var callback: ((ProductOrderDetail) -> Unit)?
) : RecyclerView.Adapter<ProductOrderAdapter.ListViewHolder>() {

	var items: ArrayList<ProductWithCategory> = ArrayList()
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
		val price = when (type) {
			DetailOrderProductFragment.ORDER -> {
				p.product.sellPrice
			}
			DetailOrderProductFragment.PURCHASE -> {
				p.product.buyPrice
			}
			DetailOrderProductFragment.MIX -> {
				holder.binding.tvPmvPrice.visibility = View.GONE
				0
			}
			else -> 0
		}

		holder.binding.tvPmvName.text = p.product.name
		holder.setPrice(price)
		holder.itemView.setOnClickListener {
			if (p.product.isMix){
				activity.startActivityForResult(
						Intent(activity, SelectMixVariantOrderActivity::class.java)
								.putExtra(SelectMixVariantOrderActivity.PRODUCT, p.product),
						SelectMixVariantOrderActivity.RC_MIX
				)
			}else{
				DetailOrderProductFragment(type, p, callback).show(activity.supportFragmentManager, "detail-order-product")
			}
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(val binding: RvProductBinding) : RecyclerView.ViewHolder(binding.root){
		fun setPrice(price: Long?){
			if (price != null){
				binding.tvPmvPrice.text = toRupiah(price)
			}
		}
	}
}
