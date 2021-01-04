package com.sosialite.solite_pos.view.main.menu.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.data.source.local.entity.helper.Order
import com.sosialite.solite_pos.databinding.RvOrderListBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.orderIndex
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.thousand
import com.sosialite.solite_pos.utils.tools.DoneCook
import com.sosialite.solite_pos.utils.tools.MessageBottom
import com.sosialite.solite_pos.view.main.menu.bottom.DetailOrderFragment
import java.util.*
import kotlin.collections.ArrayList

class OrderListAdapter(
		private var context: Context,
		private var fragmentManager: FragmentManager
		) : RecyclerView.Adapter<OrderListAdapter.ListViewHolder>() {
	private val items: ArrayList<Order> = ArrayList()

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

	fun setItems(items: ArrayList<Order>){
		if (this.items.isNotEmpty()){
			this.items.clear()
		}
		this.items.addAll(items)
		notifyDataSetChanged()
	}

	fun addItem(item: Order){
		val pos = orderIndex(items, item)
		if (pos != null){
			items[pos] = item
			notifyItemChanged(pos)
		}else{
			items.add(0, item)
			notifyItemInserted(0)
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val order = items[position]

		val totalItem = "Banyaknya ${order.totalItem} barang"
		val totalPay = "Rp. ${thousand(order.totalPay)}"

		setImage(holder.binding.ivOrLogo, order.status)
		holder.binding.tvOrName.text = order.customer?.name
		holder.binding.tvOrTotalItem.text = totalItem
		holder.binding.tvOrTotalPay.text = totalPay
		setCookTime(holder.binding.tvOrTime, order, position)

		holder.itemView.setOnClickListener {
			DetailOrderFragment(order).show(fragmentManager, "detail-order")
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(val binding: RvOrderListBinding) : RecyclerView.ViewHolder(binding.root)

	private fun setImage(v: ImageView, status: Int){
		when(status){
			Order.ON_PROCESS -> v.setImageDrawable(cook)
			Order.NEED_PAY -> v.setImageDrawable(pay)
			Order.DONE -> v.setImageDrawable(done)
		}
	}

	private fun setCookTime(v: TextView, order: Order, pos: Int){
		if (order.status == Order.ON_PROCESS){
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
		items[pos].cookTime = Calendar.getInstance()
		DoneCook(context).set(items[pos])
		notifyItemChanged(pos)
	}
}
