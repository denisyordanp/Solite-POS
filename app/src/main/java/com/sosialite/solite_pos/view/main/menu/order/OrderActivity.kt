package com.sosialite.solite_pos.view.main.menu.order

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.sosialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.Category
import com.sosialite.solite_pos.data.source.local.entity.room.master.Customer
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import com.sosialite.solite_pos.databinding.ActivityOrderBinding
import com.sosialite.solite_pos.databinding.OrderListBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.currentDate
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.utils.tools.MessageBottom
import com.sosialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.sosialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.sosialite.solite_pos.view.main.MainActivity
import com.sosialite.solite_pos.view.main.menu.adapter.ItemOrderListAdapter
import com.sosialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter
import com.sosialite.solite_pos.view.main.menu.master.bottom.DetailOrderProductFragment
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class OrderActivity : SocialiteActivity() {

	private lateinit var _binding: ActivityOrderBinding
	private lateinit var adapter: ItemOrderListAdapter
	private lateinit var vpAdapter: ViewPagerAdapter
	private lateinit var _order: OrderListBinding
	private lateinit var viewModel: MainViewModel

	private var order: OrderWithProduct? = null
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

		viewModel = getViewModel(this)

		order = intent.getSerializableExtra(ORDER_DATA) as OrderWithProduct?
		type = intent.getIntExtra(ORDER_TYPE, 0)

		vpAdapter = ViewPagerAdapter(this)
		_binding.vpNewOrder.adapter = vpAdapter

		_order.rvOrderList.layoutManager = LinearLayoutManager(this)

		when(type){
			NEW_ORDER -> startActivityForResult(Intent(this, CustomerNameActivity::class.java), CustomerNameActivity.RQ_COSTUMER)
			EDIT_ORDER -> setEditOrder(order!!)
		}

		_binding.btnNwBack.setOnClickListener { onBackPressed() }
		_order.btnOlCreate.setOnClickListener { setDine() }
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

	private fun setNewOrder(customer: Customer){
		order = OrderWithProduct(
				Order(Order.orderNo(this), customer.id, currentDate),
				customer
		)
		adapter = ItemOrderListAdapter(ItemOrderListAdapter.ORDER)
		adapter.btnCallback = { setButton(it) }
		adapter.order = order
		setContent(order!!)
	}

	private fun setEditOrder(order: OrderWithProduct){
		adapter = ItemOrderListAdapter(ItemOrderListAdapter.EDIT)
		adapter.btnCallback = { setButton(it) }
		adapter.order = order
		setContent(order)
	}

	private fun setContent(order: OrderWithProduct){
		_order.rvOrderList.adapter = adapter

		val no = "No. ${order.order.orderNo}"
		_order.tvOlNo.text = no
		_order.tvOlDate.text = order.order.timeString
		_order.tvOlName.text = order.customer.name

		setPageAdapter()
	}

	private fun setPageAdapter(){
		viewModel.getCategories(Category.getFilter(Category.ACTIVE)).observe(this, {
			if (!it.isNullOrEmpty()){
				val fragments: ArrayList<FragmentWithTitle> = ArrayList()
				for (ctg in it){
					val fragment = ProductOrderFragment(DetailOrderProductFragment.ORDER, ctg) { p ->
						adapter.addItem(p)
					}
					fragments.add(FragmentWithTitle(ctg.name, fragment))
				}

				setData(fragments)
			}
		})
	}

	private fun setData(fragments: ArrayList<FragmentWithTitle>){
		TabLayoutMediator(_binding.tabNewOrder, _binding.vpNewOrder) { tab, position ->
			tab.text = fragments[position].title
			_binding.vpNewOrder.setCurrentItem(tab.position, true)
		}.attach()
		vpAdapter.setData(fragments)
		_binding.vpNewOrder.offscreenPageLimit = vpAdapter.itemCount
	}

	private fun setButton(state: Boolean){
		_order.btnOlCreate.isEnabled = state
	}

	private fun setDine(){
		MessageBottom(supportFragmentManager)
				.setMessage("Apakah makan ditempat atau dibungkus?")
				.setPositiveListener("Makan ditempat"){ setResult(false) }
				.setNegativeListener("Dibungkus"){ setResult(true) }
				.show()
	}

	private fun setResult(isTakeAway: Boolean){
		if (adapter.newOrder != null){
			val intent = Intent()
			val data = adapter.newOrder
			data?.order?.isTakeAway = isTakeAway
			intent.putExtra(MainActivity.EXTRA_ORDER, data)
			setResult(RESULT_OK, intent)
			finish()
		}
	}
}
