package com.sosialite.solite_pos.utils.tools

import android.content.DialogInterface
import android.content.res.Resources
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomSheet {
	companion object{
		fun setBottom(bottom: BottomSheetDialog): BottomSheetDialog{
			bottom.setOnShowListener { dialog: DialogInterface ->
				val dialogC = dialog as BottomSheetDialog
				val bottomSheet = dialogC.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) as View

				val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
				bottomSheetBehavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
				bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
			}
			return bottom
		}
	}
}
