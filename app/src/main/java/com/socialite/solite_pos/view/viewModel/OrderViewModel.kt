package com.socialite.solite_pos.view.viewModel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.socialite.solite_pos.data.source.local.entity.helper.*
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.master.*
import com.socialite.solite_pos.data.source.remote.response.helper.ApiResponse
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

	fun getProductOrder(orderNo: String): LiveData<Resource<List<ProductOrderDetail>>>{
		return repository.getProductOrder(orderNo)
	}

	fun insertPaymentOrder(payment: OrderPayment, callback: (ApiResponse<LiveData<OrderData>>) -> Unit) {
		return repository.insertPaymentOrder(payment, callback)
	}

	fun newOrder(order: OrderWithProduct, callback: (ApiResponse<Boolean>) -> Unit){
		repository.newOrder(order, callback)
	}

	fun updateOrder(order: Order, callback: (ApiResponse<Boolean>) -> Unit) {
		repository.updateOrder(order, callback)
	}

	fun cancelOrder(order: OrderWithProduct, callback: (ApiResponse<Boolean>) -> Unit) {
		repository.cancelOrder(order, callback)
	}
}
