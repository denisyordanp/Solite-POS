package com.socialite.solite_pos.view.screens.store.outcomes.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicEditText
import com.socialite.solite_pos.compose.PrimaryButtonView
import com.socialite.common.utility.helper.DateUtils
import com.socialite.schema.ui.helper.Outcome
import com.socialite.solite_pos.view.ui.ThousandAndSuggestionVisualTransformation

@Composable
@ExperimentalComposeUiApi
fun OutcomeDetail(
    date: String,
    isTodayDate: Boolean,
    timePicker: MaterialTimePicker.Builder,
    datePicker: MaterialDatePicker.Builder<Long>,
    outcome: Outcome? = null,
    fragmentManager: FragmentManager,
    onSubmitOutcome: (Outcome) -> Unit,
) {

    val isNewOutcome = outcome == null
    var selectedDateTime by remember { mutableStateOf(outcome?.date ?: date) }
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var total by remember { mutableStateOf(0L) }

    LaunchedEffect(key1 = "$date $outcome") {
        selectedDateTime = outcome?.date ?: date
        name = outcome?.name ?: ""
        desc = outcome?.desc ?: ""
        total = outcome?.total ?: 0L
    }

    fun submitOutcome(currentDate: String) {
        onSubmitOutcome(
            outcome?.copy(
                name = name,
                desc = desc,
                price = total,
                date = currentDate
            ) ?: Outcome.createNewOutcome(
                name = name,
                desc = desc,
                price = total,
                date = currentDate
            )
        )
        name = ""
        desc = ""
        total = 0L
    }

    fun selectTime() {
        val hourMinute = DateUtils.strToHourAndMinute(selectedDateTime)
        val picker = timePicker.setHour(hourMinute.first)
            .setMinute(hourMinute.second)
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .build()
        picker.addOnPositiveButtonClickListener {
            selectedDateTime = DateUtils.strDateTimeReplaceTime(
                dateTime = selectedDateTime,
                hour = picker.hour,
                minute = picker.minute
            )
            picker.dismiss()
            if (isNewOutcome) submitOutcome(selectedDateTime)
        }
        picker.show(fragmentManager, "")
    }

    fun selectDate() {
        val time = DateUtils.strToDate(selectedDateTime).time
        val picker =
            datePicker.setSelection(time)
                .build()
        picker.addOnPositiveButtonClickListener {
            val newDate = DateUtils.millisToDate(
                millis = it,
                isWithTime = true
            )
            selectedDateTime = DateUtils.strDateTimeReplaceDate(
                oldDate = selectedDateTime,
                newDate = newDate
            )
            picker.dismiss()
            selectTime()
        }
        picker.show(fragmentManager, "")
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        val title = if (isTodayDate || !isNewOutcome) {
            DateUtils.convertDateFromDb(
                date = selectedDateTime,
                format = DateUtils.DATE_WITH_DAY_AND_TIME_FORMAT
            )
        } else {
            DateUtils.convertDateFromDb(
                date = selectedDateTime,
                format = DateUtils.DATE_WITH_DAY_FORMAT
            )
        }
        Text(
            modifier = Modifier
                .run {
                     return@run if (!isNewOutcome) {
                         clickable {
                             selectDate()
                         }
                     } else { this }
                },
            text = title,
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        BasicEditText(
            value = name,
            placeHolder = stringResource(R.string.outcome_title),
            onValueChange = {
                name = it
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        BasicEditText(
            value = desc,
            placeHolder = stringResource(R.string.deskripsi_optional),
            onValueChange = {
                desc = it
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        BasicEditText(
            value = if (total == 0L) "" else total.toString(),
            keyboardType = KeyboardType.Number,
            visualTransformation = ThousandAndSuggestionVisualTransformation(false),
            placeHolder = stringResource(R.string.total_outcome),
            onValueChange = {
                total = it.toLongOrNull() ?: 0L
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButtonView(
            modifier = Modifier
                .fillMaxWidth(),
            buttonText = stringResource(
                id = if (isNewOutcome) R.string.adding else R.string.save
            ),
            isEnabled = name.isNotEmpty() && total != 0L,
            onClick = {
                if (isTodayDate || !isNewOutcome)
                    submitOutcome(selectedDateTime)
                else
                    selectTime()
            }
        )
    }
}
