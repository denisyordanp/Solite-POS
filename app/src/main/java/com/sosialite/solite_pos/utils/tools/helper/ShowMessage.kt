package com.sosialite.solite_pos.utils.tools.helper

import androidx.fragment.app.FragmentManager
import com.sosialite.solite_pos.view.dialog.AlertMessageFragment

class ShowMessage(private var fm: FragmentManager?) {

	var fragment: AlertMessageFragment = AlertMessageFragment()

	fun show() {
		if (fm != null){
			fragment.show(fm!!, "Message")
		}
	}

	fun setName(name: String?): ShowMessage {
		fragment.name = name
		return this
	}
}
