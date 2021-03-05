package com.socialite.solite_pos.view.main.menu.order

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.databinding.ActivityOrderBinding
import com.socialite.solite_pos.databinding.OrderListBinding
import com.socialite.solite_pos.utils.config.DateUtils.Companion.currentDate
import com.socialite.solite_pos.view.viewmodel.MainViewModel.Companion.getViewModel
import com.socialite.solite_pos.utils.tools.MessageBottom
import com.socialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.view.main.MainActivity
import com.socialite.solite_pos.view.main.menu.adapter.ItemOrderListAdapter
import com.socialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter
import com.socialite.solite_pos.view.main.menu.master.dialog.DetailOrderProductFragment
import com.socialite.solite_pos.view.viewmodel.MainViewModel
import com.socialite.solite_pos.vo.Status

class OrderActivity : SocialiteActivity() {

	private lateinit var _binding: ActivityOrderBinding
	private lateinit var adapter: ItemOrderListAdapter
	private lateinit var vpAdapter: ViewPagerAdapter
	private lateinit var _order: OrderListBinding
	private lateinit var viewModel: MainViewModel

	private var order: OrderWithProduct? = null

	companion object{
		const val NEW_ORDER_RQ_CODE = 101
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityOrderBinding.inflate(layoutInflater)
		_order = _binding.newOrderList
		setContentView(_binding.root)

		viewModel = getViewModel(this)

		vpAdapter = ViewPagerAdapter(this)
		_binding.vpNewOrder.adapter = vpAdapter

		_order.rvOrderList.layoutManager = LinearLayoutManager(this)

		startActivityForResult(Intent(this, SelectCustomerActivity::class.java), SelectCustomerActivity.RC_COSTUMER)

		_binding.btnNwBack.setOnClickListener { onBackPressed() }
		_order.btnOlCreate.setOnClickListener { setDine() }
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		when (requestCode){
			SelectCustomerActivity.RC_COSTUMER -> {
				when(resultCode){
					RESULT_OK -> {
						val customer = data?.getSerializableExtra(SelectCustomerActivity.CUSTOMER) as Customer?
						if (customer != null)
							setNewOrder(customer)
					}
					RESULT_CANCELED -> finish()
				}
			}

			SelectMixVariantOrderActivity.RC_MIX -> {
				if (resultCode == RESULT_OK){
					val product = data?.getSerializableExtra(SelectMixVariantOrderActivity.PRODUCT) as ProductOrderDetail?
					if(product != null) addItemOrder(product)
				}
			}
		}
	}

	private fun addItemOrder(product: ProductOrderDetail){
		adapter.addItem(product)
		_order.rvOrderList.scrollToPosition(0)
	}

	private fun setNewOrder(customer: Customer){
		order = OrderWithProduct(OrderData(
				Order(Order.orderNo(this), customer.id, currentDate),
				customer
		))
		adapter = ItemOrderListAdapter(ItemOrderListAdapter.ORDER)
		adapter.btnCallback = { setButton(it) }
		adapter.order = order
		setContent(order!!)
	}

	private fun setContent(order: OrderWithProduct){
		_order.rvOrderList.adapter = adapter

		val no = "No. ${order.order.order.orderNo}"
		_order.tvOlNo.text = no
		_order.tvOlDate.text = order.order.order.timeString
		_order.tvOlName.text = order.order.customer.name

		setPageAdapter()
	}

	private fun setPageAdapter(){
		viewModel.getCategories(Category.getFilter(Category.ACTIVE)).observe(this, {
			when(it.status){
				Status.SUCCESS -> {
					if (!it.data.isNullOrEmpty()){
						val fragments: ArrayList<FragmentWithTitle> = ArrayList()
						for (ctg in it.data){
							val fragment = SelectProductOrderByCategoryFragment(DetailOrderProductFragment.ORDER, ctg, this) { p ->
								addItemOrder(p)
							}
							fragments.add(FragmentWithTitle(ctg.name, fragment))
						}

						setData(fragments)
					}
				}
				else -> {}
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
			data?.order?.order?.isTakeAway = isTakeAway
			intent.putExtra(MainActivity.EXTRA_ORDER, data)
			setResult(RESULT_OK, intent)
			finish()
		}
	}
}
