package com.socialite.solite_pos.view.store.recap

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.compose.basicDropdown
import com.socialite.solite_pos.data.source.local.entity.helper.RecapData
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Store
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.utils.config.thousand
import com.socialite.solite_pos.utils.tools.helper.ReportsParameter
import com.socialite.solite_pos.view.ui.OrderMenus
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel

@Composable
fun RecapMainView(
    mainViewModel: MainViewModel,
    orderViewModel: OrderViewModel,
    datePicker: MaterialDatePicker<androidx.core.util.Pair<Long, Long>>,
    fragmentManager: FragmentManager,
    onOrdersClicked: (parameters: ReportsParameter) -> Unit,
    onOutcomesClicked: (parameters: ReportsParameter) -> Unit,
    onBackClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            BasicTopBar(
                titleText = stringResource(id = R.string.sales_recap),
                onBackClicked = onBackClicked
            )
        },
        content = { padding ->
            RecapContent(
                modifier = Modifier
                    .padding(padding),
                mainViewModel = mainViewModel,
                orderViewModel = orderViewModel,
                datePicker = datePicker,
                fragmentManager = fragmentManager,
                onOrdersClicked = onOrdersClicked,
                onOutcomesClicked = onOutcomesClicked
            )
        }
    )
}

@Composable
private fun RecapContent(
    modifier: Modifier = Modifier,
    orderViewModel: OrderViewModel,
    mainViewModel: MainViewModel,
    datePicker: MaterialDatePicker<androidx.core.util.Pair<Long, Long>>,
    fragmentManager: FragmentManager,
    onOrdersClicked: (parameters: ReportsParameter) -> Unit,
    onOutcomesClicked: (parameters: ReportsParameter) -> Unit,
) {

    val date = DateUtils.currentDate

    var storeExpanded by remember {
        mutableStateOf(false)
    }

    var selectedDate by remember {
        mutableStateOf(
            Pair(
                date,
                date
            )
        )
    }

    var selectedStore by remember {
        mutableStateOf<Store?>(null)
    }

    val stores = mainViewModel.getStores().collectAsState(initial = emptyList())

    val parameters = ReportsParameter(
        start = selectedDate.first,
        end = selectedDate.second,
        storeId = selectedStore?.id ?: ""
    )
    val recap = orderViewModel.getIncomes(parameters)
        .collectAsState(initial = RecapData.empty())

    LazyColumn(
        modifier = modifier
    ) {
        item {
            DateSelection(
                selectedDateFrom = selectedDate.first,
                selectedDateUntil = selectedDate.second,
                onDateClicked = {
                    datePicker.addOnPositiveButtonClickListener {
                        val start = DateUtils.millisToDate(it.first)
                        val end = DateUtils.millisToDate(it.second)
                        selectedDate = Pair(
                            start,
                            end
                        )
                    }
                    datePicker.show(fragmentManager, "")
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        basicDropdown(
            isExpanded = storeExpanded,
            title = R.string.select_store,
            selectedItem = selectedStore?.name,
            items = stores.value,
            onHeaderClicked = {
                storeExpanded = !storeExpanded
            },
            onSelectedItem = {
                selectedStore = it as Store
                storeExpanded = false
            }
        )

        selectedStore?.let {
            item {
                OrdersMenuItem(
                    orderViewModel = orderViewModel,
                    parameters = ReportsParameter(
                        start = selectedDate.first,
                        end = selectedDate.second,
                        storeId = it.id
                    ),
                    onOrdersClicked = onOrdersClicked
                )
            }
        }

        if (recap.value.incomes.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.surface)
                        .padding(16.dp)
                ) {
                    recap.value.incomes.forEach {
                        RecapItem(
                            firstText = it.dateString(),
                            middleText = it.payment,
                            amount = it.total,
                            isAdd = true
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        if (recap.value.outcomes.isNotEmpty() && selectedStore != null) {
            item {
                Column(
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.surface)
                        .clickable {
                            onOutcomesClicked(
                                ReportsParameter(
                                    start = selectedDate.first,
                                    end = selectedDate.second,
                                    storeId = selectedStore!!.id
                                )
                            )
                        }
                        .padding(16.dp)
                ) {
                    recap.value.outcomes.forEach {
                        RecapItem(
                            firstText = it.dateString(),
                            middleText = it.name,
                            amount = it.total,
                            isAdd = false
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        selectedStore?.let {
            item {
                TotalRecap(recap.value)
            }
        }
    }
}

@Composable
private fun DateSelection(
    selectedDateFrom: String,
    selectedDateUntil: String,
    onDateClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
            .clickable {
                onDateClicked()
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.from),
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.until),
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = DateUtils.convertDateFromDate(
                    selectedDateFrom,
                    DateUtils.DATE_WITH_DAY_FORMAT
                ),
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = DateUtils.convertDateFromDate(
                    selectedDateUntil,
                    DateUtils.DATE_WITH_DAY_FORMAT
                ),
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
private fun OrdersMenuItem(
    orderViewModel: OrderViewModel,
    parameters: ReportsParameter,
    onOrdersClicked: (parameters: ReportsParameter) -> Unit,
) {
    Spacer(modifier = Modifier.height(4.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onOrdersClicked(parameters)
            }
            .background(color = MaterialTheme.colors.surface)
            .padding(16.dp),
    ) {
        MenuItem(
            orderViewModel = orderViewModel,
            menu = OrderMenus.CURRENT_ORDER,
            parameters = parameters
        )
        Spacer(modifier = Modifier.height(2.dp))
        MenuItem(
            orderViewModel = orderViewModel,
            menu = OrderMenus.NOT_PAY_YET,
            parameters = parameters
        )
        Spacer(modifier = Modifier.height(2.dp))
        MenuItem(
            orderViewModel = orderViewModel,
            menu = OrderMenus.CANCELED,
            parameters = parameters
        )
        Spacer(modifier = Modifier.height(2.dp))
        MenuItem(
            orderViewModel = orderViewModel,
            menu = OrderMenus.DONE,
            parameters = parameters
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun RecapItem(
    firstText: String,
    middleText: String,
    amount: Long,
    isAdd: Boolean
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
                text = firstText,
                style = MaterialTheme.typography.body2
            )
            Text(
                modifier = Modifier
                    .weight(1f),
                text = middleText,
                style = MaterialTheme.typography.body2
            )
            val operation = if (isAdd) "+" else "-"
            Text(
                text = "${operation}Rp.",
                style = MaterialTheme.typography.body2.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = amount.thousand(),
                style = MaterialTheme.typography.body2.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun TotalRecap(recapData: RecapData) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
            .background(color = MaterialTheme.colors.surface)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
            .padding(16.dp)
    ) {
        val style = MaterialTheme.typography.body2
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = stringResource(R.string.cash_sales),
                style = style
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.non_cash_sales),
                style = style
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.outcome),
                style = style
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.gross_sales),
                style = style
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.total_sales),
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "Rp.",
                style = style
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Rp.",
                style = style
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Rp.",
                style = style
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Rp.",
                style = style
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Rp.",
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Column(
            horizontalAlignment = Alignment.End
        ) {
            // Total cash
            Text(
                text = recapData.totalCash.thousand(),
                style = style
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Total non cash
            Text(
                text = recapData.totalNonCash.thousand(),
                style = style
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Total outcomes
            Text(
                text = recapData.totalOutcomes.thousand(),
                style = style
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Nett gross
            Text(
                text = recapData.grossIncome.thousand(),
                style = style
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Total sales
            Text(
                text = recapData.totalIncomes.thousand(),
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun MenuItem(
    orderViewModel: OrderViewModel,
    menu: OrderMenus,
    parameters: ReportsParameter
) {

    val amount = orderViewModel.getOrderList(menu.status, parameters)
        .collectAsState(initial = emptyList())

    Row {
        val text = when (menu) {
            OrderMenus.CURRENT_ORDER -> stringResource(R.string.undone_orders)
            OrderMenus.NOT_PAY_YET -> stringResource(R.string.not_pay_yet_orders)
            OrderMenus.CANCELED -> stringResource(R.string.canceled_orders)
            OrderMenus.DONE -> stringResource(R.string.payed_orders)
        }
        Text(
            text = amount.value.size.toString(),
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = text,
            style = MaterialTheme.typography.body1
        )
    }
}
