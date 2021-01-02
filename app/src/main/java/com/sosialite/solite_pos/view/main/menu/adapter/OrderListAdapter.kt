package com.sosialite.solite_pos.view.main.menu.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.data.source.local.entity.helper.Order
import com.sosialite.solite_pos.databinding.RvOrderListBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.thousand
import com.sosialite.solite_pos.view.main.MessageFragment
import com.sosialite.solite_pos.view.main.menu.bottom.DetailOrderFragment

class OrderListAdapter(private var fragmentManager: FragmentManager) : RecyclerView.Adapter<OrderListAdapter.ListViewHolder>() {
	private val items: ArrayList<Order> = ArrayList()

	fun setItems(items: ArrayList<Order>){
		if (this.items.isNotEmpty()){
			this.items.clear()
		}
		this.items.addAll(items)
		notifyDataSetChanged()
	}

	fun addItem(item: Order){
		items.add(0, item)
		notifyItemInserted(0)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val order = items[position]

		val totalItem = "Banyaknya ${order.totalItem} barang"
		val totalPay = "Rp. ${thousand(order.totalPay)}"

		holder.setImage(order.status)
		holder.binding.tvOrName.text = order.customer?.name
		holder.binding.tvOrTotalItem.text = totalItem
		holder.binding.tvOrTotalPay.text = totalPay
		holder.setCookTime(order, fragmentManager)

		holder.itemView.setOnClickListener {
			DetailOrderFragment(order).show(fragmentManager, "detail-order")
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(val binding: RvOrderListBinding) : RecyclerView.ViewHolder(binding.root){

		private val resource = binding.root.resources
		private var warning: Drawable? = ResourcesCompat.getDrawable(resource, R.drawable.ic_warning, null)
		private var pay: Drawable? = ResourcesCompat.getDrawable(resource, R.drawable.ic_payments_50dp, null)
		private var done: Drawable? = ResourcesCompat.getDrawable(resource, R.drawable.ic_done_all_50dp, null)
		private var cook: Drawable? = ResourcesCompat.getDrawable(resource, R.drawable.ic_cooking_50dp, null)

		fun setImage(status: Int){
			when(status){
				Order.ON_PROCESS -> binding.ivOrLogo.setImageDrawable(cook)
				Order.NEED_PAY -> binding.ivOrLogo.setImageDrawable(pay)
				Order.DONE -> binding.ivOrLogo.setImageDrawable(done)
			}
		}

		fun setCookTime(order: Order, fragmentManager: FragmentManager){
			if (order.status == Order.ON_PROCESS){
				var text = ""
				if(order.strFinishCook.isNullOrEmpty()){
					binding.tvOrTime.setBackgroundColor(Color.RED)
					binding.tvOrTime.setCompoundDrawablesWithIntrinsicBounds(warning, null,null,null)
					binding.tvOrTime.setOnClickListener {
						MessageFragment.create()
								.setMessage("Anda yakin akan set waktu sekarang?")
								.setOnPositiveButton("Atur sekarang"){

								}
								.setOnNegativeButton("Batal"){
									it?.dismiss()
								}.show(fragmentManager, "Message")
					}
					text = "Atur"
				}else{
					text = order.strFinishCook!!
				}
				binding.tvOrTime.text = text
			}else{
				binding.tvOrTime.visibility = View.GONE
			}
		}
	}
}
