package com.sosialite.solite_pos.view.main.menu.master.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.sosialite.solite_pos.data.source.local.entity.room.master.Category
import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import com.sosialite.solite_pos.data.source.local.entity.room.master.Variant
import com.sosialite.solite_pos.databinding.FragmentProductMixVariantBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.view.main.menu.adapter.master.product.ProductMixVariantAdapter
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class ProductMixVariantFragment(
	private val variant: Variant?,
	private val category: Category?
	) : Fragment() {

	constructor(): this(null, null)

	private lateinit var _binding: FragmentProductMixVariantBinding
	private lateinit var adapter: ProductMixVariantAdapter
	private lateinit var viewModel: MainViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentProductMixVariantBinding.inflate(inflater, container, false)
		return _binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){

			viewModel = getViewModel(activity!!)
			setAdapter()

			_binding.rvProductMixVariant.layoutManager = GridLayoutManager(activity, 5)
			_binding.rvProductMixVariant.adapter = adapter

			getProductSelected()
		}
	}

	private fun setAdapter(){
		adapter = ProductMixVariantAdapter(ProductMixVariantAdapter.MASTER)
		adapter.addCallback = {
			addVariantMix(it)
		}
		adapter.delCallback = {
			delVariantMix(it)
		}
	}

	private fun getProductSelected(){
		if (variant != null){
			val data = viewModel.getVariantMixProduct(variant.id)
			if (!data.products.isNullOrEmpty()){
				val array: ArrayList<Product> = ArrayList()
				for (product in data.products){
					array.add(product)
				}
				adapter.selected = array
			}
			getProduct()
		}
	}

	private fun getProduct(){
		if (category != null){
			viewModel.getProductWithCategories(category.id).observe(activity!!, {
				if (!it.isNullOrEmpty()){
					adapter.items = ArrayList(it)
				}
			})
		}
	}

	private fun addVariantMix(product: Product){
		if (variant != null){
			viewModel.insertVariantMix(VariantMix(variant.id, product.id))
		}
	}

	private fun delVariantMix(product: Product){
		if (variant != null){
			viewModel.removeVariantMix(VariantMix(variant.id, product.id))
		}
	}
}
