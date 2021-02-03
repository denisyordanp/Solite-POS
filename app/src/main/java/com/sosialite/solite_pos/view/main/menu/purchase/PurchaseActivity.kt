package com.sosialite.solite_pos.view.main.menu.purchase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosialite.solite_pos.databinding.ActivityPurchaseBinding
import com.sosialite.solite_pos.databinding.OrderListBinding
import com.sosialite.solite_pos.utils.config.MainConfig
import com.sosialite.solite_pos.view.main.menu.adapter.ItemOrderListAdapter
import com.sosialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class PurchaseActivity : AppCompatActivity() {

	private lateinit var _binding: ActivityPurchaseBinding
	private lateinit var adapter: ItemOrderListAdapter
	private lateinit var vpAdapter: ViewPagerAdapter
	private lateinit var _order: OrderListBinding
	private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
		_binding = ActivityPurchaseBinding.inflate(layoutInflater)
		_order = _binding.purchaseList
        setContentView(_binding.root)

		viewModel = MainConfig.getViewModel(this)

		vpAdapter = ViewPagerAdapter(this)
		_binding.vpPurchase.adapter = vpAdapter

		_order.rvOrderList.layoutManager = LinearLayoutManager(this)

		_binding.btnPcBack.setOnClickListener { onBackPressed() }
		_order.btnOlCreate.setOnClickListener { setDine() }
    }
}
