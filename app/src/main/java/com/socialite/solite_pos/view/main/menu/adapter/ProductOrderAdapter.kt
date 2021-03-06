package com.socialite.solite_pos.view.main.menu.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.databinding.RvProductBinding
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah
import com.socialite.solite_pos.view.main.menu.master.dialog.DetailOrderProductFragment
import com.socialite.solite_pos.view.main.menu.order.SelectMixVariantOrderActivity

class ProductOrderAdapter(
	private val type: Int,
	private val activity: FragmentActivity,
	private var callback: ((ProductOrderDetail) -> Unit)?
) : RecyclerView.Adapter<ProductOrderAdapter.ListViewHolder>() {

	var items: ArrayList<ProductWithCategory> = ArrayList()
		@SuppressLint("NotifyDataSetChanged")
		set(value) {
			if (field.isNotEmpty()) {
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
		var price = 0L
		when (type) {
			DetailOrderProductFragment.ORDER -> {
				price = p.product.sellPrice
				holder.setStock(p.product, true)
			}
			DetailOrderProductFragment.PURCHASE -> {
				price = p.product.buyPrice
			}
			DetailOrderProductFragment.MIX -> {
				holder.binding.tvPmvPrice.visibility = View.GONE
				holder.setStock(p.product, false)
			}
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
                DetailOrderProductFragment(type, p.product, callback).show(activity.supportFragmentManager, "detail-order-product")
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

		fun setStock(product: Product, isOrder: Boolean) {

			if (product.isMix) {
				binding.tvPmvStock.visibility = View.GONE
			} else {
				binding.tvPmvStock.text = if (isOrder) {
					"Sisa ${product.getStockPortion()} porsi"
				} else {
					"Sisa ${product.stock} pcs"
				}
			}
		}
	}
}
