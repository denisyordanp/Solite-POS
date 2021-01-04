package com.sosialite.solite_pos.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sosialite.solite_pos.databinding.FragmentMessageBinding
import com.sosialite.solite_pos.utils.tools.BottomSheet

class MessageFragment : BottomSheetDialogFragment() {

	private lateinit var _binding: FragmentMessageBinding
	private var message: String? = ""

	private var positiveTxt: String? = ""
	private var negativeTxt: String? = ""

	private var positiveCallback: (() -> Unit)? = null
	private var negativeCallback: ((Dialog?) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        return _binding.root
    }

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
		return BottomSheet.setBottom(bottomSheetDialog)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){
			_binding.tvMsMessage.text = message

			setView()
		}
	}

	private fun setView(){
		if (!positiveTxt.isNullOrEmpty()){
			_binding.btnMsPositive.text = positiveTxt
			_binding.btnMsPositive.visibility = View.VISIBLE
			_binding.btnMsPositive.setOnClickListener {
				positiveCallback?.invoke()
				dialog?.dismiss()
			}
		}
		if (!negativeTxt.isNullOrEmpty()){
			_binding.btnMsNegative.text = negativeTxt
			_binding.btnMsNegative.visibility = View.VISIBLE
			_binding.btnMsNegative.setOnClickListener { negativeCallback?.invoke(dialog) }
		}
	}

	fun setMessage(message: String){
		this.message = message
	}

	fun setOnPositiveButton(text: String, callback: (() -> Unit)){
		positiveTxt = text
		positiveCallback = callback
	}

	fun setOnNegativeButton(text: String, callback: ((Dialog?) -> Unit)){
		negativeTxt = text
		negativeCallback = callback
	}
}
