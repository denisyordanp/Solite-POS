package com.socialite.solite_pos.view.main.opening.order_customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.PrimaryButton
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.view.viewModel.MainViewModel

@Composable
fun OrderCustomerName(
    viewModel: MainViewModel,
    onBackClicked: () -> Unit,
    onCLickName: (Customer) -> Unit
) {

    var searchName by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            NameSearchBar(
                value = searchName,
                onBackClicked = onBackClicked,
                onSearch = {
                    searchName = it
                }
            )
        },
        bottomBar = {
            SelectDineType()
        },
        backgroundColor = MaterialTheme.colors.background
    ) { padding ->

        CustomerNames(
            modifier = Modifier
                .padding(padding),
            viewModel = viewModel,
            keyword = searchName,
            onCLickName = onCLickName
        )
    }
}

@Composable
private fun NameSearchBar(
    value: String,
    onBackClicked: () -> Unit,
    onSearch: (keyword: String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = MaterialTheme.colors.primary
            )
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable { onBackClicked() }
                .padding(start = 8.dp)
                .padding(8.dp),
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = null,
            tint = MaterialTheme.colors.onPrimary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(
                    width = 0.5.dp,
                    color = MaterialTheme.colors.onPrimary,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = value,
                onValueChange = {
                    onSearch(it)
                },
                textStyle = MaterialTheme.typography.body2.copy(
                    color = Color.Black
                ),
            )
            if (value.isEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterStart),
                    text = stringResource(R.string.customer_name),
                    style = MaterialTheme.typography.body2,
                    color = Color.Black.copy(
                        alpha = 0.6f
                    )
                )
            }
        }
    }
}

@Composable
private fun CustomerNames(
    modifier: Modifier,
    viewModel: MainViewModel,
    keyword: String,
    onCLickName: (Customer) -> Unit
) {

    val customers by viewModel.filterCustomer(keyword)
        .collectAsState(initial = emptyList())

    LazyColumn(
        modifier = modifier
    ) {
        if (!customers.isNullOrEmpty()) {
            items(customers!!) {
                CustomerItem(
                    keyword = keyword,
                    customer = it,
                    onCLickName = onCLickName
                )
            }
        } else {
            item {
                CustomerItem(
                    keyword = keyword,
                    onCLickName = {}
                )
            }
        }
    }
}

@Composable
private fun CustomerItem(
    keyword: String,
    customer: Customer? = null,
    onCLickName: (Customer) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCLickName(
                    customer ?: Customer.add(keyword)
                )
            }
            .background(
                color = Color.White
            )
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier
                .weight(1f),
            text = customer?.name ?: keyword,
            style = MaterialTheme.typography.body2.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color.Black
        )
        if (customer == null) {
            Icon(
                modifier = Modifier
                    .size(18.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_add),
                tint = MaterialTheme.colors.primary,
                contentDescription = null
            )
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun SelectDineType() {
    Box(
        modifier = Modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            PrimaryButton(
                modifier = Modifier.weight(1f),
                buttonText = stringResource(R.string.dine_in),
                onClick = {}
            )
            Spacer(modifier = Modifier.width(16.dp))
            PrimaryButton(
                modifier = Modifier.weight(1f),
                buttonText = stringResource(R.string.take_away),
                onClick = {}
            )
        }
    }
}
