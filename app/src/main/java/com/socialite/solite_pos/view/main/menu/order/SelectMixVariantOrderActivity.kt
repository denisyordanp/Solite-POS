package com.socialite.solite_pos.view.main.menu.order

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.databinding.ActivitySelectMixVariantOrderBinding
import com.socialite.solite_pos.databinding.OrderListBinding
import com.socialite.solite_pos.view.viewmodel.MainViewModel.Companion.getViewModel
import com.socialite.solite_pos.utils.tools.MessageBottom
import com.socialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.view.main.menu.adapter.ItemOrderMixListAdapter
import com.socialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter
import com.socialite.solite_pos.view.main.menu.master.dialog.DetailOrderProductFragment
import com.socialite.solite_pos.view.viewmodel.MainViewModel
import com.socialite.solite_pos.vo.Status

class SelectMixVariantOrderActivity : SocialiteActivity() {

    private lateinit var _binding: ActivitySelectMixVariantOrderBinding
    private lateinit var adapter: ItemOrderMixListAdapter
    private lateinit var _order: OrderListBinding
    private lateinit var vpAdapter: ViewPagerAdapter
    private lateinit var viewModel: MainViewModel

    private var product: Product? = null

    companion object{
        const val PRODUCT = "product"
        const val RC_MIX = 12
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySelectMixVariantOrderBinding.inflate(layoutInflater)
        _order = _binding.mixProductList
        setContentView(_binding.root)

        product = intent.getSerializableExtra(PRODUCT) as Product?

        viewModel = getViewModel(this)

        adapter = ItemOrderMixListAdapter()
        _order.rvOrderList.layoutManager = LinearLayoutManager(this)
        _order.rvOrderList.adapter = adapter
        adapter.btnCallback = { setButton(it) }

        vpAdapter = ViewPagerAdapter(this)
        _binding.vpMixOrder.adapter = vpAdapter

        setPager()

        _binding.btnMixBack.setOnClickListener { onBackPressed() }
    }

    private fun setPager(){
        if (product != null){
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
                            val fragment = SelectProductOrderByCategoryFragment(DetailOrderProductFragment.MIX, ctg, this) { p ->
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
            val bottom = MessageBottom(supportFragmentManager)
            bottom.setNegativeListener("Oke") { it?.dismiss() }
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
                else -> {
                    true
                }
            }
        }
        return false
    }

    private fun setResult(){
        val intent = Intent()
        val data = ProductOrderDetail.createMix(product, adapter.sortedItems, 1)
        intent.putExtra(PRODUCT, data)
        setResult(RESULT_OK, intent)
        finish()
    }
}