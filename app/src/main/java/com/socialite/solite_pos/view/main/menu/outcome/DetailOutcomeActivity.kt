package com.socialite.solite_pos.view.main.menu.outcome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialite.solite_pos.databinding.ActivityDetailOutcomeBinding
import com.socialite.solite_pos.utils.config.DateUtils.Companion.convertDateFromDb
import com.socialite.solite_pos.utils.config.DateUtils.Companion.currentDateTime
import com.socialite.solite_pos.utils.config.DateUtils.Companion.dateWithDayFormat
import com.socialite.solite_pos.view.main.menu.adapter.OutcomeAdapter
import com.socialite.solite_pos.view.main.menu.bottom.DetailOutcomeFragment
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.MainViewModel.Companion.getMainViewModel
import com.socialite.solite_pos.vo.Status

class DetailOutcomeActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityDetailOutcomeBinding
    private lateinit var viewModel: MainViewModel
    lateinit var adapter: OutcomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailOutcomeBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        viewModel = getMainViewModel(this)

        adapter = OutcomeAdapter(supportFragmentManager)
        _binding.rvOutcome.layoutManager = LinearLayoutManager(this)
        _binding.rvOutcome.adapter = adapter

        setData()

        _binding.btnOcBack.setOnClickListener { onBackPressed() }
        _binding.fabNewOutcome.setOnClickListener {
            DetailOutcomeFragment().show(supportFragmentManager, "new_outcome")
        }
    }

    private fun setData() {
        _binding.tvOcDate.text = convertDateFromDb(currentDateTime, dateWithDayFormat)

        viewModel.getOutcome(currentDateTime).observe(this) {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    adapter.items = ArrayList(it.data)
                }
                Status.ERROR -> {
                }
            }
        }
    }
}