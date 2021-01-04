package com.sosialite.solite_pos.view.main.menu.bottom

import android.app.Dialog
import android.content.Intent
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
import com.sosialite.solite_pos.view.main.MainActivity
import com.sosialite.solite_pos.view.main.menu.adapter.ItemOrderListAdapter
import com.sosialite.solite_pos.view.main.menu.order.OrderActivity

class DetailOrderFragment(private var order: Order?) : BottomSheetDialogFragment() {

	private lateinit var _binding: FragmentDetailOrderBinding
	private lateinit var adapter: ItemOrderListAdapter
	private var mainActivity: MainActivity? = null

	constructor(): this(null)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (activity != null){
			mainActivity = activity as MainActivity
		}
	}

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
			_binding.btnDoEdit.setOnClickListener {
				startActivityForResult(
						Intent(context, OrderActivity::class.java)
								.putExtra(OrderActivity.ORDER_TYPE, OrderActivity.EDIT_ORDER)
								.putExtra(OrderActivity.ORDER_DATA, order), OrderActivity.EDIT_ORDER_RQ_CODE
				)
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == OrderActivity.EDIT_ORDER_RQ_CODE){
			if (data != null){
				val order: Order? = data.getSerializableExtra(MainActivity.EXTRA_ORDER) as Order?
				if (order != null){
					this.order = order
					mainActivity?.addOrder(order)
					setData()
				}
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
				_binding.btnDoEdit.visibility = View.INVISIBLE
			}
		}

		val no = "No. ${order?.orderNo}"
		_binding.tvDoDate.text = order?.timeString
		_binding.tvDoNoOrder.text = no
		_binding.tvDoName.text = order?.customer?.name
		adapter.setItems(order)
	}
}
