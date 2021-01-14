package com.sosialite.solite_pos.view.main.menu.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.sosialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.sosialite.solite_pos.data.source.local.entity.room.master.Category
import com.sosialite.solite_pos.databinding.FragmentProductOrderBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.view.main.menu.adapter.ProductOrderAdapter
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class ProductOrderFragment(
		private var category: Category?,
		private var callback: ((ProductOrderDetail) -> Unit)?
	) : Fragment() {

	private lateinit var _binding: FragmentProductOrderBinding
	private lateinit var adapter: ProductOrderAdapter
	private lateinit var viewModel: MainViewModel

	constructor(): this(null, null)

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View {
		_binding = FragmentProductOrderBinding.inflate(inflater, container, false)
		return _binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){

			viewModel = getViewModel(activity!!)
			adapter = ProductOrderAdapter(parentFragmentManager, callback)

			_binding.rvProductOrder.layoutManager = GridLayoutManager(activity, 5)
			_binding.rvProductOrder.adapter = adapter

			getProducts()
		}
	}

	private fun getProducts(){
		if (category != null){
			viewModel.getDataProduct(category!!.id).observe(activity!!, {
				if (!it.isNullOrEmpty()){
					adapter.items = ArrayList(it)
				}
			})
		}
	}
}
