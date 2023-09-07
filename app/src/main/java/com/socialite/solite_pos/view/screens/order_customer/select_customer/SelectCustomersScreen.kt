package com.socialite.solite_pos.view.screens.order_customer.select_customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.socialite.domain.schema.main.Customer
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicEditText
import com.socialite.solite_pos.compose.PrimaryButtonView

@Composable
@ExperimentalComposeUiApi
fun SelectCustomersScreen(
    currentViewModel: SelectCustomersViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onNewOrder: (
        customer: Customer,
        isTakeAway: Boolean
    ) -> Unit,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val state = currentViewModel.viewState.collectAsState().value
    var searchName by remember {
        mutableStateOf("")
    }
    var selectedId by remember {
        mutableStateOf(Customer.ID_ADD)
    }

    fun selectedCustomer(customer: Customer) {
        selectedId = if (customer.isAdd()) {
            val newCustomer = Customer.createNew(customer.name)
            currentViewModel.newCustomer(newCustomer)
            newCustomer.id
        } else {
            if (customer.id == selectedId)
                Customer.ID_ADD
            else
                customer.id
        }
    }

    Scaffold(
        topBar = {
            NameSearchBar(
                value = searchName,
                onBackClicked = onBackClicked,
                onSearch = {
                    searchName = it
                    currentViewModel.searchCustomer(it)
                }
            )
        },
        backgroundColor = MaterialTheme.colors.background
    ) { padding ->
        CustomerNames(
            modifier = Modifier
                .padding(padding),
            keyword = searchName,
            selectedId = selectedId,
            customers = state.getFilteredCustomers(),
            onClickName = {
                searchName = ""
                selectedCustomer(it)
                keyboard?.hide()
            },
            onChooseDine = { isTakeAway ->
                state.getFilteredCustomers().find { it.id == selectedId }?.let { customer ->
                    onNewOrder(customer, isTakeAway)
                }
            }
        )
    }
}

@Composable
@ExperimentalComposeUiApi
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
            tint = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.width(8.dp))
        BasicEditText(
            modifier = Modifier
                .padding(8.dp),
            value = value,
            placeHolder = stringResource(id = R.string.customer_name),
            onValueChange = onSearch
        )
    }
}

@Composable
private fun CustomerNames(
    modifier: Modifier,
    customers: List<Customer>,
    selectedId: String,
    keyword: String,
    onClickName: (Customer) -> Unit,
    onChooseDine: (isTakeAway: Boolean) -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .align(Alignment.TopCenter)
        ) {
            if (keyword.isNotEmpty() && !customers.isAnyMatches(keyword)) {
                item {
                    CustomerItem(
                        keyword = keyword,
                        selectedId = selectedId,
                        onCLickName = { selected ->
                            onClickName(selected)
                        }
                    )
                }
            }
            items(customers) {
                CustomerItem(
                    keyword = keyword,
                    customer = it,
                    selectedId = selectedId,
                    onCLickName = { selected ->
                        onClickName(selected)
                    }
                )
            }
        }

        SelectDineType(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            onChooseDine = onChooseDine
        )
    }
}

private fun List<Customer>.isAnyMatches(keyword: String): Boolean {
    return this.any {
        it.name.lowercase() == keyword
    }
}

@Composable
private fun CustomerItem(
    keyword: String,
    selectedId: String,
    customer: Customer? = null,
    onCLickName: (Customer) -> Unit
) {

    val isSelected = customer?.id == selectedId

    Spacer(modifier = Modifier.height(4.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCLickName(
                    customer ?: Customer.add(keyword)
                )
            }
            .background(
                color = MaterialTheme.colors.surface
            )
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier
                .weight(1f),
            text = customer?.name ?: keyword,
            style = MaterialTheme.typography.body2.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            ),
            color = MaterialTheme.colors.onSurface
        )
        if (customer == null || isSelected) {
            Icon(
                modifier = Modifier
                    .size(18.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(
                    id = if (isSelected) R.drawable.ic_done_all else R.drawable.ic_add
                ),
                tint = MaterialTheme.colors.onSurface,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun SelectDineType(
    modifier: Modifier = Modifier,
    onChooseDine: (isTakeAway: Boolean) -> Unit
) {
    Surface(
        modifier = modifier,
        elevation = 4.dp,
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ),
        color = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            PrimaryButtonView(
                modifier = Modifier.weight(1f),
                buttonText = stringResource(R.string.dine_in),
                onClick = {
                    onChooseDine(false)
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            PrimaryButtonView(
                modifier = Modifier.weight(1f),
                buttonText = stringResource(R.string.take_away),
                onClick = {
                    onChooseDine(true)
                }
            )
        }
    }
}
