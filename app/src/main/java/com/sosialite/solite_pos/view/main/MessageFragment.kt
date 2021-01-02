package com.sosialite.solite_pos.view.main

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
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

	companion object{

		private var INSTANCE: MessageFragment? = null

		fun create(): MessageFragment{
			INSTANCE = MessageFragment()
			return INSTANCE!!
		}
	}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
			_binding.btnMsPositive.setOnClickListener { positiveCallback?.invoke() }
		}
		if (!negativeTxt.isNullOrEmpty()){
			_binding.btnMsPositive.text = negativeTxt
			_binding.btnMsPositive.visibility = View.VISIBLE
			_binding.btnMsPositive.setOnClickListener { negativeCallback?.invoke(dialog) }
		}
	}

	fun setMessage(message: String): MessageFragment{
		this.message = message
		return INSTANCE!!
	}

	fun setOnPositiveButton(text: String, callback: (() -> Unit)): MessageFragment{
		positiveTxt = text
		positiveCallback = callback
		return INSTANCE!!
	}

	fun setOnNegativeButton(text: String, callback: ((Dialog?) -> Unit)): MessageFragment{
		negativeTxt = text
		negativeCallback = callback
		return INSTANCE!!
	}
}
