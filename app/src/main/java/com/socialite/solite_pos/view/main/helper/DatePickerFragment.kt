package com.socialite.solite_pos.view.main.helper

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatDialogFragment
import com.socialite.solite_pos.utils.config.DateUtils.Companion.calendarToStr
import com.socialite.solite_pos.utils.config.DateUtils.Companion.currentDateTime
import com.socialite.solite_pos.utils.config.DateUtils.Companion.strToCalendar
import java.util.*

class DatePickerFragment(private var selectedDate: String, private val callback: ((String) -> Unit)?) : AppCompatDialogFragment(), DatePickerDialog.OnDateSetListener {

    constructor() : this(currentDateTime, null)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = strToCalendar(selectedDate)

        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val date = calendar[Calendar.DATE]
        val dialog = DatePickerDialog(activity!!, this, year, month, date)
        dialog.setTitle("Pilih tanggal")
        dialog.datePicker.maxDate = getMaxDate()
        return dialog
    }

    private fun getMaxDate(): Long {
        return Date().time
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = convertCalendar(year, month, dayOfMonth)
        callback?.invoke(calendarToStr(calendar))
    }

    private fun convertCalendar(year: Int, month: Int, dayOfMonth: Int): Calendar {
        val calendar: Calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
        return calendar
    }

}