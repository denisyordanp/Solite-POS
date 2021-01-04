package com.sosialite.solite_pos.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.sosialite.solite_pos.databinding.FragmentAlertMessageBinding

class AlertMessageFragment : DialogFragment() {

	private lateinit var _binding: FragmentAlertMessageBinding
	var name: String? = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentAlertMessageBinding.inflate(inflater, container, false)
		dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return _binding.root
    }

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){
			_binding.tvAmName.text = name
			_binding.btnAmOk.setOnClickListener { dialog?.dismiss() }
		}
	}
}
