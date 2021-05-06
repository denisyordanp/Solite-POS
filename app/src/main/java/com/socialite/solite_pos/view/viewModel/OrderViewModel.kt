package com.socialite.solite_pos.view.viewModel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.socialite.solite_pos.data.source.local.entity.helper.*
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.master.*
import com.socialite.solite_pos.data.source.repository.SoliteRepository
import com.socialite.solite_pos.vo.Resource

class OrderViewModel(private val repository: SoliteRepository) : ViewModel() {

	companion object : ViewModelFromFactory<OrderViewModel>(){
		fun getOrderViewModel(activity: FragmentActivity): OrderViewModel{
			return buildViewModel(activity, OrderViewModel::class.java)
		}
	}

	fun getOrderList(status: Int, date: String): LiveData<Resource<List<OrderData>>> {
		return repository.getOrderList(status, date)
	}

	fun getLocalOrders(status: Int, date: String): LiveData<List<OrderData>> {
		return repository.getLocalOrders(status, date)
	}

	fun getProductOrder(orderNo: String): LiveData<Resource<List<ProductOrderDetail>>>{
		return repository.getProductOrder(orderNo)
	}

	fun insertPaymentOrder(payment: OrderPayment): OrderPayment {
		return repository.insertPaymentOrder(payment)
	}

	fun newOrder(order: OrderWithProduct) {
		repository.newOrder(order)
	}

	fun updateOrder(order: Order) {
		repository.updateOrder(order)
	}

	fun doneOrder(order: OrderWithProduct) {
		repository.doneOrder(order)
	}

	fun replaceProductOrder(old: OrderWithProduct, new: OrderWithProduct) {
		repository.replaceProductOrder(old, new)
	}
}
