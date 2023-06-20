package com.socialite.solite_pos.view.factory

import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.socialite.solite_pos.R

object DateAndTimeManager {

    fun getTimePickerBuilder() = MaterialTimePicker.Builder()
        .setTitleText(R.string.select_time)

    fun getRangeDatePickerBuilder() = MaterialDatePicker.Builder.dateRangePicker()
        .setCalendarConstraints(buildConstraint())
        .setTitleText(R.string.select_date)
        .setPositiveButtonText(R.string.select)
        .build()

    fun getDatePickerBuilder() = MaterialDatePicker.Builder.datePicker()
        .setCalendarConstraints(buildConstraint())
        .setTitleText(R.string.select_date)
        .setPositiveButtonText(R.string.select)

    private fun buildConstraint() = CalendarConstraints.Builder()
        .setValidator(DateValidatorPointBackward.now())
        .build()
}
