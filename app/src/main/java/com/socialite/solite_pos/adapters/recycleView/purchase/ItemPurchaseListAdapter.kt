package com.socialite.solite_pos.adapters.recycleView.purchase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseProductWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.socialite.solite_pos.databinding.*
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah

class ItemPurchaseListAdapter(private val type: Int) : RecyclerView.Adapter<ItemPurchaseListAdapter.BaseViewHolder<PurchaseProductWithProduct>>() {

	companion object{
		private const val FIRST_COLUMN = 0
		private const val VALUE_COLUMN = 1
		private const val TOTAL_COLUMN = 2

		const val PURCHASE = 0
		const val DETAIL = 1
	}

	var btnCallback: ((Boolean) -> Unit)? = null

	var purchase: PurchaseWithProduct? = null
	set(value) {
		if (value != null){
			field = value
			if (items.isNotEmpty()){
				items.clear()
			}
			items.addAll(value.products)
			if (type == DETAIL){
				setData()
			}
			btnCallback?.invoke(false)
			notifyDataSetChanged()
		}
	}

	val newPurchase: PurchaseWithProduct?
	get() {
		return if (purchase != null){
			PurchaseWithProduct(
					purchase!!.purchase,
					purchase!!.supplier,
					sortedItems
			)
		}else{
			null
		}
	}

	private val items: ArrayList<PurchaseProductWithProduct> = ArrayList()
	private val sortedItems: ArrayList<PurchaseProductWithProduct>
	get() {
		val items: ArrayList<PurchaseProductWithProduct> = this.items
		if (type == DETAIL){
			items.remove(PurchaseProductWithProduct.title)
		}
		items.remove(PurchaseProductWithProduct.grand)
		return items
	}

	private val grandTotal: Long
	get() {
		var total = 0L
		for (item in items){
			if (item.product != null && item.purchaseProduct != null){
				total += item.product!!.buyPrice * item.purchaseProduct!!.amount
			}
		}
		return total
	}

	fun addItem(detail: PurchaseProductWithProduct){
		if (items.isEmpty()){
			if (detail.purchaseProduct?.amount != 0){
				setData()
				add(detail)
			}
		}else{
			add(detail)
		}
	}

	private fun add(detail: PurchaseProductWithProduct){
		val pos = findProductPurchaseIndex(items, detail)
		if (pos != null){
			if (detail.purchaseProduct?.amount == 0){
				delItem(pos)
				btnCallback?.invoke(false)
			}else{
				items[pos] = detail
				notifyItemChanged(pos)
				btnCallback?.invoke(true)
			}
		}else{
			if (detail.purchaseProduct?.amount != 0){
				items.add(0, detail)
				notifyItemInserted(0)
				btnCallback?.invoke(true)
			}else{
				btnCallback?.invoke(false)
			}
		}
		notifyItemChanged(items.size-1)
	}

	private fun findProductPurchaseIndex(array: ArrayList<PurchaseProductWithProduct>, detail: PurchaseProductWithProduct?): Int?{
		for ((i, v) in array.withIndex()){
			if (v.product != null){
				if (v.product == detail?.product){
					return i
				}
			}
		}
		return null
	}

	private fun delItem(pos: Int){
		if (items.size == 2){
			items.clear()
			notifyDataSetChanged()
		}else{
			items.removeAt(pos)
			notifyItemRemoved(pos)
			notifyItemChanged(items.size-1)
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<PurchaseProductWithProduct> {
		return when (viewType){
			FIRST_COLUMN -> {
				FirstColumnViewHolder(RvFirstColumnItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
			}
			VALUE_COLUMN -> {
				when(type){
					DETAIL -> ValueDetailColumnViewHolder(RvDetailItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
					PURCHASE -> ValueOrderColumnViewHolder(RvItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
					else -> throw IllegalArgumentException("Invalid adapterType")
				}
			}
			TOTAL_COLUMN -> {
				when(type){
					DETAIL -> ValueDetailColumnViewHolder(RvDetailItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
					PURCHASE -> TotalPurchaseColumnViewHolder(RvTotalItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
					else -> throw IllegalArgumentException("Invalid adapterType")
				}
			}
			else -> throw IllegalArgumentException("Invalid viewType")
		}
	}

	override fun onBindViewHolder(holder: BaseViewHolder<PurchaseProductWithProduct>, position: Int) {
		holder.bind(items[position])
	}

	override fun getItemViewType(position: Int): Int {
		val item = items[position]
		return when(item.type){
			null -> VALUE_COLUMN
			ProductOrderDetail.TITLE -> FIRST_COLUMN
			else -> TOTAL_COLUMN
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	private fun setData(){
		if (type == DETAIL){
			items.add(0, PurchaseProductWithProduct.title)
		}
		items.add(0, PurchaseProductWithProduct.grand)
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

	inner class ValueOrderColumnViewHolder(private val binding: RvItemOrderListBinding) : BaseViewHolder<PurchaseProductWithProduct>(binding.root) {
		override fun bind(detail: PurchaseProductWithProduct) {
			if (detail.product != null && detail.purchaseProduct != null){
				val no = "$adapterPosition."
				val amount = "${detail.purchaseProduct!!.amount}x"
				val total = detail.product!!.buyPrice * detail.purchaseProduct!!.amount

				binding.tvIoNo.text = no
				binding.tvIoAmount.text = amount
				binding.tvIoName.text = detail.product?.name
				binding.tvIoPrice.text = toRupiah(detail.product?.buyPrice)
				binding.tvIoTotal.text = toRupiah(total)

				binding.btnOlDelete.setOnClickListener {
					if (items.size == 2){
						items.clear()
						notifyDataSetChanged()
					}else{
						items.remove(detail)
						notifyItemRemoved(adapterPosition)
						notifyItemChanged(items.size-1)
					}
					btnCallback?.invoke(true)
				}
			}
		}
	}

	inner class TotalPurchaseColumnViewHolder(private val binding: RvTotalItemOrderListBinding) : BaseViewHolder<PurchaseProductWithProduct>(binding.root) {
		override fun bind(detail: PurchaseProductWithProduct) {
			binding.tvIoPrice.text = "Total :"
			binding.tvIoTotal.text = toRupiah(grandTotal)
		}

	}

	abstract class BaseViewHolder<ProductOrderDetail>(itemView: View) : RecyclerView.ViewHolder(itemView) {
		abstract fun bind(detail: PurchaseProductWithProduct)
	}
}
