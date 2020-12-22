package com.sosialite.solite_pos.utils.config

import java.text.NumberFormat
import java.util.*

class MainConfig {
	companion object{
		fun thousand(amount: Int): String{
			return NumberFormat.getNumberInstance(Locale.getDefault()).format(amount)
		}
	}
}
