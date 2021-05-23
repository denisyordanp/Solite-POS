package com.socialite.solite_pos.view.main.menu.master.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.databinding.FragmentListProductMasterBinding
import com.socialite.solite_pos.adapters.recycleView.product.ProductMasterAdapter
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import com.socialite.solite_pos.vo.Status

class ListProductMasterFragment(private var category: Category) : Fragment() {

	private lateinit var _binding: FragmentListProductMasterBinding
	private lateinit var adapter: ProductMasterAdapter
	private lateinit var viewModel: ProductViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
		_binding = FragmentListProductMasterBinding.inflate(inflater, container, false)
        return _binding.root
    }

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){

			viewModel = ProductViewModel.getMainViewModel(activity!!)

			adapter = ProductMasterAdapter(activity!!)
			_binding.rvProductMaster.layoutManager = LinearLayoutManager(activity)
			_binding.rvProductMaster.adapter = adapter

			setData()
		}
	}

	private fun setData(){
		viewModel.getProducts(category.id).observe(activity!!, {
			when(it.status){
				Status.LOADING -> { }
				Status.SUCCESS -> {
					if (!it.data.isNullOrEmpty()){
						adapter.items = ArrayList(it.data)
					}
				}
				Status.ERROR -> { }
			}
		})
	}
}
