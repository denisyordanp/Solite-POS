package com.socialite.solite_pos.view.main.menu.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.databinding.FragmentOnProcessBinding
import com.socialite.solite_pos.utils.config.DateUtils.Companion.currentDate
import com.socialite.solite_pos.view.main.menu.adapter.OrderListAdapter
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel.Companion.getOrderViewModel
import com.socialite.solite_pos.vo.Status

class OnProcessFragment : Fragment() {

	private lateinit var _binding: FragmentOnProcessBinding
	private lateinit var adapter: OrderListAdapter
	private lateinit var viewModel: OrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
		_binding = FragmentOnProcessBinding.inflate(inflater, container, false)
        return _binding.root
    }

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){

			viewModel = getOrderViewModel(activity!!)

			adapter = OrderListAdapter(activity!!, viewModel)
			_binding.rvOp.layoutManager = GridLayoutManager(activity, 4)
			_binding.rvOp.adapter = adapter

		}
	}

	override fun onStart() {
		super.onStart()
		getData()
	}

	private fun getData(){
		viewModel.getOrderList(Order.ON_PROCESS, currentDate).observe(activity!!){ response ->
			when(response.status){
				Status.LOADING -> {}
				Status.SUCCESS -> {
					adapter.items = ArrayList(response.data)
					adapter.cookCallback = { updateOrder(it) }
				}
				Status.ERROR -> {}
			}
		}
	}

	private fun updateOrder(order: Order){
		viewModel.updateOrder(order) {}
	}
}
