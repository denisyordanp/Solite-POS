package com.socialite.solite_pos.view.main.menu.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialite.solite_pos.databinding.FragmentPurchaseBinding
import com.socialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.socialite.solite_pos.view.main.menu.adapter.PurchaseAdapter
import com.socialite.solite_pos.view.viewmodel.MainViewModel
import com.socialite.solite_pos.vo.Status

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

    private fun getPurchases(){
        viewModel.purchases.observe(activity!!){
            when(it.status){
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    adapter.items = ArrayList(it.data)
                }
                Status.ERROR -> {}
            }
        }
    }
}
