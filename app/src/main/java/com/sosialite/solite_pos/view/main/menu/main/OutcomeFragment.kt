package com.sosialite.solite_pos.view.main.menu.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosialite.solite_pos.databinding.FragmentOutcomeBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.currentDate
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.view.main.menu.adapter.OutcomeAdapter
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class OutcomeFragment : Fragment() {

	private lateinit var _binding: FragmentOutcomeBinding
	private lateinit var viewModel: MainViewModel
	private lateinit var adapter: OutcomeAdapter

	companion object {
		val instance: OutcomeFragment
			get() {
				return OutcomeFragment()
			}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View {
		_binding = FragmentOutcomeBinding.inflate(inflater, container, false)
		return _binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){

			viewModel = getViewModel(activity!!)
			adapter = OutcomeAdapter(childFragmentManager)

			_binding.rvOc.layoutManager = LinearLayoutManager(activity)
			_binding.rvOc.adapter = adapter

			getData(currentDate)

		}
	}

	private fun getData(date: String){
		viewModel.getOutcome(date).observe(activity!!, {
			if (!it.isNullOrEmpty()){
				adapter.items = ArrayList(it)
			}
		})
	}
}
