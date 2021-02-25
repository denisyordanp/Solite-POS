package com.sosialite.solite_pos.view.main.menu.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.sosialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import com.sosialite.solite_pos.databinding.FragmentDoneBinding
import com.sosialite.solite_pos.utils.config.MainConfig
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.currentDate
import com.sosialite.solite_pos.view.main.menu.adapter.OrderListAdapter
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class DoneFragment : Fragment() {

	private lateinit var _binding: FragmentDoneBinding
	private lateinit var adapter: OrderListAdapter
	private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
		_binding = FragmentDoneBinding.inflate(inflater, container, false)
        return _binding.root
    }

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null && context != null){
			adapter = OrderListAdapter(context!!, activity!!.supportFragmentManager)
			viewModel = MainConfig.getViewModel(activity!!)

			_binding.rvDn.layoutManager = GridLayoutManager(activity, 4)
			_binding.rvDn.adapter = adapter

			getData()
		}
	}

	private fun getData(){
		adapter.items = ArrayList(viewModel.getOrderDetail(Order.DONE, currentDate))
		adapter.cookCallback = { updateOrder(it) }
	}

	private fun updateOrder(order: Order){
		viewModel.updateOrder(order) {}
	}

	fun addItem(order: OrderWithProduct){
		adapter.addItem(order)
	}
}
