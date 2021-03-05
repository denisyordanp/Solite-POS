package com.socialite.solite_pos.view.main.menu.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.R
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.databinding.RvOrderListBinding
import com.socialite.solite_pos.utils.config.DateUtils.Companion.currentDate
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.thousand
import com.socialite.solite_pos.utils.tools.DoneCook
import com.socialite.solite_pos.utils.tools.MessageBottom
import com.socialite.solite_pos.view.main.menu.bottom.DetailOrderFragment
import com.socialite.solite_pos.view.viewmodel.MainViewModel
import com.socialite.solite_pos.vo.Status

class OrderListAdapter(
		private var activity: FragmentActivity,
		private var viewModel: MainViewModel
		) : RecyclerView.Adapter<OrderListAdapter.ListViewHolder>() {

	var cookCallback: ((Order) -> Unit)? = null

	var items: ArrayList<OrderData> = ArrayList()
		set(value) {
			if (field.isNotEmpty()) {
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}

	private var warning: Drawable? = null
	private var cancel: Drawable? = null
	private var done: Drawable? = null
	private var cook: Drawable? = null
	private var pay: Drawable? = null

	init {
		val r = activity.resources
		warning = ResourcesCompat.getDrawable(r, R.drawable.ic_warning, null)

		pay = ResourcesCompat.getDrawable(r, R.drawable.ic_payments_50dp, null)
		done = ResourcesCompat.getDrawable(r, R.drawable.ic_done_all_50dp, null)
		cook = ResourcesCompat.getDrawable(r, R.drawable.ic_cooking_50dp, null)
		cancel = ResourcesCompat.getDrawable(r, R.drawable.ic_cross_50dp, null)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val order = items[position]

		holder.getItem(order)
		holder.setImage(order.order.status)
		holder.binding.tvOrName.text = order.customer.name
		holder.setCookTime(order.order, position)

	}

	override fun getItemCount(): Int {
		return items.size
	}

	inner class ListViewHolder(val binding: RvOrderListBinding) : RecyclerView.ViewHolder(binding.root){

		fun getItem(order: OrderData){
			val orderWithProduct = OrderWithProduct(order)
			viewModel.getProductOrder(order.order.orderNo).observe(activity){
				when(it.status){
					Status.LOADING -> {}
					Status.SUCCESS -> {
						Log.w("TESTINGDATA", "product ${it.data}")
						if (!it.data.isNullOrEmpty()){
							orderWithProduct.products = it.data
						}
						setItems(orderWithProduct)
					}
					Status.ERROR -> {
						Log.w("TESTINGDATA", "product error")
					}
				}
			}
		}

		private fun setItems(order: OrderWithProduct){
			val totalItem = "Banyaknya ${order.totalItem} barang"
			val totalPay = "Rp. ${thousand(order.grandTotal)}"
			binding.tvOrTotalItem.text = totalItem
			binding.tvOrTotalPay.text = totalPay
			itemView.setOnClickListener {
				DetailOrderFragment(order).show(activity.supportFragmentManager, "detail-order")
			}
		}

		fun setImage(status: Int?){
			if (status != null){
				when (status) {
					Order.ON_PROCESS -> binding.ivOrLogo.setImageDrawable(cook)
					Order.NEED_PAY -> binding.ivOrLogo.setImageDrawable(pay)
					Order.DONE -> binding.ivOrLogo.setImageDrawable(done)
					Order.CANCEL -> binding.ivOrLogo.setImageDrawable(cancel)
				}
			}
		}

		fun setCookTime(order: Order?, pos: Int){
			if (order?.status == Order.ON_PROCESS){
				binding.tvOrTime.visibility = View.VISIBLE
				val text: String
				if(order.finishToString(activity).isNullOrEmpty()){
					binding.tvOrTime.setBackgroundColor(Color.RED)
					binding.tvOrTime.setCompoundDrawablesWithIntrinsicBounds(warning, null,null,null)
					binding.tvOrTime.setOnClickListener {
						MessageBottom(activity.supportFragmentManager)
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
					text = order.finishToString(activity)!!
				}
				binding.tvOrTime.text = text
			}else{
				binding.tvOrTime.visibility = View.GONE
			}
		}

		private fun updateTime(pos: Int){
			val order = items[pos]
			order.order.cookTime = currentDate
			DoneCook(activity).set(order)
			cookCallback?.invoke(order.order)
			notifyItemChanged(pos)
		}
	}
}
