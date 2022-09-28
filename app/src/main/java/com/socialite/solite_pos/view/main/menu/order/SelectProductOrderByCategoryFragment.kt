package com.socialite.solite_pos.view.main.menu.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.socialite.solite_pos.adapters.recycleView.order.ProductOrderAdapter
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.databinding.FragmentProductOrderBinding
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SelectProductOrderByCategoryFragment(
    private val type: Int,
    private var category: Category?,
    private var callback: ((ProductOrderDetail) -> Unit)?
) : Fragment() {

    private lateinit var _binding: FragmentProductOrderBinding
    private lateinit var adapter: ProductOrderAdapter
    private lateinit var viewModel: ProductViewModel

    constructor() : this(0, null, null)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductOrderBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {

            viewModel = ProductViewModel.getMainViewModel(activity!!)
            adapter = ProductOrderAdapter(type, activity!!, callback)

            _binding.rvProductOrder.layoutManager = GridLayoutManager(activity, 4)
            _binding.rvProductOrder.adapter = adapter

            getProducts()
        }
    }

    private fun getProducts() {
        lifecycleScope.launch {
            category?.let {
                viewModel.getProducts(it.id)
                    .collect { products ->
                        if (products.isNotEmpty()) {
                            adapter.setProducts(products)
                        }
                    }
            }
        }
    }
}
