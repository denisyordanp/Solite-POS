package com.sosialite.solite_pos.view.main.menu.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.sosialite.solite_pos.databinding.FragmentPurchaseBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.view.main.menu.adapter.PurchaseAdapter
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class PurchaseFragment : Fragment() {

	private lateinit var _binding: FragmentPurchaseBinding
	private lateinit var adapter: PurchaseAdapter
	private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentPurchaseBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null){

            viewModel = getViewModel(activity!!)

            adapter = PurchaseAdapter(childFragmentManager)
            _binding.rvPurchase.layoutManager = LinearLayoutManager(activity)
            _binding.rvPurchase.adapter = adapter

            getPurchases()
        }
    }

    fun addPurchase(purchase: PurchaseWithProduct){
        adapter.addPurchase(purchase)
    }

    private fun getPurchases(){
        adapter.items = ArrayList(viewModel.getPurchase())
    }
}
