package com.socialite.solite_pos.view.main.menu.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.databinding.FragmentCancelBinding
import com.socialite.solite_pos.utils.config.DateUtils.Companion.currentDate
import com.socialite.solite_pos.view.main.menu.adapter.OrderListAdapter
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel.Companion.getOrderViewModel
import com.socialite.solite_pos.vo.Status

class CancelFragment : Fragment() {

    private lateinit var _binding: FragmentCancelBinding
    private lateinit var adapter: OrderListAdapter
    private lateinit var viewModel: OrderViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentCancelBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null){

            viewModel = getOrderViewModel(activity!!)

            adapter = OrderListAdapter(activity!!, viewModel)
            _binding.rvCl.adapter = adapter
            _binding.rvCl.layoutManager = GridLayoutManager(activity, 4)

        }
    }

    override fun onStart() {
        super.onStart()
        getData()
    }

    private fun getData(){
        viewModel.getOrderList(Order.CANCEL, currentDate).observe(activity!!){
            when(it.status){
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    adapter.items = ArrayList(it.data)
                }
                Status.ERROR -> {}
            }
        }
    }
}