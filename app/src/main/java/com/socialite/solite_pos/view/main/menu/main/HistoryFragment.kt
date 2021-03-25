package com.socialite.solite_pos.view.main.menu.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.socialite.solite_pos.databinding.FragmentHistoryBinding
import com.socialite.solite_pos.view.main.menu.history.SalesActivity

class HistoryFragment : Fragment() {

    private lateinit var _binding: FragmentHistoryBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity == null) return

        _binding.btnHtSales.setOnClickListener {
            val intent = Intent(activity, SalesActivity::class.java)
            startActivity(intent)
        }
    }
}