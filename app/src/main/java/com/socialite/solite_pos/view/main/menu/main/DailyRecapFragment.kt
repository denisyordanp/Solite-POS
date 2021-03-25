package com.socialite.solite_pos.view.main.menu.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.RecapData
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.databinding.FragmentDailyRecapBinding
import com.socialite.solite_pos.utils.config.DateUtils.Companion.convertDateFromDate
import com.socialite.solite_pos.utils.config.DateUtils.Companion.currentDate
import com.socialite.solite_pos.utils.config.DateUtils.Companion.dateWithDayFormat
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah
import com.socialite.solite_pos.view.main.menu.adapter.RecapAdapter
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import com.socialite.solite_pos.vo.Status

class DailyRecapFragment(private var queryDate: String) : Fragment() {

    private lateinit var _binding: FragmentDailyRecapBinding
    private lateinit var outcomeRecapAdapter: RecapAdapter
    private lateinit var incomeRecapAdapter: RecapAdapter
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var viewModel: MainViewModel

    constructor() : this(currentDate)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDailyRecapBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {

            orderViewModel = OrderViewModel.getOrderViewModel(activity!!)
            viewModel = MainViewModel.getMainViewModel(activity!!)

            setDate(queryDate)
            setUpAdapter()

        }
    }

    private fun setUpAdapter() {
        outcomeRecapAdapter = RecapAdapter()
        _binding.rvRcOutcome.layoutManager = LinearLayoutManager(activity)
        _binding.rvRcOutcome.adapter = outcomeRecapAdapter

        incomeRecapAdapter = RecapAdapter()
        _binding.rvRcIncome.layoutManager = LinearLayoutManager(activity)
        _binding.rvRcIncome.adapter = incomeRecapAdapter
    }

    fun setDate(newDate: String) {
        queryDate = newDate
        setTitle(queryDate)
        getRecap()
    }

    private fun setTitle(date: String) {
        _binding.tvRcDate.text = convertDateFromDate(date, dateWithDayFormat)
    }

    private fun getRecap() {
        getIncome()
        getOutCome()
    }

    private fun getIncome() {
        orderViewModel.getOrderList(Order.DONE, queryDate).observe(activity!!) { orders ->
            when (orders.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    if (!orders.data.isNullOrEmpty()) {
                        getProductOrder(orders.data)
                    }
                }
                Status.ERROR -> {}
            }
        }
    }

    private fun getProductOrder(ordersData: List<OrderData>) {
        val incomes: ArrayList<RecapData> = ArrayList()
        for (item in ordersData) {
            val orderWithProduct = OrderWithProduct(item)
            orderViewModel.getProductOrder(item.order.orderNo).observe(activity!!) { products ->
                when (products.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        if (!products.data.isNullOrEmpty()) {
                            orderWithProduct.products = products.data
                            incomes.add(
                                    RecapData(
                                            orderWithProduct.order.order.orderNo,
                                            orderWithProduct.order.payment?.name,
                                            orderWithProduct.grandTotal,
                                            orderWithProduct.order.payment?.isCash
                                    ))
                        }
                        incomeRecapAdapter.items = incomes
                        setData()
                    }
                    Status.ERROR -> {
                    }
                }
            }
        }
    }

    private fun getOutCome(){
        viewModel.getOutcome(queryDate).observe(activity!!) {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    val outcomes: ArrayList<RecapData> = ArrayList()
                    if (it.data != null) {
                        for (item in it.data) {
                            outcomes.add(RecapData("${item.amount}x", item.name, item.total, null))
                        }
                    }
                    outcomeRecapAdapter.items = outcomes
                    setData()
                }
                Status.ERROR -> {}
            }
        }
    }

    private fun setData(){
        val incomeCash = incomeRecapAdapter.getIncome(true)
        val outcome = outcomeRecapAdapter.grandTotal
        val incomeNonCash = incomeRecapAdapter.getIncome(false)
        _binding.tvRcIncomeCash.text = toRupiah(incomeCash)
        _binding.tvRcTotalOutcome.text = toRupiah(outcome)
        _binding.tvRcTotalCash.text = toRupiah(incomeCash - outcome)
        _binding.tvRcIncomeNonCash.text = toRupiah(incomeNonCash)
        _binding.tvRcGrandTotal.text =
            toRupiah(incomeRecapAdapter.grandTotal - outcomeRecapAdapter.grandTotal)
    }
}