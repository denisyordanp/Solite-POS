package com.sosialite.solite_pos.view.main.menu.order

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosialite.solite_pos.data.source.local.entity.main.Customer
import com.sosialite.solite_pos.data.source.local.entity.helper.Order
import com.sosialite.solite_pos.databinding.ActivityOrderBinding
import com.sosialite.solite_pos.databinding.OrderListBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.currentTime
import com.sosialite.solite_pos.utils.tools.OrderNo
import com.sosialite.solite_pos.utils.tools.helper.DataDummy
import com.sosialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.sosialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.sosialite.solite_pos.view.main.MainActivity
import com.sosialite.solite_pos.view.main.menu.adapter.ItemOrderListAdapter
import com.sosialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter

class OrderActivity : SocialiteActivity() {

	private lateinit var _binding: ActivityOrderBinding
	private lateinit var adapter: ItemOrderListAdapter
	private lateinit var vpAdapter: ViewPagerAdapter
	private lateinit var _order: OrderListBinding

	private var order: Order? = null
	private var type: Int = 0

	companion object{
		const val NEW_ORDER_RQ_CODE = 101
		const val EDIT_ORDER_RQ_CODE = 202

		const val ORDER_DATA = "order_data"
		const val ORDER_TYPE = "order_type"
		const val EDIT_ORDER = 11
		const val NEW_ORDER = 22
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityOrderBinding.inflate(layoutInflater)
		_order = _binding.newOrderList
		setContentView(_binding.root)

		order = intent.getSerializableExtra(ORDER_DATA) as Order?
		type = intent.getIntExtra(ORDER_TYPE, 0)

		vpAdapter = ViewPagerAdapter(supportFragmentManager)
		_binding.vpNewOrder.adapter = vpAdapter
		_binding.tabNewOrder.setupWithViewPager(_binding.vpNewOrder)


		adapter = ItemOrderListAdapter(ItemOrderListAdapter.ORDER)
		adapter.buttonCallback = { setButton(it) }

		_order.rvOrderList.layoutManager = LinearLayoutManager(this)
		_order.rvOrderList.adapter = adapter

		when(type){
			NEW_ORDER -> startActivityForResult(Intent(this, CustomerNameActivity::class.java), CustomerNameActivity.RQ_COSTUMER)
			EDIT_ORDER -> setEditOrder()
		}

		_binding.btnNwBack.setOnClickListener { onBackPressed() }
		_order.btnOlCreate.setOnClickListener { setResult() }
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == CustomerNameActivity.RQ_COSTUMER){
			when(resultCode){
				RESULT_OK -> {
					val customer = data?.getSerializableExtra(CustomerNameActivity.CUSTOMER) as Customer?
					if (customer != null){
						setNewOrder(customer)
					}
				}
				RESULT_CANCELED -> finish()
			}
		}
	}

	private fun setButton(state: Boolean){
		_order.btnOlCreate.isEnabled = state
	}

	private fun setNewOrder(customer: Customer){
		order = Order(customer, OrderNo.orderNo, currentTime)
		adapter.setItems(order)

		setContent(order!!)
	}

	private fun setEditOrder(){
		if (order != null){
			adapter.setItems(order)

			setContent(order!!)
		}
	}

	private fun setContent(order: Order){
		val no = "No. ${order.orderNo}"
		_order.tvOlNo.text = no
		_order.tvOlDate.text = order.timeString
		_order.tvOlName.text = order.customer?.name

		setPageAdapter()
	}

	private fun setPageAdapter(){
		val fragments: ArrayList<FragmentWithTitle> = ArrayList()
		for (ctg in DataDummy.DataCategory.allCategory){
			val fragment = ProductListFragment(ctg, order) {b, d ->
				if (b) adapter.addItem(d) else adapter.delItem(d)
			}
			fragments.add(FragmentWithTitle(ctg.name, fragment))
		}

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
