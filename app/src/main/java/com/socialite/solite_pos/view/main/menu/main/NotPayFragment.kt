package com.socialite.solite_pos.view.main.menu.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.databinding.FragmentNotPayBinding
import com.socialite.solite_pos.utils.config.DateUtils.Companion.currentDate
import com.socialite.solite_pos.view.main.menu.adapter.OrderListAdapter
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel.Companion.getOrderViewModel

class NotPayFragment(private var queryDate: String) : Fragment() {

    private lateinit var _binding: FragmentNotPayBinding
    private lateinit var adapter: OrderListAdapter
    private lateinit var viewModel: OrderViewModel

    constructor() : this(currentDate)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNotPayBinding.inflate(inflater, container, false)
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
        adapter = OrderListAdapter(activity!!, viewModel)
        _binding.rvNp.layoutManager = GridLayoutManager(activity, 4)
        _binding.rvNp.adapter = adapter
    }

    fun setDate(newDate: String) {
        queryDate = newDate
        getData()
    }

    private fun getData() {
        viewModel.getLocalOrders(Order.NEED_PAY, queryDate).observe(activity!!) { orders ->
            setOrders(ArrayList(orders))
        }
    }

    private fun setOrders(orders: ArrayList<OrderData>) {
        showEmpty(orders.isNullOrEmpty())

        adapter.items = orders
    }

    private fun showEmpty(state: Boolean) {
        if (state) {
            _binding.tvNpEmpty.visibility = View.VISIBLE
        } else {
            _binding.tvNpEmpty.visibility = View.INVISIBLE
        }
    }
}
