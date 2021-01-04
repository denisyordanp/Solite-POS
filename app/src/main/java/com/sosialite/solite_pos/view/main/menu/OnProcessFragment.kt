package com.sosialite.solite_pos.view.main.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.sosialite.solite_pos.data.source.local.entity.helper.Order
import com.sosialite.solite_pos.databinding.FragmentOnProcessBinding
import com.sosialite.solite_pos.utils.tools.helper.DataDummy
import com.sosialite.solite_pos.view.main.menu.adapter.OrderListAdapter

class OnProcessFragment : Fragment() {

	private lateinit var _binding: FragmentOnProcessBinding
	private lateinit var adapter: OrderListAdapter

	companion object{
		val instance: OnProcessFragment
		get() {
			return OnProcessFragment()
		}
	}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
		_binding = FragmentOnProcessBinding.inflate(inflater, container, false)
        return _binding.root
    }

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null && context != null){
			adapter = OrderListAdapter(context!!, activity!!.supportFragmentManager)
			adapter.setItems(DataDummy.DataOrder.getProcess())

			_binding.rvOp.layoutManager = GridLayoutManager(activity, 4)
			_binding.rvOp.adapter = adapter
		}
	}

	fun addItem(order: Order){
		adapter.addItem(order)
	}
}
