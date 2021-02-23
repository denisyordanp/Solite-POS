package com.sosialite.solite_pos.view.main.menu.bottom

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sosialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import com.sosialite.solite_pos.databinding.FragmentDetailOrderBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.utils.tools.BottomSheet
import com.sosialite.solite_pos.utils.tools.MessageBottom
import com.sosialite.solite_pos.view.main.MainActivity
import com.sosialite.solite_pos.view.main.menu.adapter.ItemOrderListAdapter
import com.sosialite.solite_pos.view.main.menu.order.OrderActivity
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class DetailOrderFragment(private var order: OrderWithProduct?) : BottomSheetDialogFragment() {

	private lateinit var _binding: FragmentDetailOrderBinding
	private lateinit var adapter: ItemOrderListAdapter
	private lateinit var viewModel: MainViewModel

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
		if (mainActivity != null){

			viewModel = getViewModel(mainActivity!!)

			adapter = ItemOrderListAdapter(ItemOrderListAdapter.DETAIL)
			_binding.rvDetailOrder.layoutManager = LinearLayoutManager(mainActivity)
			_binding.rvDetailOrder.adapter = adapter

			setData()

			_binding.btnDoCancel.setOnClickListener{ cancelOrder() }
			_binding.btnDoPrint.setOnClickListener{
				mainActivity?.printBill?.doPrint(order)
			}
			_binding.btnDoPay.setOnClickListener{
				PayFragment(order).show(childFragmentManager, "pay")
			}
			_binding.btnDoDone.setOnClickListener { doneOrder() }
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
				val order = data.getSerializableExtra(MainActivity.EXTRA_ORDER) as OrderWithProduct?
				if (order != null){
					this.order = order
					mainActivity?.addOrder(order)
					setData()
				}
			}
		}
	}

	private fun setData(){
		if (order != null){
			when(order!!.order.status){
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
					_binding.btnDoEdit.visibility = View.GONE
				}

				else -> {
					_binding.linearLayout.visibility = View.GONE
				}
			}

			val no = "No. ${order?.order?.orderNo}"
			_binding.tvDoDate.text = order?.order?.timeString
			_binding.tvDoNoOrder.text = no
			_binding.tvDoName.text = order?.customer?.name
			adapter.order = order
		}
	}

	private fun doneOrder(){
		if (order != null){
			MessageBottom(childFragmentManager)
					.setMessage("Yakin pesanan telah sampai ke konsumen?")
					.setPositiveListener("Ya, sudah"){
						mainActivity?.setToNotPay(order)
					}
					.setNegativeListener("Belum"){ dialog?.dismiss() }
					.show()
		}
	}

	private fun cancelOrder(){
		if (order != null){
			val isCancelAble = order!!.order.isCancelable(mainActivity!!)

			val message: String
			val negative: String
			if (isCancelAble){
				message = "Anda yakin membatalkan pesanan ini?"
				negative = "Tidak"
			}else{
				message = "Maaf pesanan yang sudah selesai di masak tidak dapat dibatalkan"
				negative = "Oke"
			}

			val bottom = MessageBottom(childFragmentManager)
			bottom.setMessage(message)
			bottom.setNegativeListener(negative){ it?.dismiss() }
			if (isCancelAble){
				bottom.setPositiveListener("Ya"){
					mainActivity?.cancelOrder(order)
					dialog?.dismiss()
				}
			}
			bottom.show()
		}
	}
}
