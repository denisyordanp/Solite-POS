package com.socialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseProductWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.helper.PurchaseWithSupplier
import com.socialite.solite_pos.databinding.RvPurchaseBinding
import com.socialite.solite_pos.utils.config.DateUtils.Companion.dateFormat
import com.socialite.solite_pos.utils.config.DateUtils.Companion.dateWithTimeFormat
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.vo.Status

class PurchaseAdapter(
		private val viewModel: MainViewModel,
		private val activity: FragmentActivity
		) : RecyclerView.Adapter<PurchaseAdapter.ListViewHolder>() {

	var items: ArrayList<PurchaseWithSupplier> = ArrayList()
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

		holder.getPurchaseProducts(p.purchase.purchaseNo)
	}

	override fun getItemCount(): Int {
		return items.size
	}

	inner class ListViewHolder(var binding: RvPurchaseBinding) : RecyclerView.ViewHolder(binding.root){
		fun getPurchaseProducts(purchaseNo: String){
			viewModel.getPurchaseProducts(purchaseNo).observe(activity){
				when(it.status){
					Status.LOADING -> {}
					Status.SUCCESS -> {
						if (it.data.isNullOrEmpty()) return@observe
						setPurchaseProducts(it.data)
					}
					Status.ERROR -> {}
				}
			}
		}

		private fun setPurchaseProducts(products: List<PurchaseProductWithProduct>){
			binding.tvRvPcTotal.text = toRupiah(getTotalPurchases(products))
			binding.root.setOnClickListener {

			}
		}

		private fun getTotalPurchases(products: List<PurchaseProductWithProduct>): Long{
			var total = 0L
			for (item in products){
				if (item.purchaseProduct != null && item.product != null){
					total += item.purchaseProduct!!.amount * item.product!!.buyPrice
				}
			}
			return total
		}
	}
}
