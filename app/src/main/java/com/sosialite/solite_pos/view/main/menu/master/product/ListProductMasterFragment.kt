package com.sosialite.solite_pos.view.main.menu.master.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosialite.solite_pos.data.source.local.entity.room.master.Category
import com.sosialite.solite_pos.databinding.FragmentListProductMasterBinding
import com.sosialite.solite_pos.utils.config.MainConfig
import com.sosialite.solite_pos.view.main.menu.adapter.master.product.ProductMasterAdapter
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class ListProductMasterFragment(private var category: Category?) : Fragment() {

	private lateinit var _binding: FragmentListProductMasterBinding
	private lateinit var adapter: ProductMasterAdapter
	private lateinit var viewModel: MainViewModel

	constructor(): this(null)

	companion object {
		val instance: ListProductMasterFragment
			get() {
				return ListProductMasterFragment()
			}
	}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
		_binding = FragmentListProductMasterBinding.inflate(inflater, container, false)
        return _binding.root
    }

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){

			viewModel = MainConfig.getViewModel(activity!!)
			adapter = ProductMasterAdapter(parentFragmentManager)

			setData()

			_binding.rvProductMaster.layoutManager = LinearLayoutManager(activity)
			_binding.rvProductMaster.adapter = adapter
		}
	}

	private fun setData(){
		if (category != null){
			viewModel.getDataProduct(category!!.id).observe(activity!!, {
				if (!it.isNullOrEmpty()){
					adapter.items = ArrayList(it)
				}
			})
		}
	}
}
