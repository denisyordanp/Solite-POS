package com.sosialite.solite_pos.view.main.menu.bottom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sosialite.solite_pos.data.source.local.entity.helper.Order
import com.sosialite.solite_pos.databinding.FragmentDetailOrderBinding
import com.sosialite.solite_pos.utils.tools.BottomSheet
import com.sosialite.solite_pos.view.main.menu.adapter.ItemOrderListAdapter


class DetailOrderFragment(private var order: Order?) : BottomSheetDialogFragment() {

	private lateinit var _binding: FragmentDetailOrderBinding
	private lateinit var adapter: ItemOrderListAdapter

	constructor(): this(null)

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View {
		_binding = FragmentDetailOrderBinding.inflate(inflater, container, false)
		return _binding.root
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
		return BottomSheet.setBottom(bottomSheetDialog)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){
			adapter = ItemOrderListAdapter(ItemOrderListAdapter.DETAIL)
			_binding.rvDetailOrder.layoutManager = LinearLayoutManager(activity)
			_binding.rvDetailOrder.adapter = adapter

			setData()

			_binding.btnDoCancel.setOnClickListener{ }
			_binding.btnDoPrint.setOnClickListener{ }
			_binding.btnDoPay.setOnClickListener{
				PayFragment(order).show(childFragmentManager, "pay")
			}
		}
	}

	private fun setData(){

		when(order?.status){
			Order.ON_PROCESS -> {
				_binding.contDoCancel.visibility = View.VISIBLE
				_binding.contDoPay.visibility = View.VISIBLE
				_binding.contDoDone.visibility = View.VISIBLE
			}

			Order.NEED_PAY -> {
				_binding.contDoPay.visibility = View.VISIBLE
			}
			Order.DONE -> {
				_binding.contDoPrint.visibility = View.VISIBLE
			}
		}

		val no = "No. ${order?.orderNo}"
		_binding.tvDoDate.text = order?.timeString
		_binding.tvDoNoOrder.text = no
		_binding.tvDoName.text = order?.customer?.name
		adapter.setItems(order)
	}
}
