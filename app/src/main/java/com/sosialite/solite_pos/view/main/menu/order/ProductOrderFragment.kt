package com.sosialite.solite_pos.view.main.menu.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.sosialite.solite_pos.data.source.local.entity.helper.DetailOrder
import com.sosialite.solite_pos.data.source.local.entity.helper.Order
import com.sosialite.solite_pos.data.source.local.entity.main.Category
import com.sosialite.solite_pos.databinding.FragmentProductOrderBinding
import com.sosialite.solite_pos.utils.tools.OrderListBroadcast
import com.sosialite.solite_pos.utils.tools.helper.DataDummy
import com.sosialite.solite_pos.view.main.menu.adapter.ProductOrderAdapter

class ProductOrderFragment(
		private var category: Category?,
		private var order: Order?,
		private var callback: ((Boolean, DetailOrder) -> Unit)?
	) : Fragment() {

	private lateinit var _binding: FragmentProductOrderBinding
	private lateinit var adapter: ProductOrderAdapter

	private lateinit var deleteReceiver: OrderListBroadcast

	constructor(): this(null, null, null)

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View {
		_binding = FragmentProductOrderBinding.inflate(inflater, container, false)
		return _binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){

			adapter = ProductOrderAdapter(order)

			_binding.rvProductOrder.layoutManager = GridLayoutManager(activity, 5)
			_binding.rvProductOrder.adapter = adapter

			adapter.items = DataDummy.DataProduct.getDetailProduct(category)
			adapter.callback = callback

			deleteReceiver = OrderListBroadcast((activity!!))
			deleteReceiver.setReceiver {
				Log.w("TESTINGDATA", "receiver data : $it")
				deleteData(it)
			}
		}
	}

	private fun deleteData(code: Int?){
		if (code != null){
			adapter.deleteData(code)
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		deleteReceiver.removeReceiver()
	}
}
