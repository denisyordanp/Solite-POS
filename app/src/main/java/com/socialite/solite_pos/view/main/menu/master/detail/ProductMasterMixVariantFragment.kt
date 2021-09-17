package com.socialite.solite_pos.view.main.menu.master.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.databinding.FragmentMasterProductMixVariantBinding
import com.socialite.solite_pos.adapters.recycleView.product.ProductMixVariantAdapter
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import com.socialite.solite_pos.vo.Status

class ProductMasterMixVariantFragment(
	private val variant: Variant?,
	private val category: Category?
	) : Fragment() {

	constructor() : this(null, null)

	private lateinit var _binding: FragmentMasterProductMixVariantBinding
	private lateinit var adapter: ProductMixVariantAdapter
	private lateinit var viewModel: ProductViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentMasterProductMixVariantBinding.inflate(inflater, container, false)
		return _binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null) {

			viewModel = ProductViewModel.getMainViewModel(activity!!)
			setAdapter()

			_binding.rvProductMixVariant.layoutManager = GridLayoutManager(activity, 5)
		}
	}

	private fun setAdapter(){
		adapter = ProductMixVariantAdapter(variant, activity!!)
		_binding.rvProductMixVariant.adapter = adapter

		getProduct()
	}

	private fun getProduct(){
		if (category != null){
			viewModel.getProductWithCategories(category.id).observe(activity!!, {
				when(it.status){
					Status.LOADING -> { }
					Status.SUCCESS -> {
						adapter.setProducts(ArrayList(it.data))
					}
					Status.ERROR -> {
						Toast.makeText(activity!!, "Error", Toast.LENGTH_SHORT).show()
					}
				}
			})
		}
	}
}
