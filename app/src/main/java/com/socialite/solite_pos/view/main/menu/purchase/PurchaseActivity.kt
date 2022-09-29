package com.socialite.solite_pos.view.main.menu.purchase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.socialite.solite_pos.R
import com.socialite.solite_pos.adapters.recycleView.purchase.PurchaseListAdapter
import com.socialite.solite_pos.adapters.viewPager.ViewPagerAdapter
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseProductWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Purchase
import com.socialite.solite_pos.data.source.local.entity.room.master.PurchaseProduct
import com.socialite.solite_pos.data.source.local.entity.room.master.Supplier
import com.socialite.solite_pos.databinding.ActivityPurchaseBinding
import com.socialite.solite_pos.databinding.OrderListBinding
import com.socialite.solite_pos.utils.tools.MessageBottom
import com.socialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.view.main.menu.master.dialog.DetailOrderProductFragment
import com.socialite.solite_pos.view.main.menu.order.SelectProductOrderByCategoryFragment
import com.socialite.solite_pos.view.main.opening.MainActivity
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PurchaseActivity : SocialiteActivity() {

    private lateinit var _binding: ActivityPurchaseBinding
    private lateinit var adapter: PurchaseListAdapter
    private lateinit var vpAdapter: ViewPagerAdapter
    private lateinit var _purchase: OrderListBinding
    private lateinit var viewModel: ProductViewModel

    private var purchase: Purchase? = null
    private var supplier: Supplier? = null

    companion object {
        const val NEW_PURCHASE = 99
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPurchaseBinding.inflate(layoutInflater)
        _purchase = _binding.purchaseList
        setContentView(_binding.root)

        viewModel = ProductViewModel.getMainViewModel(this)

        vpAdapter = ViewPagerAdapter(this)
        _binding.vpPurchase.adapter = vpAdapter

        adapter = PurchaseListAdapter()
        adapter.btnCallback = {
            _purchase.btnOlCreate.isEnabled = it
        }
        _purchase.rvOrderList.adapter = adapter
        _purchase.rvOrderList.layoutManager = LinearLayoutManager(this)

        selectSupplier()

        _binding.btnPcBack.setOnClickListener { onBackPressed() }
        _purchase.btnOlCreate.setOnClickListener {
            MessageBottom(supportFragmentManager)
                .setMessageImage(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_ask_question,
                        null
                    )
                )
                .setMessage("Apakah yakin dengan pembelian ini?")
                .setPositiveListener("Ya") { setResult() }
                .setNegativeListener("Batal") { it?.dismiss() }
                .show()
        }
    }

    private fun selectSupplier() {
        SelectSupplierFragment {
            if (it == null) {
                onBackPressed()
            } else {
                supplier = it
                purchase = Purchase(this, it.id)
                adapter.purchase = PurchaseWithProduct(purchase!!, it)
                setPageAdapter()
            }
        }.show(supportFragmentManager, "select_supplier")
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
                                DetailOrderProductFragment.PURCHASE,
                                ctg
                            ) { p ->
                                addItemToAdapter(p)
                            }
                            fragments.add(FragmentWithTitle(ctg.name, fragment))
                        }

                        setData(fragments)
                    }
                }
        }
    }

    private fun addItemToAdapter(product: ProductOrderDetail) {
        if (purchase != null) {
            adapter.addItem(
                PurchaseProductWithProduct(
                    PurchaseProduct(purchase!!.purchaseNo, product.product!!.id, product.amount),
                    product.product
                )
            )
            _purchase.rvOrderList.scrollToPosition(0)
        }
    }

    private fun setData(fragments: ArrayList<FragmentWithTitle>) {
        TabLayoutMediator(_binding.tabPurchase, _binding.vpPurchase) { tab, position ->
            tab.text = fragments[position].title
            _binding.vpPurchase.setCurrentItem(tab.position, true)
        }.attach()
        vpAdapter.setData(fragments)
        _binding.vpPurchase.offscreenPageLimit = vpAdapter.itemCount

        _purchase.tvOlNo.text = purchase?.purchaseNo
        _purchase.tvOlName.text = supplier?.name
        _purchase.btnOlCreate.text = "Buat Pembelian"
    }

    private fun setResult() {
        if (adapter.newPurchase != null) {
            val intent = Intent()
            intent.putExtra(MainActivity.EXTRA_PURCHASE, adapter.newPurchase)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
