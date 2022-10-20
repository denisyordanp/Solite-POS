package com.socialite.solite_pos.view.main.opening.recap

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.data.source.local.entity.helper.Income
import com.socialite.solite_pos.data.source.local.entity.helper.RecapData
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.utils.config.thousand
import com.socialite.solite_pos.view.dialog.DatePickerFragment
import com.socialite.solite_pos.view.main.opening.ui.OrderMenus
import com.socialite.solite_pos.view.main.opening.ui.theme.SolitePOSTheme
import com.socialite.solite_pos.view.viewModel.OrderViewModel

class RecapActivity : AppCompatActivity() {

    private lateinit var orderViewModel: OrderViewModel

    private lateinit var datePicker: DatePickerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderViewModel = OrderViewModel.getOrderViewModel(this)

        datePicker = DatePickerFragment.createInstance()

        setContent {
            SolitePOSTheme {
                RecapMainView()
            }
        }
    }

    @Composable
    private fun RecapMainView() {
        Scaffold(
            topBar = {
                BasicTopBar(
                    titleText = stringResource(id = R.string.sales_recap),
                    onBackClicked = {
                        onBackPressed()
                    }
                )
            },
            content = { padding ->
                RecapContent(
                    modifier = Modifier
                        .padding(padding)
                )
            }
        )
    }

    @Composable
    private fun RecapContent(
        modifier: Modifier = Modifier
    ) {

        val date = DateUtils.currentDate

        var selectedDate by remember {
            mutableStateOf(
                Pair(
                    date,
                    date
                )
            )
        }

        val recap = orderViewModel.getIncomes(selectedDate.first, selectedDate.second)
            .collectAsState(initial = RecapData.empty())

        LazyColumn(
            modifier = modifier
        ) {
            item {
                DateSelection(
                    selectedDateFrom = selectedDate.first,
                    selectedDateUntil = selectedDate.second,
                    onFromDateClicked = {
                        datePicker.show(
                            manager = supportFragmentManager,
                            selectedDate = selectedDate.first,
                            callback = {
                                selectedDate = selectedDate.copy(
                                    first = it
                                )
                            }
                        )
                    },
                    onUntilDateClicked = {
                        datePicker.show(
                            manager = supportFragmentManager,
                            selectedDate = selectedDate.second,
                            callback = {
                                selectedDate = selectedDate.copy(
                                    second = it
                                )
                            }
                        )
                    }
                )
            }
            item {
                OrdersMenuItem(selectedDate.first)
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(color = Color.White)
                )
            }
            items(recap.value.incomes) {
                SalesRecap(it)
            }
            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .background(color = Color.White)
                )
            }
            item {
                TotalRecap(recap.value)
            }
        }
    }

    @Composable
    private fun DateSelection(
        selectedDateFrom: String,
        selectedDateUntil: String,
        onFromDateClicked: () -> Unit,
        onUntilDateClicked: () -> Unit,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(4.dp),
                    text = stringResource(R.string.from),
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier
                        .padding(4.dp),
                    text = stringResource(R.string.until),
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colors.background,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable {
                            onFromDateClicked()
                        }
                        .padding(4.dp),
                    text = DateUtils.convertDateFromDate(selectedDateFrom, DateUtils.DATE_WITH_DAY_FORMAT),
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colors.background,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable {
                            onUntilDateClicked()
                        }
                        .padding(4.dp),
                    text = DateUtils.convertDateFromDate(selectedDateUntil, DateUtils.DATE_WITH_DAY_FORMAT),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }

    @Composable
    private fun OrdersMenuItem(
        date: String
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(16.dp),
        ) {
            MenuItem(OrderMenus.CURRENT_ORDER, date)
            Spacer(modifier = Modifier.height(2.dp))
            MenuItem(OrderMenus.NOT_PAY_YET, date)
            Spacer(modifier = Modifier.height(2.dp))
            MenuItem(OrderMenus.CANCELED, date)
            Spacer(modifier = Modifier.height(2.dp))
            MenuItem(OrderMenus.DONE, date)
        }
        Spacer(modifier = Modifier.height(4.dp))
    }

    @Composable
    private fun SalesRecap(income: Income) {
        Column(
            modifier = Modifier
                .background(color = Color.White)
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = income.dateString(),
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = "Rp.",
                    style = MaterialTheme.typography.body2.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = income.total.thousand(),
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
                .height(1.dp)
                .background(color = Color.White)
                .padding(horizontal = 16.dp)
                .background(color = MaterialTheme.colors.onBackground)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            val style = MaterialTheme.typography.body2
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "Penjualan tunai",
                    style = style
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Penjualan non tunai",
                    style = style
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Pengeluaran",
                    style = style
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Penjualan bersih",
                    style = style
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Total penjualan",
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
        menu: OrderMenus,
        date: String
    ) {

        val amount = orderViewModel.getOrderList(menu.status, date)
            .collectAsState(initial = emptyList())

        Row {
            val text = when (menu) {
                OrderMenus.CURRENT_ORDER -> "Pesanan belum selesai"
                OrderMenus.NOT_PAY_YET -> "Pesanan belum melakukan pembayaran"
                OrderMenus.CANCELED -> "Pesanan dibatalkan"
                OrderMenus.DONE -> "Pesanan sudah bayar"
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
}

