package com.socialite.solite_pos.view.main.menu.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialite.solite_pos.adapters.recycleView.purchase.PurchaseAdapter
import com.socialite.solite_pos.databinding.FragmentPurchaseBinding
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.MainViewModel.Companion.getMainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PurchaseFragment : Fragment() {

    private lateinit var _binding: FragmentPurchaseBinding
    private lateinit var adapter: PurchaseAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPurchaseBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {

            viewModel = getMainViewModel(requireActivity())

            adapter = PurchaseAdapter(requireActivity())
            _binding.rvPurchase.layoutManager = LinearLayoutManager(activity)
            _binding.rvPurchase.adapter = adapter
        }
    }

    override fun onStart() {
        super.onStart()
        getPurchases()
    }

    private fun getPurchases() {
        lifecycleScope.launch {
            viewModel.purchases
                .collect {
                    if (it.isNotEmpty()) {
                        adapter.setPurchases(it)
                    }
                }
        }
    }
}
