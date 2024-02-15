package com.socialite.solite_pos.feature.customerorder.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.socialite.common.ui.component.PrimaryButton
import com.socialite.common.ui.component.SoliteContent
import com.socialite.core.ui.extension.paddings
import com.socialite.feature.customerorder.R
import com.socialite.schema.ui.dummy.DummySchema
import com.socialite.schema.ui.main.Customer
import com.socialite.solite_pos.feature.customerorder.component.CustomerItem
import com.socialite.solite_pos.feature.customerorder.component.SearchTopBar

@Composable
fun SelectCustomerScreen(
    modifier: Modifier = Modifier,
    customers: List<Customer>,
    onBackClick: () -> Unit,
    onServeClick: (isTakeAway: Boolean, customer: Customer) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        var selectedCustomer by remember {
            mutableStateOf<Customer?>(null)
        }

        Column(
            modifier = modifier
                .align(Alignment.TopCenter)
        ) {
            var searchValue by remember { mutableStateOf("") }

            SearchTopBar(
                searchValue = searchValue,
                searchPlaceHolder = stringResource(id = R.string.search_customer_placeholder),
                onSearchValueChange = { newValue ->
                    searchValue = newValue
                },
                onBackClick = onBackClick
            )

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.paddings.medium),
                contentPadding = PaddingValues(top = MaterialTheme.paddings.medium)
            ) {
                items(
                    items = customers,
                    key = { c -> c.id }
                ) { customer ->
                    CustomerItem(
                        name = customer.name,
                        isSelected = selectedCustomer == customer,
                        onClick = {
                            selectedCustomer = customer
                        }
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.paddings.smallMedium))
                }
            }
        }

        ServeOption(
            modifier = Modifier.align(Alignment.BottomCenter),
            isEnabled = selectedCustomer != null,
            onServeClick = { isTakeAway ->
                selectedCustomer?.let {
                    onServeClick(isTakeAway, it)
                }
            }
        )
    }
}

@Composable
private fun ServeOption(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    onServeClick: (isTakeAway: Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(75.dp)
            .background(MaterialTheme.colors.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PrimaryButton(
            modifier = Modifier
                .weight(1f)
                .padding(start = MaterialTheme.paddings.medium),
            text = stringResource(R.string.dine_in_title),
            isEnabled = isEnabled,
            onClick = {
                onServeClick(false)
            }
        )
        Spacer(modifier = Modifier.width(MaterialTheme.paddings.medium))
        PrimaryButton(
            modifier = Modifier
                .weight(1f)
                .padding(end = MaterialTheme.paddings.medium),
            text = stringResource(R.string.take_away_title),
            isEnabled = isEnabled,
            onClick = {
                onServeClick(true)
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    SoliteContent {
        SelectCustomerScreen(
            modifier = it,
            customers = DummySchema.customers,
            onBackClick = {},
            onServeClick = { _, _ -> }
        )
    }
}