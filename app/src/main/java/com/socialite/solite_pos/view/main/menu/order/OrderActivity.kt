package com.socialite.solite_pos.view.main.menu.order

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.remote.response.helper.StatusResponse
import com.socialite.solite_pos.databinding.ActivityOrderBinding
import com.socialite.solite_pos.databinding.OrderListBinding
import com.socialite.solite_pos.utils.config.DateUtils.Companion.currentDateTime
import com.socialite.solite_pos.utils.tools.MessageBottom
import com.socialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.view.main.menu.adapter.ItemOrderListAdapter
import com.socialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter
import com.socialite.solite_pos.view.main.menu.master.dialog.DetailOrderProductFragment
import com.socialite.solite_pos.view.main.opening.MainActivity
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.MainViewModel.Companion.getMainViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel.Companion.getOrderViewModel
import com.socialite.solite_pos.vo.Status

class OrderActivity : SocialiteActivity() {

	private lateinit var _binding: ActivityOrderBinding
	private lateinit var _order: OrderListBinding

	private lateinit var adapter: ItemOrderListAdapter
	private lateinit var vpAdapter: ViewPagerAdapter

	private lateinit var orderViewModel: OrderViewModel
	private lateinit var viewModel: MainViewModel

	private var order: OrderWithProduct? = null

	companion object{
		const val NEW_ORDER_RQ_CODE = 101
		const val EDIT_ORDER = "edit_order"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOrderBinding.inflate(layoutInflater)
        _order = _binding.newOrderList
        _order.contItemAmount.root.visibility = View.GONE
        setContentView(_binding.root)

        viewModel = getMainViewModel(this)
		orderViewModel = getOrderViewModel(this)

        vpAdapter = ViewPagerAdapter(this)
        _binding.vpNewOrder.adapter = vpAdapter

        _order.rvOrderList.layoutManager = LinearLayoutManager(this)

		order = intent.getSerializableExtra(EDIT_ORDER) as OrderWithProduct?

		if (order != null) {
			setEditOrder(order!!)
		} else {
			startActivityForResult(Intent(this, SelectCustomerActivity::class.java), SelectCustomerActivity.RC_COSTUMER)
		}

		_binding.btnNwBack.setOnClickListener { onBackPressed() }
		_order.btnOlCreate.setOnClickListener {
			if (order != null) {
				editOrder(order!!)
			} else {
				setDine()
			}
		}
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

	private fun setNewOrder(customer: Customer) {
        val order = OrderWithProduct(OrderData(
                Order(Order.orderNo(this), customer.id, currentDateTime),
                customer
        ))
        setAdapter(order)
        setContent(order)
    }

	private fun setAdapter(order: OrderWithProduct) {
		adapter = ItemOrderListAdapter(this, ItemOrderListAdapter.ORDER)
		adapter.btnCallback = { setButton(it) }
		adapter.order = order
	}

	private fun setEditOrder(order: OrderWithProduct) {
		setAdapter(order)
		setContent(order)
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

	private fun editOrder(order: OrderWithProduct) {

		val new = adapter.newOrder

		if (new != null) {
			orderViewModel.replaceProductOrder(order, new) {
				when(it.status) {
					StatusResponse.SUCCESS -> {
						finish()
					}
					StatusResponse.ERROR -> {}
					else -> {}
				}
			}
		}

	}

}
