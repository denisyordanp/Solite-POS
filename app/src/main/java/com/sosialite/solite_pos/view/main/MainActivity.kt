package com.sosialite.solite_pos.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.sosialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import com.sosialite.solite_pos.data.source.local.entity.room.master.Purchase
import com.sosialite.solite_pos.databinding.ActivityMainBinding
import com.sosialite.solite_pos.databinding.MainMenuBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.utils.printer.PrintBill
import com.sosialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.sosialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.sosialite.solite_pos.view.main.menu.outcome.DetailOutcomeActivity
import com.sosialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter
import com.sosialite.solite_pos.view.main.menu.main.*
import com.sosialite.solite_pos.view.main.menu.order.OrderActivity
import com.sosialite.solite_pos.view.main.menu.purchase.PurchaseActivity
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class MainActivity : SocialiteActivity() {

	private lateinit var _binding: ActivityMainBinding
	private lateinit var _menu: MainMenuBinding
	private lateinit var adapter: ViewPagerAdapter
	private lateinit var viewModel: MainViewModel

	lateinit var printBill: PrintBill

	private var primaryColor: Int = 0
	private var white: Int = 0

	private val onProcessFragment: OnProcessFragment = OnProcessFragment()
	private val purchaseFragment: PurchaseFragment = PurchaseFragment()
	private val recapFragment: DailyRecapFragment = DailyRecapFragment()
	private val settingFragment: SettingFragment = SettingFragment()
	private val notPayFragment: NotPayFragment = NotPayFragment()
	private val masterFragment: MasterFragment = MasterFragment()
	private val cancelFragment: CancelFragment = CancelFragment()
	private val doneFragment: DoneFragment = DoneFragment()

	companion object{
		const val EXTRA_ORDER = "extra_order"
		const val EXTRA_PURCHASE = "extra_purchase"
	}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
		_binding = ActivityMainBinding.inflate(layoutInflater)
		_menu = _binding.mainMenu
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
		_menu.menuOrder.setOnClickListener { setMenu(it, 0, true) }
		_menu.menuNotPay.setOnClickListener { setMenu(it, 1, true) }
		_menu.menuDone.setOnClickListener { setMenu(it, 2, true) }
		_menu.menuCancel.setOnClickListener { setMenu(it, 3, true) }
		_menu.menuRecap.setOnClickListener { setMenu(it, 4, true) }
		_menu.menuPurchase.setOnClickListener { setMenu(it, 5, true) }
		_menu.menuMaster.setOnClickListener { setMenu(it, 6, false) }
		_menu.menuSetting.setOnClickListener { setMenu(it, 7, false) }

		_menu.menuOrder.performClick()
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
		arrayList.add(3, FragmentWithTitle("", cancelFragment))
		arrayList.add(4, FragmentWithTitle("", recapFragment))
		arrayList.add(5, FragmentWithTitle("", purchaseFragment))
		arrayList.add(6, FragmentWithTitle("", masterFragment))
		arrayList.add(7, FragmentWithTitle("", settingFragment))

		adapter.setData(arrayList)
		_binding.vpMain.offscreenPageLimit = adapter.itemCount
		_menu.menuOrder.setBackgroundColor(primaryColor)
	}

	private fun setMenu(v: View, pos: Int, isFabShow: Boolean){
		when(pos){
			0,1,2,3 -> setFab(1)
			4 -> setFab(2)
			5 -> setFab(3)
		}
		if (isFabShow) _binding.fabMainNewOrder.show() else _binding.fabMainNewOrder.hide()
		resetButton()
		v.setBackgroundColor(primaryColor)
		_binding.vpMain.setCurrentItem(pos, true)
	}

	private fun resetButton(){
		_menu.menuPurchase.setBackgroundColor(white)
		_menu.menuSetting.setBackgroundColor(white)
		_menu.menuHistory.setBackgroundColor(white)
		_menu.menuCancel.setBackgroundColor(white)
		_menu.menuMaster.setBackgroundColor(white)
		_menu.menuNotPay.setBackgroundColor(white)
		_menu.menuRecap.setBackgroundColor(white)
		_menu.menuOrder.setBackgroundColor(white)
		_menu.menuDone.setBackgroundColor(white)
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
				_binding.fabMainNewOrder.text = "Pengeluaran"
				_binding.fabMainNewOrder.setOnClickListener {
					startActivity(Intent(this, DetailOutcomeActivity::class.java))
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
