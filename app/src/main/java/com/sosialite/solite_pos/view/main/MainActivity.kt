package com.sosialite.solite_pos.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.data.source.local.entity.helper.Order
import com.sosialite.solite_pos.databinding.ActivityMainBinding
import com.sosialite.solite_pos.utils.printer.PrintBill
import com.sosialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.sosialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.sosialite.solite_pos.view.main.menu.DoneFragment
import com.sosialite.solite_pos.view.main.menu.NotPayFragment
import com.sosialite.solite_pos.view.main.menu.OnProcessFragment
import com.sosialite.solite_pos.view.main.menu.SettingFragment
import com.sosialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter
import com.sosialite.solite_pos.view.main.menu.order.OrderActivity

class MainActivity : SocialiteActivity() {

	private lateinit var _binding: ActivityMainBinding
	private lateinit var printBill: PrintBill
	private lateinit var adapter: ViewPagerAdapter

	private var primaryColor: Int = 0
	private var white: Int = 0

	private val onProcessFragment: OnProcessFragment = OnProcessFragment.instance
	private val settingFragment: SettingFragment = SettingFragment.instance
	private val notPayFragment: NotPayFragment = NotPayFragment.instance
	private val doneFragment: DoneFragment = DoneFragment.instance

	companion object{
		const val EXTRA_ORDER = "extra_order"
	}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
		_binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

		printBill = PrintBill(this)

		primaryColor = ResourcesCompat.getColor(resources, R.color.primary, null)
		white = ResourcesCompat.getColor(resources, R.color.white, null)

		adapter = ViewPagerAdapter(supportFragmentManager)
		_binding.vpMain.adapter = adapter

		setPager()
		setMenu()

		_binding.fabMainNewOrder.setOnClickListener {
			startActivityForResult(
					Intent(this, OrderActivity::class.java)
							.putExtra(OrderActivity.ORDER_TYPE, OrderActivity.NEW_ORDER)
					, OrderActivity.NEW_ORDER_RQ_CODE)
		}
    }

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		when (requestCode){
			OrderActivity.NEW_ORDER_RQ_CODE -> {
				if (data != null){
					val order: Order? = data.getSerializableExtra(EXTRA_ORDER) as Order?
					if (order != null){
						addOrder(order)
					}
				}
			}
			PrintBill.REQUEST_CONNECT_BT -> printBill.onSetSocket()
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		printBill.onDestroy()
	}

	private fun setMenu(){
		_binding.mainMenu.menuOrder.setOnClickListener { setMenu(it, 0) }
		_binding.mainMenu.menuNotPay.setOnClickListener { setMenu(it, 1) }
		_binding.mainMenu.menuDone.setOnClickListener { setMenu(it, 2) }
		_binding.mainMenu.menuSetting.setOnClickListener { setMenu(it, 3) }
	}

	fun addOrder(order: Order){
		onProcessFragment.addItem(order)
	}

	private fun setPager(){
		val arrayList: ArrayList<FragmentWithTitle> = ArrayList()
		arrayList.add(FragmentWithTitle("", onProcessFragment))
		arrayList.add(FragmentWithTitle("", notPayFragment))
		arrayList.add(FragmentWithTitle("", doneFragment))
		arrayList.add(FragmentWithTitle("", settingFragment))

		adapter.setData(arrayList)
		_binding.vpMain.offscreenPageLimit = adapter.count-1
		_binding.mainMenu.menuOrder.setBackgroundColor(primaryColor)
	}

	private fun setMenu(v: View, pos: Int){
		resetButton()
		v.setBackgroundColor(primaryColor)
		_binding.vpMain.setCurrentItem(pos, true)
	}

	private fun resetButton(){
		_binding.mainMenu.menuOutMoney.setBackgroundColor(white)
		_binding.mainMenu.menuSetting.setBackgroundColor(white)
		_binding.mainMenu.menuHistory.setBackgroundColor(white)
		_binding.mainMenu.menuNotPay.setBackgroundColor(white)
		_binding.mainMenu.menuOrder.setBackgroundColor(white)
		_binding.mainMenu.menuDone.setBackgroundColor(white)
	}

	fun printBill(order: Order){
		printBill.doPrint(order)
	}
}
