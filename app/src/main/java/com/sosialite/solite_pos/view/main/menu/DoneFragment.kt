package com.sosialite.solite_pos.view.main.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.sosialite.solite_pos.databinding.FragmentDoneBinding
import com.sosialite.solite_pos.utils.tools.helper.DataDummy
import com.sosialite.solite_pos.view.main.menu.adapter.OrderListAdapter

class DoneFragment : Fragment() {

	private lateinit var _binding: FragmentDoneBinding
	private lateinit var adapter: OrderListAdapter

	companion object {
		val instance: DoneFragment
			get() {
				return DoneFragment()
			}
	}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
		_binding = FragmentDoneBinding.inflate(inflater, container, false)
        return _binding.root
    }

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null && context != null){
			adapter = OrderListAdapter(context!!, activity!!.supportFragmentManager)
			adapter.setItems(DataDummy.DataOrder.getDone())

			_binding.rvDn.layoutManager = GridLayoutManager(activity, 4)
			_binding.rvDn.adapter = adapter
		}
	}
}
