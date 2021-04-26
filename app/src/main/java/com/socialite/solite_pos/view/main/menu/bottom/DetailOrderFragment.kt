package com.socialite.solite_pos.view.main.menu.bottom

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.databinding.FragmentDetailOrderBinding
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah
import com.socialite.solite_pos.utils.tools.BottomSheet
import com.socialite.solite_pos.utils.tools.MessageBottom
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.view.main.menu.adapter.ItemOrderListAdapter
import com.socialite.solite_pos.view.main.menu.order.OrderActivity
import com.socialite.solite_pos.view.main.opening.MainActivity
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.MainViewModel.Companion.getMainViewModel

class DetailOrderFragment(private var order: OrderWithProduct?) : BottomSheetDialogFragment() {

	private lateinit var _binding: FragmentDetailOrderBinding
	private lateinit var adapter: ItemOrderListAdapter
	private lateinit var viewModel: MainViewModel

	private var mainActivity: MainActivity? = null

	constructor(): this(null)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (activity != null) {
			try {
				mainActivity = activity as MainActivity
			} catch (e: ClassCastException) {
				e.printStackTrace()
			}
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

		var act: SocialiteActivity = activity as SocialiteActivity

		if (mainActivity != null) {

			act = mainActivity!!
            viewModel = getMainViewModel(act)

            _binding.btnDoCancel.setOnClickListener { cancelOrder() }
            _binding.btnDoPrint.setOnClickListener {
                mainActivity?.printBill?.doPrint(order)
            }
			_binding.btnDoDone.setOnClickListener { doneOrder() }
			_binding.btnDoEdit.setOnClickListener { editOrder(order!!) }

		}

		adapter = ItemOrderListAdapter(act, ItemOrderListAdapter.DETAIL)
		_binding.rvDetailOrder.layoutManager = LinearLayoutManager(act)
		_binding.rvDetailOrder.adapter = adapter

		setData()

		_binding.btnDoPay.setOnClickListener {
			PayFragment(order, this).show(childFragmentManager, "pay")
		}
	}

	fun showReturn(order: OrderWithProduct){
		val inReturn = toRupiah(order.order.orderPayment?.inReturn(order.grandTotal))
		MessageBottom(childFragmentManager)
				.setMessage("Kembalian : $inReturn")
				.setNegativeListener("Ok"){
					it?.dismiss()
				}.show()
	}

	private fun setData() {
		if (order != null){
			when(order!!.order.order.status){
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

				else -> {
					_binding.linearLayout.visibility = View.GONE
				}
			}

			val no = "No. ${order?.order?.order?.orderNo}"
			_binding.tvDoDate.text = order?.order?.order?.timeString
			_binding.tvDoNoOrder.text = no
			_binding.tvDoName.text = order?.order?.customer?.name
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

	private fun editOrder(order: OrderWithProduct) {
		val intent = Intent(requireContext(), OrderActivity::class.java)
		intent.putExtra(OrderActivity.EDIT_ORDER, order)
		startActivity(intent)
		dialog?.dismiss()
	}
}
