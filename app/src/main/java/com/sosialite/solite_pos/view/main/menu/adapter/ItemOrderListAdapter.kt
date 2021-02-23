package com.sosialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.sosialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import com.sosialite.solite_pos.databinding.*
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.productOrderIndex
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.toRupiah

class ItemOrderListAdapter(private val type: Int) : RecyclerView.Adapter<ItemOrderListAdapter.BaseViewHolder<ProductOrderDetail>>() {

	var btnCallback: ((Boolean) -> Unit)? = null

	var order: OrderWithProduct? = null
	set(value) {
		if (value != null){
			field = value
			if (items.isNotEmpty()){
				items.clear()
			}
			items.addAll(value.products)
			if (type == DETAIL || type == EDIT){
				setData()
			}
			btnCallback?.invoke(false)
			notifyDataSetChanged()
		}
	}

	val newOrder: OrderWithProduct?
	get() {
		return if (order != null){
			OrderWithProduct(
					order!!.order,
					order!!.payment,
					order!!.customer,
					sortedItems
			)
		}else{
			null
		}
	}

	private val items: ArrayList<ProductOrderDetail> = ArrayList()
	private val sortedItems: ArrayList<ProductOrderDetail>
	get() {
		val items: ArrayList<ProductOrderDetail> = this.items
		if (type == DETAIL){
			items.remove(ProductOrderDetail.title)
		}
		when(order?.order?.status){
			Order.ON_PROCESS, Order.NEED_PAY -> {
				items.remove(ProductOrderDetail.grand)
			}
			Order.DONE -> {
				items.remove(ProductOrderDetail.grand)
				items.remove(ProductOrderDetail.payment)
				items.remove(ProductOrderDetail.payReturn)
			}
		}
		return items
	}
	private val grandTotal: Int
	get() {
		var total = 0
		for (item in items){
			if (item.product != null){
				total += item.product!!.sellPrice * item.amount
			}
		}
		return total
	}

	fun addItem(detail: ProductOrderDetail){
		if (items.isEmpty()){
			if (detail.amount != 0){
				setData()
				add(detail)
			}
		}else{
			add(detail)
		}
	}

