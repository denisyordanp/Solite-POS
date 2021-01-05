package com.sosialite.solite_pos.view.main.menu.master.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosialite.solite_pos.data.source.local.entity.main.Category
import com.sosialite.solite_pos.databinding.FragmentProductMasterBinding
import com.sosialite.solite_pos.utils.config.MainConfig
import com.sosialite.solite_pos.view.main.menu.adapter.ProductMasterAdapter
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class ProductMasterFragment(private var category: Category?) : Fragment() {

	private lateinit var _binding: FragmentProductMasterBinding
	private lateinit var adapter: ProductMasterAdapter
	private lateinit var viewModel: MainViewModel

	constructor(): this(null)

	companion object {
		val instance: ProductMasterFragment
			get() {
				return ProductMasterFragment()
			}
	}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
		_binding = FragmentProductMasterBinding.inflate(inflater, container, false)
        return _binding.root
    }

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){

			viewModel = MainConfig.getViewModel(activity!!)
			adapter = ProductMasterAdapter()

			setData()

			_binding.rvProductMaster.layoutManager = LinearLayoutManager(activity)
			_binding.rvProductMaster.adapter = adapter
		}
	}

	private fun setData(){
		viewModel.getProducts(category?.id)?.observe(activity!!, {
			if (!it.isNullOrEmpty()){
				adapter.items = ArrayList(it)
			}
		})
	}
}
