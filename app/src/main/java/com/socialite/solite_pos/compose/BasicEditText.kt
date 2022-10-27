package com.socialite.solite_pos.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.R

@Composable
@ExperimentalComposeUiApi
fun BasicEditText(
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    value: String,
    placeHolder: String,
    onValueChange: (String) -> Unit,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colors.onSurface,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = value,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                keyboardActions = KeyboardActions {
                    onAction?.invoke()
                },
                onValueChange = {
                    onValueChange(it)
                },
                textStyle = MaterialTheme.typography.body2.copy(
                    color = MaterialTheme.colors.onPrimary
                ),
                visualTransformation = visualTransformation,
                cursorBrush = SolidColor(
                    MaterialTheme.colors.onPrimary
                ),
            )
            if (value.isEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterStart),
                    text = placeHolder,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(
                        alpha = 0.6f
                    )
                )
            }
        }
        Icon(
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.CenterVertically)
                .clickable {
                    onValueChange("")
                },
            painter = painterResource(id = R.drawable.ic_close),
            tint = MaterialTheme.colors.onSurface,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}
