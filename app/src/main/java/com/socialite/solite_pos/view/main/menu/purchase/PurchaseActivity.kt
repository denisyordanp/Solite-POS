package com.socialite.solite_pos.view.main.menu.purchase

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
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
import com.socialite.solite_pos.view.main.MainActivity
import com.socialite.solite_pos.view.main.menu.adapter.ItemPurchaseListAdapter
import com.socialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter
import com.socialite.solite_pos.view.main.menu.master.dialog.DetailOrderProductFragment
import com.socialite.solite_pos.view.main.menu.order.SelectProductOrderByCategoryFragment
import com.socialite.solite_pos.view.viewmodel.MainViewModel
import com.socialite.solite_pos.view.viewmodel.MainViewModel.Companion.getViewModel
import com.socialite.solite_pos.vo.Status

class PurchaseActivity : SocialiteActivity() {

	private lateinit var _binding: ActivityPurchaseBinding
	private lateinit var adapter: ItemPurchaseListAdapter
	private lateinit var vpAdapter: ViewPagerAdapter
	private lateinit var _order: OrderListBinding
	private lateinit var viewModel: MainViewModel

	private var purchase: Purchase? = null
	private var supplier: Supplier? = null

	companion object{
		const val NEW_PURCHASE = 99
	}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
		_binding = ActivityPurchaseBinding.inflate(layoutInflater)
		_order = _binding.purchaseList
        setContentView(_binding.root)

		viewModel = getViewModel(this)

		vpAdapter = ViewPagerAdapter(this)
		_binding.vpPurchase.adapter = vpAdapter

		adapter = ItemPurchaseListAdapter(ItemPurchaseListAdapter.PURCHASE)
		adapter.btnCallback = {
			_order.btnOlCreate.isEnabled = it
		}
		_order.rvOrderList.adapter = adapter
		_order.rvOrderList.layoutManager = LinearLayoutManager(this)

		selectSupplier()

		_binding.btnPcBack.setOnClickListener { onBackPressed() }
		_order.btnOlCreate.setOnClickListener {
			MessageBottom(supportFragmentManager)
					.setMessage("Apakah yakin dengan pembelian ini?")
					.setPositiveListener("Ya"){ setResult()  }
					.setNegativeListener("Batal"){ it?.dismiss() }
					.show()
		}
    }

	private fun selectSupplier(){
		SelectSupplierFragment{
			if (it == null){
				onBackPressed()
			}else{
				supplier = it
				purchase = Purchase(this, it.id)
				adapter.purchase = PurchaseWithProduct(purchase!!, it)
				setPageAdapter()
			}
		}.show(supportFragmentManager, "select_supplier")
	}

	private fun setPageAdapter(){
		viewModel.getCategories(Category.getFilter(Category.ACTIVE, true)).observe(this, {
			when(it.status){
				Status.SUCCESS -> {
					if (!it.data.isNullOrEmpty()){
						val fragments: ArrayList<FragmentWithTitle> = ArrayList()
						for (ctg in it.data){
							val fragment = SelectProductOrderByCategoryFragment(DetailOrderProductFragment.PURCHASE, ctg, this) { p ->
								if (purchase != null){
									adapter.addItem(PurchaseProductWithProduct(
											PurchaseProduct(purchase!!.purchaseNo, p.product!!.id, p.amount),
											p.product
									))
								}
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
		TabLayoutMediator(_binding.tabPurchase, _binding.vpPurchase) { tab, position ->
			tab.text = fragments[position].title
			_binding.vpPurchase.setCurrentItem(tab.position, true)
		}.attach()
		vpAdapter.setData(fragments)
		_binding.vpPurchase.offscreenPageLimit = vpAdapter.itemCount

		_order.tvOlNo.text = purchase?.purchaseNo
		_order.tvOlName.text = supplier?.name
		_order.btnOlCreate.text = "Buat Pembelian"
	}

	private fun setResult(){
		if (adapter.newPurchase != null){
			val intent = Intent()
			intent.putExtra(MainActivity.EXTRA_PURCHASE, adapter.newPurchase)
			setResult(RESULT_OK, intent)
			finish()
		}
	}
}
