package com.socialite.solite_pos.compose

import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun <T> GeneralDropdown(
    modifier: Modifier = Modifier,
    label: String,
    items: List<GeneralDropdownItem<T>>,
    selectedItem: GeneralDropdownItem<T>?,
    onSelectedItem: (GeneralDropdownItem<T>) -> Unit,
    onExpanded: (() -> Unit)? = null
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {
            if (it) onExpanded?.invoke()
            isExpanded = !isExpanded
        },
        content = {
            BasicEditText(
                modifier = modifier,
                value = selectedItem?.name ?: "",
                placeHolder = label,
                onValueChange = {},
                readOnly = true,
                suffixIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                }
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = {
                    isExpanded = false
                },
                content = {
                    items.forEach {
                        DropdownMenuItem(
                            content = {
                                Text(text = it.name)
                            },
                            onClick = {
                                onSelectedItem(it)
                                isExpanded = false
                            }
                        )
                    }
                }
            )
        }
    )
}

data class GeneralDropdownItem<T>(
    val name: String,
    val value: T
)
