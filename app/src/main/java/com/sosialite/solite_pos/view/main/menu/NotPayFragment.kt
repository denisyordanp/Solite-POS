package com.sosialite.solite_pos.view.main.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosialite.solite_pos.data.source.local.entity.Customer
import com.sosialite.solite_pos.data.source.local.entity.Product
import com.sosialite.solite_pos.data.source.local.entity.helper.DetailOrder
import com.sosialite.solite_pos.data.source.local.entity.helper.Order
import com.sosialite.solite_pos.databinding.FragmentNotPayBinding
import com.sosialite.solite_pos.utils.config.MainConfig
import com.sosialite.solite_pos.view.main.menu.adapter.OrderListAdapter
import java.util.*
import kotlin.collections.ArrayList

class NotPayFragment : Fragment() {

	private lateinit var _binding: FragmentNotPayBinding
	private lateinit var adapter: OrderListAdapter

	companion object {
		val instance: NotPayFragment
			get() {
				return NotPayFragment()
			}
	}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
		_binding = FragmentNotPayBinding.inflate(inflater, container, false)
        return _binding.root
    }

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){
			adapter = OrderListAdapter(activity!!.supportFragmentManager)
			adapter.setItems(getItems())

			_binding.rvNp.layoutManager = GridLayoutManager(activity, 4)
			_binding.rvNp.adapter = adapter
		}
	}

	private fun getItems(): ArrayList<Order>{
		val items: ArrayList<Order> = ArrayList()
		items.add(Order(Customer(22, "Evaviliya"), "54165", getProduct(), MainConfig.currentTime, Calendar.getInstance(), 0, Order.NEED_PAY))
		items.add(Order(Customer(22, "Evaviliya"), "54165", getProduct(), MainConfig.currentTime, Calendar.getInstance(), 0, Order.NEED_PAY))
		items.add(Order(Customer(22, "Evaviliya"), "54165", getProduct(), MainConfig.currentTime, Calendar.getInstance(), 0, Order.NEED_PAY))
		items.add(Order(Customer(22, "Evaviliya"), "54165", getProduct(), MainConfig.currentTime, Calendar.getInstance(), 0, Order.NEED_PAY))
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
