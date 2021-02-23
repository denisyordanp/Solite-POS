package com.sosialite.solite_pos.view.main.menu.outcome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosialite.solite_pos.databinding.ActivityDetailOutcomeBinding
import com.sosialite.solite_pos.utils.config.MainConfig
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.currentDate
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.dateFormat
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.sdFormat
import com.sosialite.solite_pos.view.main.menu.adapter.OutcomeAdapter
import com.sosialite.solite_pos.view.main.menu.bottom.DetailOutcomeFragment
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class DetailOutcomeActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityDetailOutcomeBinding
    private lateinit var adapter: OutcomeAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailOutcomeBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        viewModel = MainConfig.getViewModel(this)

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
        _binding.tvOcDate.text = dateFormat(currentDate, sdFormat)

        viewModel.getOutcome(currentDate).observe(this){
            adapter.items = ArrayList(it)
        }
    }
}