package com.socialite.solite_pos.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.R
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme

@Composable
@ExperimentalComposeUiApi
fun BasicEditText(
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    passwordVisible: Boolean = false,
    readOnly: Boolean = false,
    value: String,
    placeHolder: String,
    isEnabled: Boolean = true,
    onValueChange: (String) -> Unit,
    onAction: (() -> Unit)? = null,
    onPasswordVisibility: (() -> Unit)? = null,
    suffixIcon: (@Composable () -> Unit)? = null
) {
    val shape = RoundedCornerShape(8.dp)
    val surfaceColor = if (isEnabled) MaterialTheme.colors.surface.copy(
        alpha = 0.3f
    ) else Color.Transparent
    Surface(
        modifier = Modifier
            .heightIn(min = 50.dp)
            .fillMaxWidth(),
        color = surfaceColor,
        shape = shape
    ) {
        val borderWidth = if (isEnabled) 0.5.dp else 0.dp
        Row(
            modifier = modifier
                .border(
                    width = borderWidth,
                    color = MaterialTheme.colors.onSurface,
                    shape = shape
                )
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            ) {
                BasicEditTextContent(
                    keyboardType = keyboardType,
                    imeAction = imeAction,
                    visualTransformation = visualTransformation,
                    passwordVisible = passwordVisible,
                    value = value,
                    readOnly = readOnly,
                    placeHolder = placeHolder,
                    isEnabled = isEnabled,
                    onValueChange = onValueChange,
                    onAction = onAction
                )
            }
            suffixIcon?.invoke() ?: SuffixIcon(
                keyboardType = keyboardType,
                passwordVisible = passwordVisible,
                value = value,
                isEnabled = isEnabled,
                onValueChange = onValueChange,
                onPasswordVisibility = onPasswordVisibility
            )
        }
    }
}

@Composable
private fun BoxScope.BasicEditTextContent(
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    visualTransformation: VisualTransformation,
    passwordVisible: Boolean,
    readOnly: Boolean = false,
    value: String,
    placeHolder: String,
    isEnabled: Boolean,
    onValueChange: (String) -> Unit,
    onAction: (() -> Unit)?,
) {
    if (isEnabled) {
        EnabledContent(
            visualTransformation = if (passwordVisible)
                VisualTransformation.None else visualTransformation,
            keyboardType = keyboardType,
            imeAction = imeAction,
            readOnly = readOnly,
            value = value,
            placeHolder = placeHolder,
            onAction = onAction,
            onValueChange = onValueChange
        )
    } else {
        DisabledContent(value = value, placeHolder = placeHolder)
    }
}

@Composable
private fun RowScope.SuffixIcon(
    keyboardType: KeyboardType,
    passwordVisible: Boolean,
    value: String,
    isEnabled: Boolean,
    onValueChange: (String) -> Unit,
    onPasswordVisibility: (() -> Unit)?,
) {
    if (isEnabled && value.isNotEmpty()) {
        if (keyboardType == KeyboardType.Password) {
            Icon(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        onPasswordVisibility?.invoke()
                    },
                painter = painterResource(
                    id = if (passwordVisible)
                        R.drawable.ic_visibility_on else R.drawable.ic_visibility_off
                ),
                tint = MaterialTheme.colors.onSurface,
                contentDescription = null
            )
        } else {
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
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
private fun BoxScope.EnabledContent(
    visualTransformation: VisualTransformation,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    readOnly: Boolean = false,
    value: String,
    placeHolder: String,
    onAction: (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
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
        readOnly = readOnly,
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

@Composable
private fun DisabledContent(
    value: String,
    placeHolder: String,
) {
    Column {
        Text(
            text = placeHolder,
            style = MaterialTheme.typography.overline.copy(
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = value,
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
@ExperimentalComposeUiApi
@Preview(showBackground = true)
private fun PreviewEnabled() {
    SolitePOSTheme {
        BasicEditText(
            value = "Siomay Ayam",
            placeHolder = "Produk",
            onValueChange = {},
            isEnabled = true
        )
    }
}

@Composable
@ExperimentalComposeUiApi
@Preview(showBackground = true)
private fun PreviewDisabled() {
    SolitePOSTheme {
        BasicEditText(
            value = "Siomay Ayam",
            placeHolder = "Produk",
            onValueChange = {},
            isEnabled = false
        )
    }
}
