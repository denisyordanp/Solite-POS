package com.socialite.solite_pos.adapters.recycleView.purchase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseProductWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.socialite.solite_pos.databinding.*
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah
import com.socialite.solite_pos.utils.tools.RecycleViewDiffUtils

class PurchaseDetailAdapter : RecyclerView.Adapter<PurchaseDetailAdapter.BaseViewHolder<PurchaseProductWithProduct>>() {

	companion object{
		private const val FIRST_COLUMN = 0
		private const val VALUE_COLUMN = 1
		private const val TOTAL_COLUMN = 2
	}

	var btnCallback: ((Boolean) -> Unit)? = null

	private var purchasesProduct: ArrayList<PurchaseProductWithProduct> = ArrayList()
	private fun setPurchasesProduct(purchasesProduct: List<PurchaseProductWithProduct>) {
		val purchasesProductDiffUtil = RecycleViewDiffUtils(this.purchasesProduct, purchasesProduct)
		val diffUtilResult = DiffUtil.calculateDiff(purchasesProductDiffUtil)
		this.purchasesProduct = ArrayList(purchasesProduct)
		diffUtilResult.dispatchUpdatesTo(this)
	}

	var purchase: PurchaseWithProduct? = null
	set(value) {
		if (value != null){
			value.products = configureList(value.products)
			field = value
			setPurchasesProduct(value.products)
			btnCallback?.invoke(false)

		}
	}

	private fun configureList(purchases: ArrayList<PurchaseProductWithProduct>) : ArrayList<PurchaseProductWithProduct>{
		purchases.add(0, PurchaseProductWithProduct.title)
		purchases.add(0, PurchaseProductWithProduct.grand)
		return purchases
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<PurchaseProductWithProduct> {
		val inflater = LayoutInflater.from(parent.context)
		return when (viewType){
			FIRST_COLUMN -> {
				FirstColumnViewHolder(
					RvFirstColumnItemOrderListBinding.inflate(
						inflater,
						parent,
						false)
				)
			}
			VALUE_COLUMN -> {
				ValueDetailColumnViewHolder(
					RvDetailItemOrderListBinding.inflate(
						inflater,
						parent,
						false)
				)
			}
			TOTAL_COLUMN -> {
				ValueDetailColumnViewHolder(
					RvDetailItemOrderListBinding.inflate(
						inflater,
						parent,
						false)
				)
			}
			else -> throw IllegalArgumentException("Invalid viewType")
		}
	}

	override fun onBindViewHolder(holder: BaseViewHolder<PurchaseProductWithProduct>, position: Int) {
		holder.bind(purchasesProduct[position])
	}

	override fun getItemViewType(position: Int): Int {
		val item = purchasesProduct[position]
		return when(item.type){
			null -> VALUE_COLUMN
			ProductOrderDetail.TITLE -> FIRST_COLUMN
			else -> TOTAL_COLUMN
		}
	}

	override fun getItemCount(): Int {
		return purchasesProduct.size
	}

	inner class FirstColumnViewHolder(binding: RvFirstColumnItemOrderListBinding) : BaseViewHolder<PurchaseProductWithProduct>(binding.root) {
		override fun bind(detail: PurchaseProductWithProduct) {}
	}

	inner class ValueDetailColumnViewHolder(private val binding: RvDetailItemOrderListBinding) : BaseViewHolder<PurchaseProductWithProduct>(binding.root) {
		override fun bind(detail: PurchaseProductWithProduct) {
			if (detail.product != null && detail.purchaseProduct != null){
				val no = "$adapterPosition."
				val amount = "${detail.purchaseProduct?.amount}x"
				val total = detail.product!!.buyPrice * detail.purchaseProduct!!.amount

				binding.tvIoNo.text = no
				binding.tvIoAmount.text = amount
				binding.tvIoName.text = detail.product!!.name
				binding.tvIoPrice.text = toRupiah(detail.product!!.buyPrice)
				binding.tvIoTotal.text = toRupiah(total)
			}
		}
	}

	abstract class BaseViewHolder<ProductOrderDetail>(itemView: View) : RecyclerView.ViewHolder(itemView) {
		abstract fun bind(detail: PurchaseProductWithProduct)
	}
}
