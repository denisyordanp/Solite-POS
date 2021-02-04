package com.sosialite.solite_pos.view.main.menu.purchase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.sosialite.solite_pos.data.source.local.entity.room.master.Category
import com.sosialite.solite_pos.databinding.ActivityPurchaseBinding
import com.sosialite.solite_pos.databinding.OrderListBinding
import com.sosialite.solite_pos.utils.config.MainConfig
import com.sosialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.sosialite.solite_pos.view.main.menu.adapter.ItemOrderListAdapter
import com.sosialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter
import com.sosialite.solite_pos.view.main.menu.master.bottom.DetailOrderProductFragment
import com.sosialite.solite_pos.view.main.menu.order.ProductOrderFragment
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

		adapter = ItemOrderListAdapter(ItemOrderListAdapter.PURCHASE)

		_order.btnOlCreate.text = "Buat Pembelian"
		_order.rvOrderList.adapter = adapter
		_order.rvOrderList.layoutManager = LinearLayoutManager(this)

		setPageAdapter()

		_binding.btnPcBack.setOnClickListener { onBackPressed() }
//		_order.btnOlCreate.setOnClickListener { setDine() }
    }

	private fun setPageAdapter(){
		viewModel.getCategories(Category.getFilter(Category.ACTIVE, true)).observe(this, {
			if (!it.isNullOrEmpty()){
				val fragments: ArrayList<FragmentWithTitle> = ArrayList()
				for (ctg in it){
					val fragment = ProductOrderFragment(DetailOrderProductFragment.PURCHASE, ctg) { p ->
						adapter.addItem(p)
					}
					fragments.add(FragmentWithTitle(ctg.name, fragment))
				}

				setData(fragments)
			}
		})
	}

	private fun setData(fragments: ArrayList<FragmentWithTitle>){
		TabLayoutMediator(_binding.tabPurchase, _binding.vpPurchase) { tab, position ->
			tab.text = fragments[position].title
			_binding.vpPurchase.setCurrentItem(tab.position, true)
		}.attach()
		vpAdapter.setData(fragments)
		_binding.vpPurchase.offscreenPageLimit = vpAdapter.itemCount
	}
}
