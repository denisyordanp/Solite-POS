package com.socialite.solite_pos.view.outcomes

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
import com.google.android.material.timepicker.MaterialTimePicker
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicEditText
import com.socialite.solite_pos.compose.PrimaryButtonView
import com.socialite.solite_pos.data.source.local.entity.room.master.Outcome
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.view.ui.ThousandAndSuggestionVisualTransformation

@Composable
@ExperimentalComposeUiApi
fun NewOutcome(
    date: String,
    isTodayDate: Boolean,
    timePicker: MaterialTimePicker.Builder,
    fragmentManager: FragmentManager,
    onCreateOutcome: (Outcome) -> Unit
) {

    var selectedDateTime by remember { mutableStateOf(date) }
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var total by remember { mutableStateOf(0L) }
    LaunchedEffect(key1 = date) {
        selectedDateTime = date
        name = ""
        desc = ""
        total = 0L
    }

    fun createOutcome(currentDate: String) {
        onCreateOutcome(
            Outcome(
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
            createOutcome(selectedDateTime)
        }
        picker.show(fragmentManager, "")
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        val title = if (isTodayDate) {
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
            buttonText = stringResource(R.string.add_outcome),
            onClick = {
                if (isTodayDate)
                    createOutcome(selectedDateTime)
                else
                    selectTime()
            }
        )
    }
}
