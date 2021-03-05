package com.socialite.solite_pos.utils.config

import java.text.NumberFormat
import java.util.*

class RupiahUtils {
    companion object{
        private val locale: Locale
        get() = Locale.getDefault()

        fun toRupiah(amount: Long?): String{
            if (amount == null) return ""
            return "Rp. ${thousand(amount)}"
        }

        fun thousand(amount: Long?): String{
            return NumberFormat.getNumberInstance(locale).format(amount)
        }
    }
}