package com.socialite.solite_pos.utils.tools

import android.app.Dialog
import android.graphics.drawable.Drawable
import androidx.fragment.app.FragmentManager
import com.socialite.solite_pos.view.dialog.MessageFragment

class MessageBottom(private var fm: FragmentManager) {
	private var fragment: MessageFragment = MessageFragment()

    fun show() {
        fragment.show(fm, "Message")
    }

    fun setMessage(message: String): MessageBottom {
        fragment.message = message
        return this
    }

    fun setPositiveListener(text: String, callback: (() -> Unit)?): MessageBottom {
        fragment.setOnPositiveButton(text, callback)
        return this
    }

    fun setNegativeListener(text: String, callback: ((Dialog?) -> Unit)?): MessageBottom {
        fragment.setOnNegativeButton(text, callback)
        return this
    }

    fun setMessageImage(image: Drawable?): MessageBottom {
        fragment.image = image
        return this
    }
}
