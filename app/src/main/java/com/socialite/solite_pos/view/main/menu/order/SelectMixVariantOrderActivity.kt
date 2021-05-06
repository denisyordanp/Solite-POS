package com.socialite.solite_pos.view.main.menu.order

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.databinding.ActivitySelectMixVariantOrderBinding
import com.socialite.solite_pos.databinding.ItemAmountLayoutBinding
import com.socialite.solite_pos.databinding.OrderListBinding
import com.socialite.solite_pos.utils.tools.MessageBottom
import com.socialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.view.main.menu.adapter.ItemOrderMixListAdapter
import com.socialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter
import com.socialite.solite_pos.view.main.menu.master.dialog.DetailOrderProductFragment
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import com.socialite.solite_pos.vo.Status

class SelectMixVariantOrderActivity : SocialiteActivity() {

    private lateinit var _binding: ActivitySelectMixVariantOrderBinding
    private lateinit var adapter: ItemOrderMixListAdapter
    private lateinit var _amount: ItemAmountLayoutBinding
    private lateinit var _order: OrderListBinding
    private lateinit var vpAdapter: ViewPagerAdapter
    private lateinit var viewModel: ProductViewModel

    private var productOrderDetail: ProductOrderDetail? = null
    private var product: Product? = null
    private var amount: Int = 1

    companion object{
        const val PRODUCT_ORDER_DETAIL = "product_order_detail"
        const val PRODUCT = "product"
        const val RC_MIX = 12
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySelectMixVariantOrderBinding.inflate(layoutInflater)
        _order = _binding.mixProductList
        _amount = _order.contItemAmount
        setContentView(_binding.root)

        viewModel = ProductViewModel.getMainViewModel(this)

        setAdapter()
        checkIntent()
        setPager()

        _binding.btnMixBack.setOnClickListener { onBackPressed() }
        _amount.btnPlPlus.setOnClickListener { addAmount() }
        _amount.btnPlMin.setOnClickListener { minAmount() }
    }

    private fun setAdapter() {
        adapter = ItemOrderMixListAdapter()
        _order.rvOrderList.layoutManager = LinearLayoutManager(this)
        _order.rvOrderList.adapter = adapter
        _amount.tvPlAmount.text = amount.toString()
        adapter.btnCallback = { setButton(it) }
    }

    private fun checkIntent() {
        productOrderDetail = intent.getSerializableExtra(PRODUCT_ORDER_DETAIL) as ProductOrderDetail?
        if (productOrderDetail != null) {
            setItems(productOrderDetail!!)
            return
        }

        product = intent.getSerializableExtra(PRODUCT) as Product?
    }

    private fun setItems(detail: ProductOrderDetail) {
        product = detail.product
        adapter.setItems(detail.mixProducts)
        _amount.tvPlAmount.text = detail.amount.toString()
    }

    private fun setPager() {
        vpAdapter = ViewPagerAdapter(this)
        _binding.vpMixOrder.adapter = vpAdapter

        if (product != null) {
            _order.tvOlName.text = product!!.name
            _order.tvOlNo.text = product!!.desc
            _order.btnOlCreate.text = "Simpan pesanan"
            _order.btnOlCreate.setOnClickListener {
                if (validate())
                    setResult()
            }

            setPageAdapter()
        }
    }

    private fun setPageAdapter(){
        viewModel.getCategories(Category.getFilter(Category.ACTIVE, true)).observe(this, {
            when(it.status){
                Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()){
                        val fragments: ArrayList<FragmentWithTitle> = ArrayList()
                        for (ctg in it.data){
                            val fragment = SelectProductOrderByCategoryFragment(DetailOrderProductFragment.MIX, ctg) { p ->
                                adapter.addItem(p)
                            }
                            fragments.add(FragmentWithTitle(ctg.name, fragment))
                        }

                        setPager(fragments)
                    }
                }
                else -> {}
            }
        })
    }

    private fun setPager(fragments: ArrayList<FragmentWithTitle>){
        TabLayoutMediator(_binding.tabMixOrder, _binding.vpMixOrder) { tab, position ->
            tab.text = fragments[position].title
            _binding.vpMixOrder.setCurrentItem(tab.position, true)
        }.attach()
        vpAdapter.setData(fragments)
        _binding.vpMixOrder.offscreenPageLimit = vpAdapter.itemCount
    }

    private fun setButton(state: Boolean){
        _order.btnOlCreate.isEnabled = state
    }

    private fun validate(): Boolean {
        if (product != null){
            val check = checkStock()
            val bottom = MessageBottom(supportFragmentManager)
            bottom.setNegativeListener(getString(android.R.string.ok)) { it?.dismiss() }
            return when {
                adapter.totalItem < product!!.portion -> {
                    bottom.setMessage("Jumlah varian kurang dari porsi")
                    bottom.show()
                    false
                }
                adapter.totalItem > product!!.portion -> {
                    bottom.setMessage("Jumlah varian lebih dari porsi")
                    bottom.show()
                    false
                }
                check != null -> {
                    bottom.setMessage("Stok untuk produk ${check["name"]} hanya sisa ${check["stock"]}.")
                    bottom.show()
                    false
                }
                else -> {
                    true
                }
            }
        }
        return false
    }

    private fun checkStock(): HashMap<String, String>? {
        for (item in adapter.sortedItems) {
            if (item.product.stock < (item.amount * amount)) {
                val map: HashMap<String, String> = HashMap()
                map["name"] = item.product.name
                map["stock"] = item.product.stock.toString()
                return map
            }
        }
        return null
    }

    private fun setResult() {
        val intent = Intent()
        val data = ProductOrderDetail.createMix(product, adapter.sortedItems, amount)
        intent.putExtra(PRODUCT, data)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun addAmount() {
        amount += 1
        _amount.tvPlAmount.text = amount.toString()
    }

    private fun minAmount() {
        if (amount > 1) {
            amount -= 1
            _amount.tvPlAmount.text = amount.toString()
        }
    }
}