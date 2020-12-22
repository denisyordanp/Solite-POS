package com.sosialite.solite_pos.view.main.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.data.source.local.entity.Product
import com.sosialite.solite_pos.data.source.local.entity.helper.DetailOrder
import com.sosialite.solite_pos.data.source.local.entity.helper.Order
import com.sosialite.solite_pos.databinding.FragmentOnProcessBinding
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
			adapter = OrderListAdapter()
			adapter.setItems(getItems())

			_binding.rvOp.layoutManager = LinearLayoutManager(activity)
			_binding.rvOp.adapter = adapter
		}
	}

	private fun getItems(): ArrayList<Order>{
		val items: ArrayList<Order> = ArrayList()
		items.add(Order("Denis", "6545646", getProduct(), null))
		items.add(Order("Evaviliya", "54165", getProduct(), Calendar.getInstance()))
		items.add(Order("Linda", "23138", getProduct(), Calendar.getInstance()))
		return items
	}

	private fun getProduct(): ArrayList<DetailOrder>{
		val items: ArrayList<DetailOrder> = ArrayList()
		items.add(DetailOrder(Product("5412", "Angsio Ceker Ayam", 15000), 1))
		items.add(DetailOrder(Product("815", "Siomay Udang", 14000), 2))
		items.add(DetailOrder(Product("356", "Kulit Tahu Udang", 14000), 2))
		return items
	}
}
