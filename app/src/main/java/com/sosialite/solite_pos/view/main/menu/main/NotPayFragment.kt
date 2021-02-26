package com.sosialite.solite_pos.view.main.menu.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.sosialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import com.sosialite.solite_pos.databinding.FragmentNotPayBinding
import com.sosialite.solite_pos.utils.config.MainConfig
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.currentDate
import com.sosialite.solite_pos.view.main.menu.adapter.OrderListAdapter
import com.sosialite.solite_pos.view.viewmodel.MainViewModel
import com.sosialite.solite_pos.vo.Status

class NotPayFragment : Fragment() {

	private lateinit var _binding: FragmentNotPayBinding
	private lateinit var adapter: OrderListAdapter
	private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
		_binding = FragmentNotPayBinding.inflate(inflater, container, false)
        return _binding.root
    }

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null && context != null){
			adapter = OrderListAdapter(context!!, activity!!.supportFragmentManager)
			viewModel = MainConfig.getViewModel(activity!!)

			getData()

			_binding.rvNp.layoutManager = GridLayoutManager(activity, 4)
			_binding.rvNp.adapter = adapter
		}
	}

	private fun getData(){
		viewModel.getOrderDetail(Order.NEED_PAY, currentDate).observe(activity!!){ response ->
			when(response.status){
				Status.LOADING -> {}
				Status.SUCCESS -> {
					adapter.items = ArrayList(response.data)
				}
				Status.ERROR -> {}
			}
		}
	}
}
