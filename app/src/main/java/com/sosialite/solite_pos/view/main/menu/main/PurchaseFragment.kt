package com.sosialite.solite_pos.view.main.menu.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.databinding.FragmentPurchaseBinding

class PurchaseFragment : Fragment() {

	private lateinit var _binding: FragmentPurchaseBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentPurchaseBinding.inflate(inflater, container, false)
        return _binding.root
    }


}
