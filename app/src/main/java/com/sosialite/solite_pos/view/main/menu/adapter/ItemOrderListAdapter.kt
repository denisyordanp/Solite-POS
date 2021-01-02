package com.sosialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.data.source.local.entity.helper.DetailOrder
import com.sosialite.solite_pos.data.source.local.entity.helper.Order
import com.sosialite.solite_pos.databinding.*
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.currentTime
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.productIndex
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.toRupiah

class ItemOrderListAdapter(private val type: Int) : RecyclerView.Adapter<ItemOrderListAdapter.BaseViewHolder<DetailOrder>>() {

	private val items: ArrayList<DetailOrder> = ArrayList()
	private var status: Int? = 0

	var order: Order? = null
	val sortedOrder: Order?
	get() {
		val order = order
		order?.items?.removeAt(order.items.lastIndex)
		return order
	}

	companion object{
		private const val FIRST_COLUMN = 0
		private const val VALUE_COLUMN = 1
		private const val TOTAL_COLUMN = 2
		const val DETAIL = 0
		const val ORDER = 1
	}

	fun setItems(order: Order?){
		if (order != null){
			this.order = order
			if (!order.items.isNullOrEmpty()){
				status = order.status
				if (items.isNotEmpty()){
					items.clear()
				}
				items.addAll(order.items)
				setData()
				notifyDataSetChanged()
			}
		}
	}

	fun addItem(detail: DetailOrder){
		if (order != null){
			val pos = productIndex(items, detail.product)
			if (pos != null){
				items[pos] = detail
				notifyItemChanged(pos)
			}else{
				items.add(0, detail)
				notifyItemInserted(0)
			}
			order!!.items = items
			notifyItemChanged(items.size-1)
		}
	}

	fun delItem(detail: DetailOrder){
		if (order != null){
			if (detail.amount > 0){
				val pos = productIndex(items, detail.product)
				if (pos != null){
					items[pos] = detail
					order!!.items = items
					notifyItemChanged(pos)
					notifyItemChanged(items.size-1)
				}
			}else{
				if (items.size == 2){
					items.clear()
					order = null
					notifyDataSetChanged()
				}else{
					val pos = productIndex(items, detail.product)
					if (pos != null){
						items.remove(detail)
						order!!.items = items
						notifyItemRemoved(pos)
						notifyItemChanged(items.size-1)
					}
				}
			}

		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DetailOrder> {
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
					DETAIL -> TotalColumnViewHolder(RvTotalDetailItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
					ORDER -> TotalOrderColumnViewHolder(RvTotalItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
					else -> throw IllegalArgumentException("Invalid adapterType")
				}
			}
			else -> throw IllegalArgumentException("Invalid viewType")
		}
	}

	override fun onBindViewHolder(holder: BaseViewHolder<DetailOrder>, position: Int) {
		holder.bind(items[position], position)
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
			items.add(0, DetailOrder())
		}
		when(status){
			Order.ON_PROCESS, Order.NEED_PAY -> {
				items.add(DetailOrder())
			}
			Order.DONE -> {
				items.add(DetailOrder())
				items.add(DetailOrder())
				items.add(DetailOrder())
			}
		}
	}

	inner class FirstColumnViewHolder(binding: RvFirstColumnItemOrderListBinding) : BaseViewHolder<DetailOrder>(binding.root) {
		override fun bind(detail: DetailOrder, position: Int) {}
	}

	inner class ValueDetailColumnViewHolder(private val binding: RvDetailItemOrderListBinding) : BaseViewHolder<DetailOrder>(binding.root) {
		override fun bind(detail: DetailOrder, position: Int) {

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

	inner class ValueOrderColumnViewHolder(private val binding: RvItemOrderListBinding) : BaseViewHolder<DetailOrder>(binding.root) {
		override fun bind(detail: DetailOrder, position: Int) {

			val no = "$adapterPosition."
			val amount = "${detail.amount}x"
			var total = 0
			if (detail.product != null){
				total = detail.product!!.price * detail.amount
			}

			binding.tvIoNo.text = no
			binding.tvIoAmount.text = amount
			binding.tvIoName.text = detail.product?.name
			binding.tvIoPrice.text = toRupiah(detail.product?.price)
			binding.tvIoTotal.text = toRupiah(total)
		}
	}

	inner class TotalColumnViewHolder(private val binding: RvTotalDetailItemOrderListBinding) : BaseViewHolder<DetailOrder>(binding.root) {
		override fun bind(detail: DetailOrder, position: Int) {
			var title = ""
			var amount = ""
			if (status == Order.DONE){
				when(position){
					itemCount-3 -> {
						title = "Total : "
						amount = toRupiah(order?.totalPay)
					}
					itemCount-2 -> {
						title = "Bayar : "
						amount = toRupiah(order?.pay)
					}
					itemCount-1 -> {
						title = "Kembalian : "
						amount = toRupiah(order?.payReturn)
					}
				}
			}else{
				title = "Total : "
				amount = toRupiah(order?.totalPay)
			}
			binding.tvIoPrice.text = title
			binding.tvIoTotal.text = amount
		}
	}

	inner class TotalOrderColumnViewHolder(private val binding: RvTotalItemOrderListBinding) : BaseViewHolder<DetailOrder>(binding.root) {
		override fun bind(detail: DetailOrder, position: Int) {
			binding.tvIoName.text = "Total : "
			binding.tvIoTotal.text = toRupiah(order?.totalPay)
		}
	}

	abstract class BaseViewHolder<DetailOrder>(itemView: View) : RecyclerView.ViewHolder(itemView){
		abstract fun bind(detail: DetailOrder, position: Int)
	}
}
