package com.socialite.solite_pos.feature.customerorder.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.socialite.common.ui.component.MainBackground
import com.socialite.common.ui.component.SoliteContent
import com.socialite.common.ui.extension.contentSpace
import com.socialite.common.utility.constant.SuppressConstant
import com.socialite.core.ui.extension.bodyNormal
import com.socialite.core.ui.extension.defaultH5
import com.socialite.core.ui.extension.mainMenu
import com.socialite.core.ui.extension.paddings
import com.socialite.feature.customerorder.R
import com.socialite.solite_pos.feature.customerorder.component.SearchBar

@Composable
fun MainOrderScreen(
    mainStoreName: String,
    currentStoreName: String
) {
    MainBackground {
        val paddings = MaterialTheme.paddings

        var searchText by remember {
            mutableStateOf("")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(MaterialTheme.paddings.medium),
        ) {
            item {
                StoreInfo(
                    mainStoreName = mainStoreName,
                    currentStoreName = currentStoreName
                )
            }

            contentSpace(paddings.large)

            item {
                SearchMenu(
                    value = searchText,
                    onValueChange = { text ->
                        searchText = text
                    }
                )
            }
        }
    }
}

@Composable
private fun StoreInfo(
    mainStoreName: String,
    currentStoreName: String
) {
    Column {
        Text(
            text = mainStoreName,
            style = MaterialTheme.typography.defaultH5
        )
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.extraSmall))
        val text = buildAnnotatedString {
            append(stringResource(R.string.common_store_title))
            withStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(" $currentStoreName")
            }
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyNormal
        )
    }
}

@Composable
private fun SearchMenu(
    value: String,
    onValueChange: (value: String) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.search_menu_title),
            style = MaterialTheme.typography.mainMenu
        )
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.smallMedium))
        SearchBar(
            value = value,
            placeholder = stringResource(R.string.search_menu_placeholder),
            onValueChange = onValueChange
        )
    }
}

@Preview(showBackground = true)
@Suppress(SuppressConstant.SpellCheckingInspection)
@Composable
private fun Preview() {
    SoliteContent {
        MainOrderScreen(
            "Jajanan Sosialita",
            "Baros"
        )
    }
}