package com.sosialite.solite_pos.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.sosialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import com.sosialite.solite_pos.data.source.local.entity.room.master.Purchase
import com.sosialite.solite_pos.databinding.ActivityMainBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.utils.printer.PrintBill
import com.sosialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.sosialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.sosialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter
import com.sosialite.solite_pos.view.main.menu.bottom.DetailOutcomeFragment
import com.sosialite.solite_pos.view.main.menu.main.*
import com.sosialite.solite_pos.view.main.menu.order.OrderActivity
import com.sosialite.solite_pos.view.main.menu.purchase.PurchaseActivity
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class MainActivity : SocialiteActivity() {

	private lateinit var _binding: ActivityMainBinding
	private lateinit var adapter: ViewPagerAdapter
	private lateinit var viewModel: MainViewModel

	lateinit var printBill: PrintBill

	private var primaryColor: Int = 0
	private var white: Int = 0

	private val onProcessFragment: OnProcessFragment = OnProcessFragment()
	private val purchaseFragment: PurchaseFragment = PurchaseFragment()
	private val outcomeFragment: OutcomeFragment = OutcomeFragment()
	private val settingFragment: SettingFragment = SettingFragment()
	private val notPayFragment: NotPayFragment = NotPayFragment()
	private val masterFragment: MasterFragment = MasterFragment()
	private val doneFragment: DoneFragment = DoneFragment()

	companion object{
		const val EXTRA_ORDER = "extra_order"
		const val EXTRA_PURCHASE = "extra_purchase"
	}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
		_binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

		viewModel = getViewModel(this)

		printBill = PrintBill(this)

		primaryColor = ResourcesCompat.getColor(resources, R.color.primary, null)
		white = ResourcesCompat.getColor(resources, R.color.white, null)

		adapter = ViewPagerAdapter(this)
		_binding.vpMain.adapter = adapter
		_binding.vpMain.isUserInputEnabled = false

		setPager()
		setMenu()
    }

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		when (requestCode){
			OrderActivity.NEW_ORDER_RQ_CODE -> {
				if (data != null){
					val order: OrderWithProduct? = data.getSerializableExtra(EXTRA_ORDER) as OrderWithProduct?
					if (order != null){
						Order.add(this)
						addOrder(order)
					}
				}
			}
			PrintBill.REQUEST_CONNECT_BT -> printBill.onSetSocket()
			PurchaseActivity.NEW_PURCHASE -> {
				if (data != null){
					val purchase: PurchaseWithProduct? = data.getSerializableExtra(EXTRA_PURCHASE) as PurchaseWithProduct?
					if (purchase != null){
						addPurchase(purchase)
					}
				}
			}
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		printBill.onDestroy()
	}

	private fun setMenu(){
		_binding.mainMenu.menuOrder.setOnClickListener { setMenu(it, 0, true) }
		_binding.mainMenu.menuNotPay.setOnClickListener { setMenu(it, 1, true) }
		_binding.mainMenu.menuDone.setOnClickListener { setMenu(it, 2, true) }
		_binding.mainMenu.menuOutcome.setOnClickListener { setMenu(it, 3, true) }
		_binding.mainMenu.menuPurchase.setOnClickListener { setMenu(it, 4, true) }
		_binding.mainMenu.menuMaster.setOnClickListener { setMenu(it, 5, false) }
		_binding.mainMenu.menuSetting.setOnClickListener { setMenu(it, 6, false) }
	}

	fun setToNotPay(order: OrderWithProduct?){
		if (order != null){
			order.order.status = Order.NEED_PAY
			viewModel.updateOrder(order.order)
			onProcessFragment.removeItem(order)
			notPayFragment.addItem(order)
		}
	}

	fun setPay(order: OrderWithProduct){
		order.order.status = Order.DONE
		viewModel.updateOrder(order.order)
		printBill.doPrint(order)
		onProcessFragment.removeItem(order)
		notPayFragment.removeItem(order)
		doneFragment.addItem(order)
	}

	fun addOrder(order: OrderWithProduct){
		viewModel.newOrder(order)
		onProcessFragment.addItem(order)
	}

	fun cancelOrder(order: OrderWithProduct?){
		if (order != null){
			order.order.status = Order.CANCEL
			viewModel.cancelOrder(order)
			onProcessFragment.removeItem(order)
		}
	}

	private fun addPurchase(purchase: PurchaseWithProduct){
		Purchase.add(this)
		viewModel.newPurchase(purchase)
		purchaseFragment.addPurchase(purchase)
	}

	private fun setPager(){
		val arrayList: ArrayList<FragmentWithTitle> = ArrayList()
		arrayList.add(0, FragmentWithTitle("", onProcessFragment))
		arrayList.add(1, FragmentWithTitle("", notPayFragment))
		arrayList.add(2, FragmentWithTitle("", doneFragment))
		arrayList.add(3, FragmentWithTitle("", outcomeFragment))
		arrayList.add(4, FragmentWithTitle("", purchaseFragment))
		arrayList.add(5, FragmentWithTitle("", masterFragment))
		arrayList.add(6, FragmentWithTitle("", settingFragment))

		adapter.setData(arrayList)
		_binding.vpMain.offscreenPageLimit = adapter.itemCount
		_binding.mainMenu.menuOrder.setBackgroundColor(primaryColor)
	}

	private fun setMenu(v: View, pos: Int, isFabShow: Boolean){
		when(pos){
			0,1,2, -> setFab(1)
			3 -> setFab(2)
			4 -> setFab(3)
		}
		if (isFabShow) _binding.fabMainNewOrder.show() else _binding.fabMainNewOrder.hide()
		resetButton()
		v.setBackgroundColor(primaryColor)
		_binding.vpMain.setCurrentItem(pos, true)
	}

	private fun resetButton(){
		_binding.mainMenu.menuOutcome.setBackgroundColor(white)
		_binding.mainMenu.menuPurchase.setBackgroundColor(white)
		_binding.mainMenu.menuSetting.setBackgroundColor(white)
		_binding.mainMenu.menuHistory.setBackgroundColor(white)
		_binding.mainMenu.menuMaster.setBackgroundColor(white)
		_binding.mainMenu.menuNotPay.setBackgroundColor(white)
		_binding.mainMenu.menuOrder.setBackgroundColor(white)
		_binding.mainMenu.menuDone.setBackgroundColor(white)
	}

	private fun setFab(type: Int){
		when(type){
			1 -> {
				_binding.fabMainNewOrder.text = "Pesanan baru"
				_binding.fabMainNewOrder.setOnClickListener {
					startActivityForResult(
							Intent(this, OrderActivity::class.java)
									.putExtra(OrderActivity.ORDER_TYPE, OrderActivity.NEW_ORDER),
							OrderActivity.NEW_ORDER_RQ_CODE)
				}
			}
			2 -> {
				_binding.fabMainNewOrder.text = "Pengeluaran baru"
				_binding.fabMainNewOrder.setOnClickListener {
					DetailOutcomeFragment().show(supportFragmentManager, "detail-outcome")
				}
			}
			3 -> {
				_binding.fabMainNewOrder.text = "Pembelian baru"
				_binding.fabMainNewOrder.setOnClickListener {
					startActivityForResult(
							Intent(this, PurchaseActivity::class.java),
							PurchaseActivity.NEW_PURCHASE
					)
				}
			}
		}
	}

}
