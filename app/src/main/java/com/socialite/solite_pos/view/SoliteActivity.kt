package com.socialite.solite_pos.view

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.socialite.solite_pos.R

open class SoliteActivity : AppCompatActivity() {

    private var isBackNoticed: Boolean = false

    override fun onBackPressed() {
        if (isTaskRoot && !isBackNoticed) {
            Toast.makeText(this, getString(R.string.touch_once_more_to_close_the_app), Toast.LENGTH_LONG)
                .show()
            isBackNoticed = true
        } else {
            super.onBackPressed()
        }
    }
}
