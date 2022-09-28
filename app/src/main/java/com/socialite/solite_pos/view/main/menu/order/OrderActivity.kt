package com.socialite.solite_pos.view.main.menu.order

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.socialite.solite_pos.R
import com.socialite.solite_pos.adapters.recycleView.order.ProductOrderListAdapter
import com.socialite.solite_pos.adapters.viewPager.ViewPagerAdapter
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.databinding.ActivityOrderBinding
import com.socialite.solite_pos.databinding.OrderListBinding
import com.socialite.solite_pos.utils.config.DateUtils.Companion.currentDateTime
import com.socialite.solite_pos.utils.tools.MessageBottom
import com.socialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.view.main.menu.master.dialog.DetailOrderProductFragment
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OrderActivity : SocialiteActivity() {

    private lateinit var _binding: ActivityOrderBinding
    private lateinit var _order: OrderListBinding

    private lateinit var adapter: ProductOrderListAdapter
    private lateinit var vpAdapter: ViewPagerAdapter

    private lateinit var orderViewModel: OrderViewModel
    private lateinit var viewModel: ProductViewModel

    private var order: OrderWithProduct? = null

    companion object {
        const val EDIT_ORDER = "edit_order"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOrderBinding.inflate(layoutInflater)
        _order = _binding.newOrderList
        _order.contItemAmount.root.visibility = View.GONE
        setContentView(_binding.root)

        viewModel = ProductViewModel.getMainViewModel(this)
        orderViewModel = OrderViewModel.getOrderViewModel(this)

        vpAdapter = ViewPagerAdapter(this)
        _binding.vpNewOrder.adapter = vpAdapter

        _order.rvOrderList.layoutManager = LinearLayoutManager(this)

        order = intent.getSerializableExtra(EDIT_ORDER) as OrderWithProduct?

        if (order != null) {
            setEditOrder(order!!)
        } else {
            startActivityForResult(
                Intent(this, SelectCustomerActivity::class.java),
                SelectCustomerActivity.RC_COSTUMER
            )
        }

        _binding.btnNwBack.setOnClickListener { onBackPressed() }
        _order.btnOlCreate.setOnClickListener {
            if (order != null) {
                showRepeatOrderMessage()
            } else {
                showRepeatOrderMessage()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SelectCustomerActivity.RC_COSTUMER -> {
                when (resultCode) {
                    RESULT_OK -> {
                        val customer =
                            data?.getSerializableExtra(SelectCustomerActivity.CUSTOMER) as Customer?
                        if (customer != null)
                            setNewOrder(customer)
                    }

                    RESULT_CANCELED -> finish()
                }
            }

            SelectMixVariantOrderActivity.RC_MIX -> {
                if (resultCode == RESULT_OK) {
                    val product =
                        data?.getSerializableExtra(SelectMixVariantOrderActivity.PRODUCT) as ProductOrderDetail?
                    if (product != null) addItemOrder(product)
                }
            }
        }
    }

    private fun addItemOrder(product: ProductOrderDetail) {
        adapter.addItem(product)
        _order.rvOrderList.scrollToPosition(0)
    }

    private fun setNewOrder(customer: Customer) {
        val order = OrderWithProduct(
            OrderData(
                Order(Order.orderNo(this), customer.id, currentDateTime),
                customer
            )
        )
        setAdapter(order)
        setContent(order, false)
    }

    private fun setAdapter(order: OrderWithProduct) {
        adapter = ProductOrderListAdapter(this)
        adapter.buttonEnableCallback = { setButton(it) }
        adapter.order = order
    }

    private fun setEditOrder(order: OrderWithProduct) {
        setAdapter(order)
        setContent(order, true)
    }

    private fun setContent(order: OrderWithProduct, isEditOrder: Boolean) {
        _order.rvOrderList.adapter = adapter

        val no = "No. ${order.order.order.orderNo}"
        _order.tvOlNo.text = no
        _order.tvOlDate.text = order.order.order.timeString
        _order.tvOlName.text = order.order.customer.name

        val txtButton = if (isEditOrder) {
            "Rubah pesanan"
        } else {
            "Simpan Pesanan"
        }
        _order.btnOlCreate.text = txtButton

        setPageAdapter()
    }

    private fun setPageAdapter() {
        lifecycleScope.launch {
            val query = Category.getFilter(Category.ACTIVE)
            viewModel.getCategories(query)
                .collect {
                    if (it.isNotEmpty()) {
                        val fragments: ArrayList<FragmentWithTitle> = ArrayList()
                        for (ctg in it) {
                            val fragment = SelectProductOrderByCategoryFragment(
                                DetailOrderProductFragment.ORDER,
                                ctg
                            ) { p ->
                                addItemOrder(p)
                            }
                            fragments.add(FragmentWithTitle(ctg.name, fragment))
                        }

                        setData(fragments)
                    }
                }
        }
    }

    private fun setData(fragments: ArrayList<FragmentWithTitle>) {
        TabLayoutMediator(_binding.tabNewOrder, _binding.vpNewOrder) { tab, position ->
            tab.text = fragments[position].title
            _binding.vpNewOrder.setCurrentItem(tab.position, true)
        }.attach()
        vpAdapter.setData(fragments)
        _binding.vpNewOrder.offscreenPageLimit = vpAdapter.itemCount
    }

    private fun setButton(state: Boolean) {
        _order.btnOlCreate.isEnabled = state
    }

    private fun showRepeatOrderMessage() {
        MessageBottom(supportFragmentManager)
            .setMessageImage(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_speak_to_customer,
                    null
                )
            )
            .setMessage("Bacakan ulang pesanan untuk menghindari kesalahan pesanan")
            .setPositiveListener("Sudah") { setDine() }
            .setNegativeListener("Bacakan ulang") { it?.dismiss() }
            .show()
    }

    private fun setDine() {
        MessageBottom(supportFragmentManager)
            .setMessageImage(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_dine_or_takeaway,
                    null
                )
            )
            .setMessage("Apakah makan ditempat atau dibungkus?")
            .setPositiveListener("Makan ditempat") { createNewOrder(false) }
            .setNegativeListener("Dibungkus") { createNewOrder(true) }
            .show()
    }

    private fun createNewOrder(isTakeAway: Boolean) {
        val data = adapter.newOrder
        if (data != null) {
            data.order.order.isTakeAway = isTakeAway
            if (order != null) {
                editOrder(order!!, data)
            } else {
                newOrder(data)
            }
            finish()
        }
    }

    private fun newOrder(order: OrderWithProduct) {
        Order.add(this)
        orderViewModel.newOrder(order)
    }

    private fun editOrder(old: OrderWithProduct, new: OrderWithProduct) {
        orderViewModel.replaceProductOrder(old, new)
    }

}
