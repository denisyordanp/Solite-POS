package com.sosialite.solite_pos.view.main.menu.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import com.sosialite.solite_pos.databinding.RvOrderListBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.currentDate
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.orderIndex
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.thousand
import com.sosialite.solite_pos.utils.tools.DoneCook
import com.sosialite.solite_pos.utils.tools.MessageBottom
import com.sosialite.solite_pos.view.main.menu.bottom.DetailOrderFragment

class OrderListAdapter(
		private var context: Context,
		private var fragmentManager: FragmentManager
		) : RecyclerView.Adapter<OrderListAdapter.ListViewHolder>() {

	var cookCallback: ((Order) -> Unit)? = null

	var items: ArrayList<OrderWithProduct> = ArrayList()
		set(value) {
			if (field.isNotEmpty()){
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}

	private var warning: Drawable? = null
	private var pay: Drawable? = null
	private var done: Drawable? = null
	private var cook: Drawable? = null

	init {
		val r = context.resources
		warning = ResourcesCompat.getDrawable(r, R.drawable.ic_warning, null)

		pay = ResourcesCompat.getDrawable(r, R.drawable.ic_payments_50dp, null)
		done = ResourcesCompat.getDrawable(r, R.drawable.ic_done_all_50dp, null)
		cook = ResourcesCompat.getDrawable(r, R.drawable.ic_cooking_50dp, null)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val order = items[position]
		val totalItem = "Banyaknya ${order.totalItem} barang"
		val totalPay = "Rp. ${thousand(order.grandTotal)}"

		setImage(holder.binding.ivOrLogo, order.order.status)
		holder.binding.tvOrName.text = order.customer.name
		holder.binding.tvOrTotalItem.text = totalItem
		holder.binding.tvOrTotalPay.text = totalPay
		setCookTime(holder.binding.tvOrTime, order.order, position)

		holder.itemView.setOnClickListener {
			DetailOrderFragment(order).show(fragmentManager, "detail-order")
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(val binding: RvOrderListBinding) : RecyclerView.ViewHolder(binding.root)

	private fun setImage(v: ImageView, status: Int?){
		if (status != null){
			when(status){
				Order.ON_PROCESS -> v.setImageDrawable(cook)
				Order.NEED_PAY -> v.setImageDrawable(pay)
				Order.DONE -> v.setImageDrawable(done)
				Order.CANCEL -> v.setImageDrawable(done)

			}
		}
	}

	private fun setCookTime(v: TextView, order: Order?, pos: Int){
		if (order?.status == Order.ON_PROCESS){
			v.visibility = View.VISIBLE
			val text: String
			if(order.finishToString(context).isNullOrEmpty()){
				v.setBackgroundColor(Color.RED)
				v.setCompoundDrawablesWithIntrinsicBounds(warning, null,null,null)
				v.setOnClickListener {
					MessageBottom(fragmentManager)
							.setMessage("Anda yakin akan set waktu masak sekarang?")
							.setPositiveListener("Ya"){
								updateTime(pos)
							}
							.setNegativeListener("Batal"){
								it?.dismiss()
							}.show()
				}
				text = "Atur"
			}else{
				text = order.finishToString(context)!!
			}
			v.text = text
		}else{
			v.visibility = View.GONE
		}
	}

	private fun updateTime(pos: Int){
		val order = items[pos]
		order.order.cookTime = currentDate
		DoneCook(context).set(order)
		cookCallback?.invoke(order.order)
		notifyItemChanged(pos)
	}
}
