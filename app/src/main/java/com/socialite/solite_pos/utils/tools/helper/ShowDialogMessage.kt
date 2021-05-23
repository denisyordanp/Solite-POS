package com.socialite.solite_pos.utils.tools.helper

import androidx.fragment.app.FragmentManager
import com.socialite.solite_pos.view.dialog.AlertMessageFragment

class ShowDialogMessage(private var fm: FragmentManager?) {

	var fragment: AlertMessageFragment = AlertMessageFragment()

	fun show() {
		if (fm != null){
			fragment.show(fm!!, "Message")
		}
	}

	fun setName(name: String?): ShowDialogMessage {
		fragment.name = name
		return this
	}
}
