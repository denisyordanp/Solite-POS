package com.socialite.solite_pos.view.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.socialite.solite_pos.utils.config.DateUtils.Companion.calendarToStr
import com.socialite.solite_pos.utils.config.DateUtils.Companion.strToCalendar
import java.util.Calendar
import java.util.Date

class DatePickerFragment: AppCompatDialogFragment(), DatePickerDialog.OnDateSetListener {

    private var selectedDate: String? = null
    private var callback: ((String) -> Unit)? = null

    companion object {

        private const val TAG = "DatePickerFragment"
        fun createInstance(): DatePickerFragment {
            return DatePickerFragment()
        }
    }

    fun show(manager: FragmentManager, selectedDate: String, callback: (String) -> Unit) {
        this.selectedDate = selectedDate
        this.callback = callback
        super.show(manager, TAG)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return createDialog(selectedDate ?: "")
    }

    private fun createDialog(selectedDate: String): DatePickerDialog {
        val calendar = CalendarUtils(selectedDate)
        val dialog = DatePickerDialog(
            requireActivity(),
            this,
            calendar.year,
            calendar.month,
            calendar.date
        )
        dialog.setTitle("Pilih Tanggal")
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

    class CalendarUtils(selectedDate: String) {
        val calendar = strToCalendar(selectedDate)
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val date = calendar[Calendar.DATE]
    }

}
