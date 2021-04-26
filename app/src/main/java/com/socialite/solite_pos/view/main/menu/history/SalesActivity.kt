package com.socialite.solite_pos.view.main.menu.history

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.socialite.solite_pos.R
import com.socialite.solite_pos.databinding.ActivitySalesBinding
import com.socialite.solite_pos.databinding.MainMenuBinding
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.utils.config.DateUtils.Companion.convertDateFromDate
import com.socialite.solite_pos.utils.config.DateUtils.Companion.currentDate
import com.socialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.view.main.helper.DatePickerFragment
import com.socialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter
import com.socialite.solite_pos.view.main.menu.main.*

class SalesActivity : SocialiteActivity() {

    private lateinit var _binding: ActivitySalesBinding
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var _menu: MainMenuBinding

    private var primaryColor: Int = 0
    private var white: Int = 0

    private lateinit var onProcessFragment: OnProcessFragment
    private lateinit var recapFragment: DailyRecapFragment
    private lateinit var notPayFragment: NotPayFragment
    private lateinit var cancelFragment: CancelFragment
    private lateinit var doneFragment: DoneFragment

    private lateinit var historyDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySalesBinding.inflate(layoutInflater)
        _menu = _binding.salesMenu
        setContentView(_binding.root)

        historyDate = currentDate

        primaryColor = ResourcesCompat.getColor(resources, R.color.primary, null)
        white = ResourcesCompat.getColor(resources, R.color.white, null)

        setDate()
        setFragments()
        disabledMenu()
        setAdapter()
        setPager()
        setMenu()

    }

    private fun setFragments() {
        onProcessFragment = OnProcessFragment(historyDate)
        recapFragment = DailyRecapFragment(historyDate)
        notPayFragment = NotPayFragment(historyDate)
        cancelFragment = CancelFragment(historyDate)
        doneFragment = DoneFragment(historyDate)
    }

    private fun setDate() {
        _menu.menuDate.text = convertDateFromDate(historyDate, DateUtils.dateWithDayFormat)
    }

    private fun disabledMenu() {
        _menu.menuSetting.visibility = View.GONE
        _menu.menuMaster.visibility = View.GONE
        _menu.menuHistory.visibility = View.GONE
        _menu.menuPurchase.visibility = View.GONE
        _menu.tvMenuData.visibility = View.GONE
        _menu.tvMenuApplication.visibility = View.GONE
    }

    private fun setAdapter() {
        adapter = ViewPagerAdapter(this)
        _binding.vpSales.adapter = adapter
        _binding.vpSales.isUserInputEnabled = false
    }

    private fun setPager() {
        val arrayList: ArrayList<FragmentWithTitle> = ArrayList()
        arrayList.add(0, FragmentWithTitle("", onProcessFragment))
        arrayList.add(1, FragmentWithTitle("", notPayFragment))
        arrayList.add(2, FragmentWithTitle("", doneFragment))
        arrayList.add(3, FragmentWithTitle("", cancelFragment))
        arrayList.add(4, FragmentWithTitle("", recapFragment))

        adapter.setData(arrayList)
        _binding.vpSales.offscreenPageLimit = adapter.itemCount
        _menu.menuOrder.setBackgroundColor(primaryColor)
    }

    private fun setMenu() {
        _menu.menuDate.setOnClickListener {
            DatePickerFragment(historyDate) {
                setFragmentsDate(it)
            }.show(supportFragmentManager, "select-date")
        }
        _menu.menuOrder.setOnClickListener { setMenu(it, 0) }
        _menu.menuNotPay.setOnClickListener { setMenu(it, 1) }
        _menu.menuDone.setOnClickListener { setMenu(it, 2) }
        _menu.menuCancel.setOnClickListener { setMenu(it, 3) }
        _menu.menuRecap.setOnClickListener { setMenu(it, 4) }
        _menu.menuBack.setOnClickListener { onBackPressed() }

        _menu.menuOrder.performClick()
    }

    private fun setFragmentsDate(date: String) {
        historyDate = date
        setDate()
        recapFragment.setDate(date)
        onProcessFragment.setDate(date)
        notPayFragment.setDate(date)
        cancelFragment.setDate(date)
        doneFragment.setDate(date)
    }

    private fun setMenu(v: View, pos: Int) {
        resetButton()
        v.setBackgroundColor(primaryColor)
        _binding.vpSales.setCurrentItem(pos, true)
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
}