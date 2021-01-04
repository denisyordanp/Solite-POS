package com.sosialite.solite_pos.view.main.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.sosialite.solite_pos.databinding.FragmentNotPayBinding
import com.sosialite.solite_pos.utils.tools.helper.DataDummy
import com.sosialite.solite_pos.view.main.menu.adapter.OrderListAdapter

class NotPayFragment : Fragment() {

	private lateinit var _binding: FragmentNotPayBinding
	private lateinit var adapter: OrderListAdapter

	companion object {
		val instance: NotPayFragment
			get() {
				return NotPayFragment()
			}
	}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
		_binding = FragmentNotPayBinding.inflate(inflater, container, false)
        return _binding.root
    }

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null && context != null){
			adapter = OrderListAdapter(context!!, activity!!.supportFragmentManager)
			adapter.setItems(DataDummy.DataOrder.getPay())

			_binding.rvNp.layoutManager = GridLayoutManager(activity, 4)
			_binding.rvNp.adapter = adapter
		}
	}
}
