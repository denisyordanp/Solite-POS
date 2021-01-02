package com.sosialite.solite_pos.view.main.menu.newOrder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.sosialite.solite_pos.data.source.local.entity.Category
import com.sosialite.solite_pos.data.source.local.entity.Product
import com.sosialite.solite_pos.data.source.local.entity.helper.DetailOrder
import com.sosialite.solite_pos.databinding.FragmentProductListBinding
import com.sosialite.solite_pos.view.main.menu.adapter.ProductListAdapter

class ProductListFragment(
	private var category: Category?,
	private var callback: ((Boolean, DetailOrder) -> Unit)?
	) : Fragment() {

	private lateinit var _binding: FragmentProductListBinding
	private lateinit var adapter: ProductListAdapter

	constructor(): this(null, null)

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View {
		_binding = FragmentProductListBinding.inflate(inflater, container, false)
		return _binding.root
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){

			adapter = ProductListAdapter()
			_binding.rvProductList.layoutManager = GridLayoutManager(activity, 5)
			_binding.rvProductList.adapter = adapter
			adapter.setItems(getProduct())
			adapter.callback = callback
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)

	}

	private fun getProduct(): ArrayList<DetailOrder>{
		val items: ArrayList<DetailOrder> = ArrayList()
		items.add(DetailOrder(Product("5412", "Angsio Ceker Ayam","Porsian", "Ceker Ayam", 15000), 1))
		items.add(DetailOrder(Product("815", "Siomay Udang", "Porsian", "Siomay Udang", 14000), 2))
		items.add(DetailOrder(Product("356", "Kulit Tahu Udang", "Porsian", "Kulit Tahu Udang", 14000), 2))
		if (category != null){
			when (category!!.id) {
				1 -> {
					items.add(DetailOrder(Product("356", "Kulit Tahu Udang", "Porsian", "Kulit Tahu Udang", 14000), 2))
				}
				2 -> {
					items.add(DetailOrder(Product("356", "Kulit Tahu Udang", "Porsian", "Kulit Tahu Udang", 14000), 2))
					items.add(DetailOrder(Product("356", "Kulit Tahu Udang", "Porsian", "Kulit Tahu Udang", 14000), 2))
				}
				3 -> {
					items.add(DetailOrder(Product("356", "Kulit Tahu Udang", "Porsian", "Kulit Tahu Udang", 14000), 2))
					items.add(DetailOrder(Product("356", "Kulit Tahu Udang", "Porsian", "Kulit Tahu Udang", 14000), 2))
					items.add(DetailOrder(Product("356", "Kulit Tahu Udang", "Porsian", "Kulit Tahu Udang", 14000), 2))
				}
				4 -> {
					items.add(DetailOrder(Product("356", "Kulit Tahu Udang", "Porsian", "Kulit Tahu Udang", 14000), 2))
					items.add(DetailOrder(Product("356", "Kulit Tahu Udang", "Porsian", "Kulit Tahu Udang", 14000), 2))
					items.add(DetailOrder(Product("356", "Kulit Tahu Udang", "Porsian", "Kulit Tahu Udang", 14000), 2))
					items.add(DetailOrder(Product("356", "Kulit Tahu Udang", "Porsian", "Kulit Tahu Udang", 14000), 2))
				}
				else -> {
					items.add(DetailOrder(Product("356", "Kulit Tahu Udang", "Porsian", "Kulit Tahu Udang", 14000), 2))
					items.add(DetailOrder(Product("356", "Kulit Tahu Udang", "Porsian", "Kulit Tahu Udang", 14000), 2))
					items.add(DetailOrder(Product("356", "Kulit Tahu Udang", "Porsian", "Kulit Tahu Udang", 14000), 2))
					items.add(DetailOrder(Product("356", "Kulit Tahu Udang", "Porsian", "Kulit Tahu Udang", 14000), 2))
					items.add(DetailOrder(Product("356", "Kulit Tahu Udang", "Porsian", "Kulit Tahu Udang", 14000), 2))
				}
			}
		}
		return items
	}
}