	private fun add(detail: ProductOrderDetail){
		val pos = productOrderIndex(items, detail)
		if (pos != null){
			if (detail.amount == 0){
				delItem(pos)
				btnCallback?.invoke(false)
			}else{
				items[pos] = detail
				notifyItemChanged(pos)
				btnCallback?.invoke(true)
			}
		}else{
			if (detail.amount != 0){
				items.add(0, detail)
				notifyItemInserted(0)
				btnCallback?.invoke(true)
			}else{
				btnCallback?.invoke(false)
			}
		}
		notifyItemChanged(items.size-1)
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

	companion object{
		private const val FIRST_COLUMN = 0
		private const val VALUE_COLUMN = 1
		private const val TOTAL_COLUMN = 2

		const val DETAIL = 1
		const val ORDER = 2
		const val EDIT = 3
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ProductOrderDetail> {
		return when (viewType){
			FIRST_COLUMN -> {
				FirstColumnViewHolder(RvFirstColumnItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
			}
			VALUE_COLUMN -> {
				when(type){
					DETAIL -> ValueDetailColumnViewHolder(RvDetailItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
					ORDER, EDIT -> ValueOrderColumnViewHolder(RvItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
					else -> throw IllegalArgumentException("Invalid adapterType")
				}
			}
			TOTAL_COLUMN -> {
				when(type){
					DETAIL -> TotalDetailColumnViewHolder(RvTotalDetailItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
					ORDER, EDIT -> TotalOrderColumnViewHolder(RvTotalItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
					else -> throw IllegalArgumentException("Invalid adapterType")
				}
			}
			else -> throw IllegalArgumentException("Invalid viewType")
		}
	}

	override fun onBindViewHolder(holder: BaseViewHolder<ProductOrderDetail>, position: Int) {
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
			items.add(0, ProductOrderDetail.title)
		}
		when(order?.order?.status){
			Order.ON_PROCESS, Order.NEED_PAY -> {
				items.add(ProductOrderDetail.grand)
			}
			Order.DONE -> {
				items.add(ProductOrderDetail.grand)
				items.add(ProductOrderDetail.payment)
				if (order?.payment?.payment!!.isCash){
					items.add(ProductOrderDetail.payReturn)
				}
			}
		}
	}

	inner class FirstColumnViewHolder(binding: RvFirstColumnItemOrderListBinding) : BaseViewHolder<ProductOrderDetail>(binding.root) {
		override fun bind(detail: ProductOrderDetail) {}
	}

	inner class ValueDetailColumnViewHolder(private val binding: RvDetailItemOrderListBinding) : BaseViewHolder<ProductOrderDetail>(binding.root) {
		override fun bind(detail: ProductOrderDetail) {
			if (detail.product != null){
				val no = "$adapterPosition."
				val amount = "${detail.amount}x"
				val total = detail.product!!.sellPrice * detail.amount

				binding.tvIoNo.text = no
				binding.tvIoAmount.text = amount
				binding.tvIoName.text = detail.product!!.name
				binding.tvIoPrice.text = toRupiah(detail.product!!.sellPrice)
				binding.tvIoTotal.text = toRupiah(total)
			}
		}
	}

	inner class ValueOrderColumnViewHolder(private val binding: RvItemOrderListBinding) : BaseViewHolder<ProductOrderDetail>(binding.root) {
		override fun bind(detail: ProductOrderDetail) {

			val no = "$adapterPosition."
			val amount = "${detail.amount}x"
			val variants = StringBuilder()
			for (variant in detail.variants){
				if (variants.isNotEmpty()){
					variants.append(", ")
				}
				variants.append(variant.name)
			}

			binding.tvIoNo.text = no
			binding.tvIoAmount.text = amount
			binding.tvIoName.text = detail.product?.name
			binding.tvIoVariant.text = variants
			binding.tvIoPrice.text = toRupiah(detail.product?.sellPrice)
			binding.tvIoTotal.text = toRupiah(getTotal(detail))

			setMix(detail)
			setBtnDelete(detail)
		}

		private fun getTotal(detail: ProductOrderDetail): Int{
			var total = 0
			if (detail.product != null){
				val product: Product = detail.product!!
				total = product.sellPrice * detail.amount
			}
			return total
		}

		private fun setMix(detail: ProductOrderDetail){
			if (detail.product != null){
				if (detail.product!!.isMix){
					binding.tvIoVariant.visibility = View.GONE
					if (detail.mixProducts.isNotEmpty()){
						for (item in detail.mixProducts){
							val txt = "${item.product.name} x${item.amount}"
							val tvProduct = TextView(binding.root.context)
							tvProduct.text = txt
							binding.contIoMixVariant.addView(tvProduct)

							for (variant in item.variants){
								val tvVariant = TextView(binding.root.context)
								tvVariant.text = variant.name
								binding.contIoMixVariant.addView(tvVariant)
							}
						}
					}
				}
			}
		}

		private fun setBtnDelete(detail: ProductOrderDetail){
			if (order?.order?.status == Order.NEED_PAY){
				binding.btnOlDelete.visibility = View.INVISIBLE
			}else{
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

	inner class TotalDetailColumnViewHolder(private val binding: RvTotalDetailItemOrderListBinding) : BaseViewHolder<ProductOrderDetail>(binding.root) {
		override fun bind(detail: ProductOrderDetail) {
			var title = ""
			var amount = ""
			when(detail.type){
				ProductOrderDetail.GRAND_TOTAL -> {
					title = "Total : "
					amount = toRupiah(grandTotal)
				}
				ProductOrderDetail.PAYMENT -> {
					title = "Bayar : "
					amount = if (order?.payment?.payment!!.isCash){
						toRupiah(order?.payment?.orderPayment?.pay)
					}else{
						order?.payment?.payment!!.name
					}
				}
				ProductOrderDetail.RETURN -> {
					title = "Kembalian : "
					amount = toRupiah(order?.payment?.orderPayment?.inReturn(order!!.grandTotal))
				}
			}
			binding.tvIoPrice.text = title
			binding.tvIoTotal.text = amount
		}

	}

	inner class TotalOrderColumnViewHolder(private val binding: RvTotalItemOrderListBinding) : BaseViewHolder<ProductOrderDetail>(binding.root) {
		override fun bind(detail: ProductOrderDetail) {
			binding.tvIoName.text = "Total : "
			binding.tvIoTotal.text = toRupiah(grandTotal)
		}
	}

	abstract class BaseViewHolder<ProductOrderDetail>(itemView: View) : RecyclerView.ViewHolder(itemView){
		abstract fun bind(detail: ProductOrderDetail)
	}
}
