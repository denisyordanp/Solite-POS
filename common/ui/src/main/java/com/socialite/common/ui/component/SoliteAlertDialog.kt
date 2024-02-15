package com.socialite.common.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun SoliteAlertDialog(
    titleText: String,
    descText: String? = null,
    descAnnotatedString: AnnotatedString? = null,
    positiveAction: () -> Unit,
    positiveText: String,
    negativeAction: (() -> Unit)? = null,
    negativeText: String? = null,
    onDismiss: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = titleText,
                    style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                descText?.let {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = it,
                        style = MaterialTheme.typography.body2.copy(
                            textAlign = TextAlign.Center
                        )
                    )
                }
                descAnnotatedString?.let {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = it,
                        style = MaterialTheme.typography.body2.copy(
                            textAlign = TextAlign.Center
                        )
                    )
                }
                Spacer(modifier = Modifier.height(25.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    if (negativeAction != null && negativeText != null) {
                        SolitePrimaryButton(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            buttonText = negativeText,
                            onClick = negativeAction,
                            isNegative = true
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    SolitePrimaryButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        buttonText = positiveText,
                        onClick = positiveAction
                    )
                }
            }
        }
    }
}
