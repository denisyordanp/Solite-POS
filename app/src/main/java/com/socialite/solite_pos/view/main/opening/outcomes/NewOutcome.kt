package com.socialite.solite_pos.view.main.opening.outcomes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicEditText
import com.socialite.solite_pos.compose.PrimaryButtonView
import com.socialite.solite_pos.data.source.local.entity.room.master.Outcome
import com.socialite.solite_pos.view.main.opening.ui.ThousandAndSuggestionVisualTransformation
import okhttp3.internal.toLongOrDefault

@Composable
@ExperimentalComposeUiApi
fun NewOutcome(
    date: String,
    onCreateOutcome: (Outcome) -> Unit
) {

    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var total by remember { mutableStateOf(0L) }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
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
                total = it.toLongOrDefault(0L)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButtonView(
            modifier = Modifier
                .fillMaxWidth(),
            buttonText = stringResource(R.string.add_outcome),
            onClick = {
                onCreateOutcome(
                    Outcome(
                        name = name,
                        desc = desc,
                        price = total,
                        date = date
                    )
                )
                name = ""
                desc = ""
                total = 0L
            }
        )
    }
}
