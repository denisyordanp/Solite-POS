package com.socialite.solite_pos.adapters.recycleView.order

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.R
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.databinding.RvOrderListBinding
import com.socialite.solite_pos.utils.config.DateUtils.Companion.currentDateTime
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.thousand
import com.socialite.solite_pos.utils.tools.DoneCookService
import com.socialite.solite_pos.utils.tools.MessageBottom
import com.socialite.solite_pos.utils.tools.RecycleViewDiffUtils
import com.socialite.solite_pos.view.main.menu.bottom.DetailOrderFragment
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OrderListAdapter(
	private var activity: FragmentActivity
) : RecyclerView.Adapter<OrderListAdapter.ListViewHolder>() {

	private val orderViewModel = OrderViewModel.getOrderViewModel(activity)

	var cookCallback: ((Order) -> Unit)? = null

	private var orders: ArrayList<OrderData> = ArrayList()
	fun setOrders(orders: List<OrderData>) {
		val orderDiffUtil = RecycleViewDiffUtils(this.orders, orders)
		val diffUtilResult = DiffUtil.calculateDiff(orderDiffUtil)
		this.orders = ArrayList(orders)
		diffUtilResult.dispatchUpdatesTo(this)
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
		val inflater = LayoutInflater.from(parent.context)
		val binding = RvOrderListBinding.inflate(inflater, parent, false)
		return ListViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		holder.setDataToView(orders[position])
	}

	override fun getItemCount(): Int {
		return orders.size
	}

	inner class ListViewHolder(val binding: RvOrderListBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun setDataToView(order: OrderData) {
			setCustomerName(order.customer.name)
			getItem(order)
			setImage(order.order.status)
			setCookTime(order.order)
		}

		private fun setCustomerName(name: String) {
			binding.tvOrName.text = name
		}

		private fun getItem(order: OrderData) {
			val orderWithProduct = OrderWithProduct(order)
			activity.lifecycleScope.launch {
				orderViewModel.getProductOrder(order.order.orderNo)
					.collect {
						if (it.isNotEmpty()) {
							orderWithProduct.products = it
						}
						setItems(orderWithProduct)
					}
			}
		}

		private fun setItems(order: OrderWithProduct) {
			val totalItem = "Banyaknya ${order.totalItem} barang"
			val totalPay = "Rp. ${thousand(order.grandTotal)}"
			binding.tvOrTotalItem.text = totalItem
			binding.tvOrTotalPay.text = totalPay
			itemView.setOnClickListener {
				DetailOrderFragment(order).show(activity.supportFragmentManager, "detail-order")
			}
		}

		private fun setImage(status: Int?) {
			if (status != null) {
				when (status) {
					Order.ON_PROCESS -> binding.ivOrLogo.setImageDrawable(cook)
					Order.NEED_PAY -> binding.ivOrLogo.setImageDrawable(pay)
					Order.DONE -> binding.ivOrLogo.setImageDrawable(done)
					Order.CANCEL -> binding.ivOrLogo.setImageDrawable(cancel)
				}
			}
		}

		private fun setCookTime(order: Order?) {
			if (order?.status == Order.ON_PROCESS) {
				binding.tvOrTime.visibility = View.VISIBLE
				val text: String
				val finish = order.finishToString(activity)
				if (finish.isNullOrEmpty()) {
					binding.tvOrTime.setBackgroundColor(Color.RED)
					binding.tvOrTime.setCompoundDrawablesWithIntrinsicBounds(
						warning,
						null,
						null,
						null
					)
					binding.tvOrTime.setOnClickListener {
						MessageBottom(activity.supportFragmentManager)
							.setMessageImage(
								ResourcesCompat.getDrawable(
									activity.resources,
									R.drawable.ic_order_done,
									null
								)
							)
							.setMessage("Anda yakin akan set waktu masak sekarang?")
							.setPositiveListener("Ya") {
								updateTime(adapterPosition)
							}
							.setNegativeListener("Batal"){
								it?.dismiss()
							}.show()
					}
					text = "Atur"
				}else{
					text = finish
				}
				binding.tvOrTime.text = text
			}else{
				binding.tvOrTime.visibility = View.GONE
			}
		}

		private fun updateTime(pos: Int) {
			val order = orders[pos]
			order.order.cookTime = currentDateTime
			DoneCookService(activity).set(order)
			cookCallback?.invoke(order.order)
			notifyItemChanged(pos)
		}
	}
}
