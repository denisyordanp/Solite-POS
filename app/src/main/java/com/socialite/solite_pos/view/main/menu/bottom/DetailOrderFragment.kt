package com.socialite.solite_pos.view.main.menu.bottom

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.socialite.solite_pos.R
import com.socialite.solite_pos.adapters.recycleView.order.ProductDetailOrderAdapter
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.databinding.FragmentDetailOrderBinding
import com.socialite.solite_pos.utils.printer.PrintBill
import com.socialite.solite_pos.utils.tools.BottomSheetView
import com.socialite.solite_pos.utils.tools.MessageBottom
import com.socialite.solite_pos.view.main.menu.order.OrderActivity
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel

class DetailOrderFragment(private var order: OrderWithProduct?) : BottomSheetDialogFragment() {

	private lateinit var _binding: FragmentDetailOrderBinding
	private lateinit var productDetailOrderAdapter: ProductDetailOrderAdapter
	private lateinit var printBill: PrintBill

	private lateinit var viewModel: MainViewModel
	private lateinit var orderViewModel: OrderViewModel

	constructor() : this(null)

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentDetailOrderBinding.inflate(inflater, container, false)
		return _binding.root
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
		return BottomSheetView.setBottom(bottomSheetDialog)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null) {
			prepare()
			setAdapter()
			setData()
			setListener()
		}
	}

	private fun prepare() {
		printBill = PrintBill(activity!!)
		viewModel = MainViewModel.getMainViewModel(activity!!)
		orderViewModel = OrderViewModel.getOrderViewModel(activity!!)
	}

	private fun setAdapter() {
		productDetailOrderAdapter = ProductDetailOrderAdapter()
		_binding.rvDetailOrder.layoutManager = LinearLayoutManager(activity)
		_binding.rvDetailOrder.adapter = productDetailOrderAdapter
	}

	private fun setListener() {
		_binding.btnDoCancel.setOnClickListener { cancelOrder() }
		_binding.btnDoPrintBill.setOnClickListener {
//			if (order != null) {
//				printBill.doPrintBill(order!!, PrintBill.BILL) {
//
//				}
//			}
		}
		_binding.btnDoPrintOrder.setOnClickListener {
//			if (order != null) {
//				printBill.doPrintBill(order!!, PrintBill.QUEUE) {
//
//				}
//			}
		}
		_binding.btnDoDone.setOnClickListener { doneOrder() }
		_binding.btnDoEdit.setOnClickListener { editOrder(order!!) }
		_binding.btnDoPay.setOnClickListener {
			PayFragment(order).show(childFragmentManager, "pay")
		}
	}

	private fun setData() {
		if (order == null) return

		when (order!!.order.order.status) {
			Order.ON_PROCESS -> {
				_binding.contDoCancel.visibility = View.VISIBLE
				_binding.contDoPay.visibility = View.VISIBLE
				_binding.contDoDone.visibility = View.VISIBLE
			}

			Order.NEED_PAY -> {
				_binding.contDoPay.visibility = View.VISIBLE
			}

			Order.DONE -> {
				_binding.contDoPrintBill.visibility = View.VISIBLE
				_binding.btnDoEdit.visibility = View.GONE
			}

			else -> {
				_binding.linearLayout.visibility = View.GONE
			}
		}

		val no = "No. ${order!!.order.order.orderNo}"
		_binding.tvDoDate.text = order!!.order.order.timeString
		_binding.tvDoNoOrder.text = no
		_binding.tvDoName.text = order!!.order.customer.name
		_binding.tvDoTakeAway.text = if (order!!.order.order.isTakeAway) {
			"Dibungkus"
		} else {
			"Makan Ditempat"
		}
		productDetailOrderAdapter.order = order
	}

	private fun doneOrder(){
		if (order == null) return
		MessageBottom(childFragmentManager)
			.setMessageImage(ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_ask_question, null))
			.setMessage("Yakin pesanan telah sampai ke konsumen?")
			.setPositiveListener("Ya, sudah"){
				updateOrder(order!!, Order.NEED_PAY)
			}
			.setNegativeListener("Belum"){ dialog?.dismiss() }
			.show()
	}

	private fun cancelOrder(){
		if (order == null) return
		val isCancelAble = order!!.order.order.isCancelable(activity!!)

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
		bottom.setMessageImage(ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_ask_question, null))
		bottom.setMessage(message)
		bottom.setNegativeListener(negative){ it?.dismiss() }
		if (isCancelAble){
			bottom.setPositiveListener("Ya") {
				updateOrder(order!!, Order.CANCEL)
				dialog?.dismiss()
			}
		}
		bottom.show()
	}

	private fun updateOrder(order: OrderWithProduct, status: Int) {
        order.order.order.status = status
        orderViewModel.updateOrder(order.order.order)
    }

    private fun editOrder(order: OrderWithProduct) {
        val intent = Intent(requireContext(), OrderActivity::class.java)
        intent.putExtra(OrderActivity.EDIT_ORDER, order)
        startActivity(intent)
        dialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        printBill.onDestroy()
    }
}
