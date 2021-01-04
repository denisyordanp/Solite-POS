package com.sosialite.solite_pos.view.items

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.databinding.FragmentItemsBinding

class ItemsFragment : Fragment() {

	private lateinit var _binding: FragmentItemsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_items, container, false)
    }
}
