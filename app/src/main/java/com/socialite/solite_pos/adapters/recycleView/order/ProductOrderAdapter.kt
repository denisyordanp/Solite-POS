package com.socialite.solite_pos.adapters.recycleView.order

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.databinding.RvProductBinding
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah
import com.socialite.solite_pos.utils.tools.RecycleViewDiffUtils
import com.socialite.solite_pos.view.main.menu.master.dialog.DetailOrderProductFragment
import com.socialite.solite_pos.view.main.menu.order.SelectMixVariantOrderActivity

class ProductOrderAdapter(
	private val type: Int,
	private val activity: FragmentActivity,
	private var callback: ((ProductOrderDetail) -> Unit)?
) : RecyclerView.Adapter<ProductOrderAdapter.ListViewHolder>() {

	private var products: ArrayList<ProductWithCategory> = ArrayList()
	fun setProducts(products: List<ProductWithCategory>) {
		val orderDiffUtil = RecycleViewDiffUtils(this.products, products)
		val diffUtilResult = DiffUtil.calculateDiff(orderDiffUtil)
		this.products = ArrayList(products)
		diffUtilResult.dispatchUpdatesTo(this)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = RvProductBinding.inflate(inflater, parent, false)
		return ListViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		holder.setDataToView(products[position].product)
	}

	override fun getItemCount(): Int {
		return products.size
	}

	inner class ListViewHolder(val binding: RvProductBinding) : RecyclerView.ViewHolder(binding.root){

		fun setDataToView(product: Product) {
			setProductName(product.name)
			setStockAndPriceView(product)
			setClickListener(product)
		}

		private fun setProductName(name: String) {
			binding.tvPmvName.text = name
		}

		private fun setStockAndPriceView(product: Product) {
			var price = 0L
			when (type) {
				DetailOrderProductFragment.ORDER -> {
					price = product.sellPrice
					setStock(product, true)
				}
				DetailOrderProductFragment.PURCHASE -> {
					price = product.buyPrice
				}
				DetailOrderProductFragment.MIX -> {
					binding.tvPmvPrice.visibility = View.GONE
					setStock(product, false)
				}
			}
			setPrice(price)
		}

		private fun setPrice(price: Long?){
			if (price != null){
				binding.tvPmvPrice.text = toRupiah(price)
			}
		}

		private fun setStock(product: Product, isOrder: Boolean) {
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

		private fun setClickListener(product: Product) {
			binding.root.setOnClickListener {
				if (product.isMix){
					activity.startActivityForResult(
						Intent(activity, SelectMixVariantOrderActivity::class.java)
							.putExtra(SelectMixVariantOrderActivity.PRODUCT, product),
						SelectMixVariantOrderActivity.RC_MIX
					)
				}else{
					DetailOrderProductFragment(type, product, callback).show(activity.supportFragmentManager, "detail-order-product")
				}
			}
		}
	}
}
