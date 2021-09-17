package com.socialite.solite_pos.view.dialog

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.socialite.solite_pos.databinding.FragmentMessageBinding
import com.socialite.solite_pos.utils.tools.BottomSheetView

class MessageFragment : BottomSheetDialogFragment() {

	private lateinit var _binding: FragmentMessageBinding

	var message: String? = ""
	var image: Drawable? = null
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
		return BottomSheetView.setBottom(bottomSheetDialog)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null) {
			setFragmentView()
		}
	}

	private fun setFragmentView() {
		setMessage()
		setImageView()
		setPositiveView()
		setNegativeView()
	}

	private fun setMessage() {
		_binding.tvMsMessage.text = message
	}

	private fun setImageView() {
		if (image != null) {
			_binding.ivMsImage.setImageDrawable(image)
			_binding.ivMsImage.visibility = View.VISIBLE
		}
	}

	private fun setPositiveView() {
		if (!positiveTxt.isNullOrEmpty()) {
			_binding.btnMsPositive.text = positiveTxt
			_binding.btnMsPositive.visibility = View.VISIBLE
			_binding.btnMsPositive.setOnClickListener {
				onClickPositive()
			}
		}
	}

	private fun onClickPositive() {
		positiveCallback?.invoke()
		dialog?.dismiss()
	}

	private fun setNegativeView() {
		if (!negativeTxt.isNullOrEmpty()) {
			_binding.btnMsNegative.text = negativeTxt
			_binding.btnMsNegative.visibility = View.VISIBLE
			_binding.btnMsNegative.setOnClickListener {
				onClickNegative()
			}
		}
	}

	private fun onClickNegative() {
		negativeCallback?.invoke(dialog)
		dialog?.dismiss()
	}

	fun setOnPositiveButton(text: String, callback: (() -> Unit)?) {
		positiveTxt = text
		positiveCallback = callback
	}

	fun setOnNegativeButton(text: String, callback: ((Dialog?) -> Unit)?) {
		negativeTxt = text
		negativeCallback = callback
	}


}
