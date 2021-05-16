package com.socialite.solite_pos.view.main.opening

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.socialite.solite_pos.R
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.Purchase
import com.socialite.solite_pos.data.source.local.entity.room.master.User
import com.socialite.solite_pos.databinding.ActivityMainBinding
import com.socialite.solite_pos.databinding.MainMenuBinding
import com.socialite.solite_pos.utils.config.DateUtils.Companion.currentDate
import com.socialite.solite_pos.utils.preference.UserPref
import com.socialite.solite_pos.utils.printer.PrintBill
import com.socialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter
import com.socialite.solite_pos.view.main.menu.main.*
import com.socialite.solite_pos.view.main.menu.order.OrderActivity
import com.socialite.solite_pos.view.main.menu.outcome.DetailOutcomeActivity
import com.socialite.solite_pos.view.main.menu.purchase.PurchaseActivity
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel.Companion.getOrderViewModel

class MainActivity : SocialiteActivity() {

	private lateinit var _binding: ActivityMainBinding
	private lateinit var orderViewModel: OrderViewModel
	private lateinit var adapter: ViewPagerAdapter
	private lateinit var viewModel: MainViewModel
	private lateinit var _menu: MainMenuBinding
	private lateinit var userPref: UserPref

	private lateinit var showedDate: String
	private var primaryColor: Int = 0
	private var white: Int = 0

	private val purchaseFragment: PurchaseFragment = PurchaseFragment()
	private val settingFragment: SettingFragment = SettingFragment()
	private val historyFragment: HistoryFragment = HistoryFragment()
	private val masterFragment: MasterFragment = MasterFragment()

	private lateinit var onProcessFragment: OnProcessFragment
	private lateinit var recapFragment: DailyRecapFragment
	private lateinit var notPayFragment: NotPayFragment
	private lateinit var cancelFragment: CancelFragment
	private lateinit var doneFragment: DoneFragment

	companion object {
		const val EXTRA_ORDER = "extra_order"
		const val EXTRA_PURCHASE = "extra_purchase"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityMainBinding.inflate(layoutInflater)
		_menu = _binding.mainMenu
		setContentView(_binding.root)

		showedDate = currentDate
		userPref = UserPref(this)

		orderViewModel = getOrderViewModel(this)
		viewModel = MainViewModel.getMainViewModel(this)

		primaryColor = ResourcesCompat.getColor(resources, R.color.primary, null)
		white = ResourcesCompat.getColor(resources, R.color.white, null)

		adapter = ViewPagerAdapter(this)
		_binding.vpMain.adapter = adapter
		_binding.vpMain.isUserInputEnabled = false

		disabledMenu()
		setFragments()
		setPager()
		setMenu()
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode == Activity.RESULT_OK) {
			when (requestCode){
				PurchaseActivity.NEW_PURCHASE -> {
					if (data != null) {
						val purchase: PurchaseWithProduct? = data.getSerializableExtra(EXTRA_PURCHASE) as PurchaseWithProduct?
						if (purchase != null) {
							addPurchase(purchase)
						}
					}
				}
			}
		}
	}

	override fun onResume() {
		super.onResume()
		checkDate()
	}

	private fun checkDate() {
		if (showedDate == currentDate) return
		setFragmentsDate(currentDate)
	}

	private fun setFragmentsDate(date: String) {
		showedDate = currentDate
		recapFragment.setDate(date)
		onProcessFragment.setDate(date)
		notPayFragment.setDate(date)
		cancelFragment.setDate(date)
		doneFragment.setDate(date)
	}

	private fun setFragments() {
		onProcessFragment = OnProcessFragment(showedDate)
		recapFragment = DailyRecapFragment(showedDate)
		notPayFragment = NotPayFragment(showedDate)
		cancelFragment = CancelFragment(showedDate)
		doneFragment = DoneFragment(showedDate)
	}

	private fun disabledMenu() {
		_menu.menuBack.visibility = View.GONE
		_menu.tvMenuDate.visibility = View.GONE
		_menu.menuDate.visibility = View.GONE

		if (isNotAdmin()) {
			_menu.menuPurchase.visibility = View.GONE
			_menu.menuHistory.visibility = View.GONE
			_menu.menuMaster.visibility = View.GONE
			_menu.tvMenuData.visibility = View.GONE
		}
	}

	private fun isNotAdmin(): Boolean {
		return User.isNotAdmin(userPref.userAuthority)
	}

	private fun setMenu() {
		_menu.menuOrder.setOnClickListener { setMenu(it, 0, true) }
		_menu.menuNotPay.setOnClickListener { setMenu(it, 1, true) }
		_menu.menuDone.setOnClickListener { setMenu(it, 2, true) }
		_menu.menuCancel.setOnClickListener { setMenu(it, 3, true) }
		_menu.menuRecap.setOnClickListener { setMenu(it, 4, true) }
		_menu.menuPurchase.setOnClickListener { setMenu(it, 5, true) }
		_menu.menuHistory.setOnClickListener { setMenu(it, 6, false) }
		_menu.menuMaster.setOnClickListener { setMenu(it, 7, false) }
		_menu.menuSetting.setOnClickListener { setMenu(it, 8, false) }

		_menu.menuOrder.performClick()
	}

	private fun addPurchase(purchase: PurchaseWithProduct){
		Purchase.add(this)
		viewModel.newPurchase(purchase) {}
	}

	private fun setPager() {
		val arrayList: ArrayList<FragmentWithTitle> = ArrayList()
		arrayList.add(0, FragmentWithTitle("", onProcessFragment))
		arrayList.add(1, FragmentWithTitle("", notPayFragment))
		arrayList.add(2, FragmentWithTitle("", doneFragment))
		arrayList.add(3, FragmentWithTitle("", cancelFragment))
		arrayList.add(4, FragmentWithTitle("", recapFragment))
		arrayList.add(5, FragmentWithTitle("", purchaseFragment))
		arrayList.add(6, FragmentWithTitle("", historyFragment))
		arrayList.add(7, FragmentWithTitle("", masterFragment))
		arrayList.add(8, FragmentWithTitle("", settingFragment))

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
					startActivity(Intent(this, OrderActivity::class.java))
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
