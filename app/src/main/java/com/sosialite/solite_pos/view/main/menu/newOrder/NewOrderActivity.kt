package com.sosialite.solite_pos.view.main.menu.newOrder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosialite.solite_pos.data.source.local.entity.Category
import com.sosialite.solite_pos.data.source.local.entity.helper.Order
import com.sosialite.solite_pos.databinding.ActivityNewOrderBinding
import com.sosialite.solite_pos.databinding.OrderListBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.currentTime
import com.sosialite.solite_pos.utils.tools.FragmentWithTitle
import com.sosialite.solite_pos.view.main.MainActivity
import com.sosialite.solite_pos.view.main.menu.CustomerNameActivity
import com.sosialite.solite_pos.view.main.menu.adapter.ItemOrderListAdapter
import com.sosialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter

class NewOrderActivity : AppCompatActivity() {

	private lateinit var _binding: ActivityNewOrderBinding
	private lateinit var adapter: ItemOrderListAdapter
	private lateinit var vpAdapter: ViewPagerAdapter
	private lateinit var _order: OrderListBinding

	companion object{
		const val RQ_CODE = 101
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityNewOrderBinding.inflate(layoutInflater)
		_order = _binding.newOrderList
		setContentView(_binding.root)

		vpAdapter = ViewPagerAdapter(supportFragmentManager)
		_binding.vpNewOrder.adapter = vpAdapter
		_binding.tabNewOrder.setupWithViewPager(_binding.vpNewOrder)

		adapter = ItemOrderListAdapter(ItemOrderListAdapter.ORDER)
		_order.rvOrderList.layoutManager = LinearLayoutManager(this)
		_order.rvOrderList.adapter = adapter

		_binding.btnNwBack.setOnClickListener { onBackPressed() }
		_order.btnOlCreate.setOnClickListener { setResult() }
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == CustomerNameActivity.RQ_COSTUMER){
			when(resultCode){
				RESULT_OK -> setContent()
				RESULT_CANCELED -> finish()
			}
		}
	}

	private fun setContent(){
		setPageAdapter()

		val order = Order("231234", currentTime)
		adapter.setItems(order)

		val no = "No. ${order.orderNo}"
		_order.tvOlNo.text = no
		_order.tvOlDate.text = order.timeString
	}

	private fun setPageAdapter(){
		val fragments: ArrayList<FragmentWithTitle> = ArrayList()
		fragments.add(
			FragmentWithTitle(
				"PROMO",
				ProductListFragment(Category(1, "PROMO")){ b, d ->
					Log.w("TESTINGDATA", "callback")
					if (b) adapter.addItem(d) else adapter.delItem(d)
				}
			)
		)
		fragments.add(
			FragmentWithTitle(
				"Dimsum",
				ProductListFragment(Category(2, "Dimsum")){ b, d ->
					if (b) adapter.addItem(d) else adapter.delItem(d)
				}
			)
		)
		fragments.add(
			FragmentWithTitle(
				"Mix Variant",
				ProductListFragment(Category(3, "Mix Variant")){ b, d ->
					if (b) adapter.addItem(d) else adapter.delItem(d)
				}
			)
		)
		fragments.add(
			FragmentWithTitle(
				"Minuman",
				ProductListFragment(Category(4, "Minuman")){ b, d ->
					if (b) adapter.addItem(d) else adapter.delItem(d)
				}
			)
		)
		fragments.add(
			FragmentWithTitle(
				"Extra",
				ProductListFragment(Category(5, "Extra")){ b, d ->
					if (b) adapter.addItem(d) else adapter.delItem(d)
				}
			)
		)

		vpAdapter.setData(fragments)
		_binding.vpNewOrder.offscreenPageLimit = vpAdapter.count
	}

	private fun setResult(){
		if (adapter.order != null){
			val data = Intent()
			data.putExtra(MainActivity.EXTRA_ORDER, adapter.sortedOrder)
			setResult(RESULT_OK, data)
			finish()
		}
	}
}
