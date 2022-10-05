package com.socialite.solite_pos.view.main.opening

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.socialite.solite_pos.R
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.master.Purchase
import com.socialite.solite_pos.data.source.local.entity.room.master.User
import com.socialite.solite_pos.databinding.ActivityMainBinding
import com.socialite.solite_pos.databinding.MainMenuBinding
import com.socialite.solite_pos.utils.config.DateUtils.Companion.currentDate
import com.socialite.solite_pos.utils.preference.UserPref
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.view.main.menu.main.CancelFragment
import com.socialite.solite_pos.view.main.menu.main.DailyRecapFragment
import com.socialite.solite_pos.view.main.menu.main.DoneFragment
import com.socialite.solite_pos.view.main.menu.main.HistoryFragment
import com.socialite.solite_pos.view.main.menu.main.MasterFragment
import com.socialite.solite_pos.view.main.menu.main.NotPayFragment
import com.socialite.solite_pos.view.main.menu.main.OnProcessFragment
import com.socialite.solite_pos.view.main.menu.main.PurchaseFragment
import com.socialite.solite_pos.view.main.menu.main.SettingFragment
import com.socialite.solite_pos.view.main.menu.order.OrderActivity
import com.socialite.solite_pos.view.main.menu.outcome.DetailOutcomeActivity
import com.socialite.solite_pos.view.main.menu.purchase.PurchaseActivity
import com.socialite.solite_pos.view.utils.MainMenus
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel.Companion.getOrderViewModel


class MainActivity : SocialiteActivity() {

    private lateinit var _binding: ActivityMainBinding
    private lateinit var orderViewModel: OrderViewModel
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

        initializeMenus()
        setFragments()
        setMenus()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PurchaseActivity.NEW_PURCHASE -> {
                    if (data != null) {
                        val purchase: PurchaseWithProduct? =
                            data.getSerializableExtra(EXTRA_PURCHASE) as PurchaseWithProduct?
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

    private fun initializeMenus() {
        _menu.menuBack.visibility = View.GONE
        _menu.tvMenuDate.visibility = View.GONE
        _menu.menuDate.visibility = View.GONE

        if (isNotAdmin()) {
            _menu.menuPurchase.visibility = View.GONE
            _menu.menuHistory.visibility = View.GONE
            _menu.menuMaster.visibility = View.GONE
            _menu.tvMenuData.visibility = View.GONE
        }
        _menu.menuOrder.setBackgroundColor(primaryColor)
    }

    private fun isNotAdmin(): Boolean {
        return User.isNotAdmin(userPref.userAuthority)
    }

    private fun setMenus() {
        _menu.menuOrder.setOnClickListener { setMenus(it, MainMenus.ORDER, true) }
        _menu.menuNotPay.setOnClickListener { setMenus(it, MainMenus.NOT_PAY, true) }
        _menu.menuDone.setOnClickListener { setMenus(it, MainMenus.DONE, true) }
        _menu.menuCancel.setOnClickListener { setMenus(it, MainMenus.CANCELED, true) }
        _menu.menuRecap.setOnClickListener { setMenus(it, MainMenus.DAILY_RECAP, true) }
        _menu.menuPurchase.setOnClickListener { setMenus(it, MainMenus.PURCHASE, true) }
        _menu.menuHistory.setOnClickListener { setMenus(it, MainMenus.HISTORY, false) }
        _menu.menuMaster.setOnClickListener { setMenus(it, MainMenus.MASTER, false) }
        _menu.menuSetting.setOnClickListener { setMenus(it, MainMenus.SETTING, false) }

        _menu.menuOrder.performClick()
    }

    private fun addPurchase(purchase: PurchaseWithProduct) {
        Purchase.add(this)
        viewModel.newPurchase(purchase)
    }

    private fun doTransaction(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .disallowAddToBackStack()
            .commit()
    }

    private fun setMenus(v: View, menu: MainMenus, isFabShow: Boolean) {
        when (menu) {
            MainMenus.ORDER -> {
                doTransaction(onProcessFragment)
                setFab(FabButtonType.NEW_ORDER)
            }

            MainMenus.NOT_PAY -> {
                doTransaction(notPayFragment)
                setFab(FabButtonType.NEW_ORDER)
            }

            MainMenus.DONE -> {
                doTransaction(doneFragment)
                setFab(FabButtonType.NEW_ORDER)
            }

            MainMenus.CANCELED -> {
                doTransaction(cancelFragment)
                setFab(FabButtonType.NEW_ORDER)
            }

            MainMenus.DAILY_RECAP -> {
                doTransaction(recapFragment)
                setFab(FabButtonType.OUTCOMES)
            }

            MainMenus.PURCHASE -> {
                doTransaction(purchaseFragment)
                setFab(FabButtonType.NEW_PURCHASE)
            }

            MainMenus.HISTORY -> {
                doTransaction(historyFragment)
            }

            MainMenus.MASTER -> {
                doTransaction(masterFragment)
            }

            MainMenus.SETTING -> {
                doTransaction(settingFragment)
            }
        }
        if (isFabShow) _binding.fabMainNewOrder.show() else _binding.fabMainNewOrder.hide()
        resetButton()
        v.setBackgroundColor(primaryColor)
    }

    private fun resetButton() {
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

    private fun setFab(type: FabButtonType) {
        when (type) {
            FabButtonType.NEW_ORDER -> {
                _binding.fabMainNewOrder.setOnClickListener {
                    startActivity(Intent(this, OrderActivity::class.java))
                }
            }

            FabButtonType.OUTCOMES -> {
                _binding.fabMainNewOrder.setOnClickListener {
                    startActivity(Intent(this, DetailOutcomeActivity::class.java))
                }
            }

            FabButtonType.NEW_PURCHASE -> {
                _binding.fabMainNewOrder.setOnClickListener {
                    startActivityForResult(
                        Intent(this, PurchaseActivity::class.java),
                        PurchaseActivity.NEW_PURCHASE
                    )
                }
            }
        }
        _binding.fabMainNewOrder.text = getString(type.title)
    }

    enum class FabButtonType(@StringRes val title: Int) {
        NEW_ORDER(R.string.new_order),
        OUTCOMES(R.string.outcomes),
        NEW_PURCHASE(R.string.new_purchase),
    }

}
