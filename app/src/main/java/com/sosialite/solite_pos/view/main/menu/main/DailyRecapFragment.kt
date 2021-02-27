package com.sosialite.solite_pos.view.main.menu.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosialite.solite_pos.data.source.local.entity.helper.RecapData
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import com.sosialite.solite_pos.databinding.FragmentDailyRecapBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.currentDate
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.dateFormat
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.sdFormat
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.toRupiah
import com.sosialite.solite_pos.view.main.menu.adapter.RecapAdapter
import com.sosialite.solite_pos.view.viewmodel.MainViewModel
import com.sosialite.solite_pos.vo.Status

class DailyRecapFragment : Fragment() {

    private lateinit var _binding: FragmentDailyRecapBinding
    private lateinit var outcomeRecapAdapter: RecapAdapter
    private lateinit var incomeRecapAdapter: RecapAdapter
    private lateinit var viewModel: MainViewModel

    companion object{
        private val TAG = DailyRecapFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyRecapBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null){
            outcomeRecapAdapter = RecapAdapter()
            _binding.rvRcOutcome.layoutManager = LinearLayoutManager(activity)
            _binding.rvRcOutcome.adapter = outcomeRecapAdapter

            incomeRecapAdapter = RecapAdapter()
            _binding.rvRcIncome.layoutManager = LinearLayoutManager(activity)
            _binding.rvRcIncome.adapter = incomeRecapAdapter

            viewModel = getViewModel(activity!!)

            _binding.tvRcDate.text = dateFormat(currentDate, sdFormat)

            getIncome()
            getOutCome()
        }
    }

    private fun getIncome(){
        viewModel.getOrderDetail(Order.DONE, currentDate).observe(activity!!){
            when(it.status){
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()){
                        val incomes: ArrayList<RecapData>  = ArrayList()
                        for (item in it.data){
                            incomes.add(RecapData(item.order.orderNo, item.grandTotal))
                        }
                        incomeRecapAdapter.items = incomes
                    }
                    setData()
                }
                Status.ERROR -> {}
            }
        }
    }

    private fun getOutCome(){
        viewModel.getOutcome(currentDate).observe(activity!!){
            when(it.status){
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    val outcomes: ArrayList<RecapData>  = ArrayList()
                    if (it.data != null){
                        for (item in it.data){
                            outcomes.add(RecapData("${item.amount}x ${item.name}", item.total))
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
        _binding.tvRcTotalIncome.text = toRupiah(incomeRecapAdapter.grandTotal)
        _binding.tvRcTotalOutcome.text = toRupiah(outcomeRecapAdapter.grandTotal)
        _binding.tvRcGrandTotal.text =
            toRupiah(incomeRecapAdapter.grandTotal - outcomeRecapAdapter.grandTotal)
    }
}