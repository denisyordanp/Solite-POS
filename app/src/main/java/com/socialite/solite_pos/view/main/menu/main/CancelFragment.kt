package com.socialite.solite_pos.view.main.menu.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.databinding.FragmentCancelBinding
import com.socialite.solite_pos.utils.config.DateUtils.Companion.currentDate
import com.socialite.solite_pos.adapters.recycleView.order.OrderListAdapter
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel.Companion.getOrderViewModel

class CancelFragment(private var queryDate: String) : Fragment() {

    private lateinit var _binding: FragmentCancelBinding
    private lateinit var adapter: OrderListAdapter
    private lateinit var viewModel: OrderViewModel

    constructor() : this(currentDate)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCancelBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {

            viewModel = getOrderViewModel(activity!!)

            setDate(queryDate)
            setUpAdapter()
        }
    }

    private fun setUpAdapter() {
        adapter = OrderListAdapter(activity!!)
        _binding.rvCl.adapter = adapter
        _binding.rvCl.layoutManager = GridLayoutManager(activity, 4)
    }

    fun setDate(newDate: String) {
        queryDate = newDate
        getData()
    }

    private fun getData() {
        viewModel.getLocalOrders(Order.CANCEL, queryDate).observe(activity!!) { orders ->
            setOrders(ArrayList(orders))
        }
    }

    private fun setOrders(orders: ArrayList<OrderData>) {
        showEmpty(orders.isNullOrEmpty())

        adapter.setOrders(orders)
    }

    private fun showEmpty(state: Boolean) {
        if (state) {
            _binding.tvClEmpty.visibility = View.VISIBLE
        } else {
            _binding.tvClEmpty.visibility = View.INVISIBLE
        }
    }
}