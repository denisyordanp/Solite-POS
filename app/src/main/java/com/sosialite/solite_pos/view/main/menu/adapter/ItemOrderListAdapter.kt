package com.sosialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import com.sosialite.solite_pos.databinding.*
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.productIndex
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.toRupiah

class ItemOrderListAdapter(private val type: Int) : RecyclerView.Adapter<ItemOrderListAdapter.BaseViewHolder<ProductOrderDetail>>() {

	private var status: Int? = 0

	var order: Order? = null
	val sortedOrder: Order?
	get() {
		val newOrder = order
//		order?.items?.removeAt(order.items.lastIndex)
		return order
	}

	var items: ArrayList<ProductOrderDetail> = ArrayList()
		set(value) {
//			order = value[0].order
			status = order?.status
			if (field.isNotEmpty()){
				field.clear()
			}
			field.addAll(value)
			if (type == DETAIL){
				setData()
			}
			notifyDataSetChanged()
		}
	private val grandTotal: Int
	get() {
		var total = 0
		for (item in items){
			if (item.product != null){
				total += item.product!!.price * item.amount
			}
		}
		return total
	}

	companion object{
		private const val FIRST_COLUMN = 0
		private const val VALUE_COLUMN = 1
		private const val TOTAL_COLUMN = 2
		const val DETAIL = 0
		const val ORDER = 1
	}

	fun addItem(
			detail: ProductOrderDetail,
			btnCallback: (Boolean) -> Unit
	){
		if (order != null){
			if (items.isEmpty()){
				if (detail.amount != 0){
					setData()
					add(detail, btnCallback)
				}
			}else{
				add(detail, btnCallback)
			}
		}
	}

	private fun add(
			detail: ProductOrderDetail,
			btnCallback: (Boolean) -> Unit
	){
		val pos = productIndex(items, detail)
		if (pos != null){
			if (detail.amount == 0){
				delItem(pos)
				btnCallback.invoke(false)
			}else{
				items[pos] = detail
				notifyItemChanged(pos)
				btnCallback.invoke(true)
			}
		}else{
			if (detail.amount != 0){
				items.add(0, detail)
				notifyItemInserted(0)
				btnCallback.invoke(true)
			}else{
				btnCallback.invoke(false)
			}
		}
		notifyItemChanged(items.size-1)
	}

	private fun delItem(pos: Int){
		if (order != null){
			if (items.size == 2){
				items.clear()
				notifyDataSetChanged()
			}else{
				items.removeAt(pos)
//				order!!.items = items
				notifyItemRemoved(pos)
				notifyItemChanged(items.size-1)
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ProductOrderDetail> {
		return when (viewType){
			FIRST_COLUMN -> {
				FirstColumnViewHolder(RvFirstColumnItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
			}
			VALUE_COLUMN -> {
				when(type){
					DETAIL -> ValueDetailColumnViewHolder(RvDetailItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
					ORDER -> ValueOrderColumnViewHolder(RvItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
					else -> throw IllegalArgumentException("Invalid adapterType")
				}
			}
			TOTAL_COLUMN -> {
				when(type){
					DETAIL -> TotalDetailColumnViewHolder(RvTotalDetailItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
					ORDER -> TotalOrderColumnViewHolder(RvTotalItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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
		return if (position == 0){
			when(type){
				DETAIL -> FIRST_COLUMN
				ORDER -> VALUE_COLUMN
				else -> throw IllegalArgumentException("Invalid adapterType")
			}
		}else if (status == Order.ON_PROCESS || status == Order.NEED_PAY){
			if (position == items.size - 1){
				TOTAL_COLUMN
			}else{
				VALUE_COLUMN
			}
		}else {
			if (position > items.size - 4){
				TOTAL_COLUMN
			}else{
				VALUE_COLUMN
			}
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	private fun setData(){
		if (type == DETAIL){
			items.add(0, ProductOrderDetail())
		}
		when(status){
			Order.ON_PROCESS, Order.NEED_PAY -> {
				items.add(ProductOrderDetail())
			}
			Order.DONE -> {
				items.add(ProductOrderDetail())
				items.add(ProductOrderDetail())
				items.add(ProductOrderDetail())
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
				val total = detail.product!!.price * detail.amount

				binding.tvIoNo.text = no
				binding.tvIoAmount.text = amount
				binding.tvIoName.text = detail.product?.name
				binding.tvIoPrice.text = toRupiah(detail.product?.price)
				binding.tvIoTotal.text = toRupiah(total)
			}
		}
	}

	inner class ValueOrderColumnViewHolder(private val binding: RvItemOrderListBinding) : BaseViewHolder<ProductOrderDetail>(binding.root) {
		override fun bind(detail: ProductOrderDetail) {

			val no = "$adapterPosition."
			val amount = "${detail.amount}x"
			var total = 0
			if (detail.product != null){
				total = detail.product?.price ?: 0 * detail.amount
			}

			binding.tvIoNo.text = no
			binding.tvIoAmount.text = amount
			binding.tvIoName.text = detail.product?.name
			binding.tvIoPrice.text = toRupiah(detail.product?.price)
			binding.tvIoTotal.text = toRupiah(total)

			if (order?.status == Order.NEED_PAY){
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
				}
			}
		}
	}

	inner class TotalDetailColumnViewHolder(private val binding: RvTotalDetailItemOrderListBinding) : BaseViewHolder<ProductOrderDetail>(binding.root) {
		override fun bind(detail: ProductOrderDetail) {
			var title = ""
			var amount = ""
			if (status == Order.DONE){
				when(adapterPosition){
					itemCount-3 -> {
						title = "Total : "
						amount = toRupiah(grandTotal)
					}
					itemCount-2 -> {
						title = "Bayar : "
//						amount = toRupiah(order?.pay)
						amount = toRupiah(100000)
					}
					itemCount-1 -> {
						title = "Kembalian : "
						amount = toRupiah(100000 - grandTotal)
					}
				}
			}else{
				title = "Total : "
				amount = toRupiah(grandTotal)
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
