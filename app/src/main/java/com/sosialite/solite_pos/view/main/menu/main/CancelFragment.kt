package com.sosialite.solite_pos.view.main.menu.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import com.sosialite.solite_pos.databinding.FragmentCancelBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.currentDate
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.view.main.menu.adapter.OrderListAdapter
import com.sosialite.solite_pos.view.viewmodel.MainViewModel
import com.sosialite.solite_pos.vo.Status

class CancelFragment : Fragment() {

    private lateinit var _binding: FragmentCancelBinding
    private lateinit var adapter: OrderListAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentCancelBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null){

            viewModel = getViewModel(activity!!)

            adapter = OrderListAdapter(activity!!, childFragmentManager)
            _binding.rvCl.adapter = adapter
            _binding.rvCl.layoutManager = GridLayoutManager(activity, 4)

            getData()
        }
    }

    private fun getData(){
        viewModel.getOrderDetail(Order.CANCEL, currentDate).observe(activity!!){
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