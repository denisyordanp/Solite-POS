package com.sosialite.solite_pos.view.main.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.sosialite.solite_pos.data.source.local.entity.Customer
import com.sosialite.solite_pos.data.source.local.entity.Product
import com.sosialite.solite_pos.data.source.local.entity.helper.DetailOrder
import com.sosialite.solite_pos.data.source.local.entity.helper.Order
import com.sosialite.solite_pos.databinding.FragmentOnProcessBinding
import com.sosialite.solite_pos.utils.config.MainConfig
import com.sosialite.solite_pos.view.main.menu.adapter.OrderListAdapter
import java.util.*
import kotlin.collections.ArrayList

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
		if (activity != null){
			adapter = OrderListAdapter(activity!!.supportFragmentManager)
			adapter.setItems(getItems())

			_binding.rvOp.layoutManager = GridLayoutManager(activity, 4)
			_binding.rvOp.adapter = adapter
		}
	}

	fun addItem(order: Order){
		adapter.addItem(order)
	}

	private fun getItems(): ArrayList<Order>{
		val items: ArrayList<Order> = ArrayList()
		items.add(Order(Customer(21, "Linda"), "23138", getProduct(), MainConfig.currentTime, Calendar.getInstance(), 0, Order.ON_PROCESS))
		items.add(Order(Customer(21, "Linda"), "23138", getProduct(), MainConfig.currentTime, Calendar.getInstance(), 0, Order.ON_PROCESS))
		items.add(Order(Customer(21, "Linda"), "23138", getProduct(), MainConfig.currentTime, null, 0, Order.ON_PROCESS))
		items.add(Order(Customer(21, "Linda"), "23138", getProduct(), MainConfig.currentTime, null, 0, Order.ON_PROCESS))
		items.add(Order(Customer(21, "Linda"), "23138", getProduct(), MainConfig.currentTime, null, 0, Order.ON_PROCESS))
		items.add(Order(Customer(21, "Linda"), "23138", getProduct(), MainConfig.currentTime, null, 0, Order.ON_PROCESS))
		return items
	}

	private fun getProduct(): ArrayList<DetailOrder>{
		val items: ArrayList<DetailOrder> = ArrayList()
		items.add(DetailOrder(Product("5412", "Angsio Ceker Ayam","Porsian", "Ceker Ayam", 15000), 1))
		items.add(DetailOrder(Product("815", "Siomay Udang", "Porsian", "Siomay Udang", 14000), 2))
		items.add(DetailOrder(Product("356", "Kulit Tahu Udang", "Porsian", "Kulit Tahu Udang", 14000), 2))
		return items
	}
}
