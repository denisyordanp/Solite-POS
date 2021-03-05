package com.socialite.solite_pos.utils.config

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment

open class CustomDialogFragment: DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setDialogFragment(dialog?.window)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setDialogFragment(w: Window?){
        w?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        w?.requestFeature(Window.FEATURE_NO_TITLE)
    }
}