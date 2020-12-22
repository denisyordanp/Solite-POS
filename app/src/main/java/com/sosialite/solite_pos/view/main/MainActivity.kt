package com.sosialite.solite_pos.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.data.source.local.entity.Product
import com.sosialite.solite_pos.data.source.local.entity.helper.DetailOrder
import com.sosialite.solite_pos.databinding.ActivityMainBinding
import com.sosialite.solite_pos.utils.printer.PrintBill
import com.sosialite.solite_pos.view.main.menu.DoneFragment
import com.sosialite.solite_pos.view.main.menu.NotPayFragment
import com.sosialite.solite_pos.view.main.menu.OnProcessFragment

class MainActivity : AppCompatActivity() {

	private lateinit var _binding: ActivityMainBinding
	private lateinit var printBill: PrintBill

	private val primaryColor: Int = ResourcesCompat.getColor(resources, R.color.primary, null)
	private val white: Int = ResourcesCompat.getColor(resources, R.color.white, null)

	private val onProcessFragment: OnProcessFragment = OnProcessFragment.instance
	private val notPayFragment: NotPayFragment = NotPayFragment.instance
	private val doneFragment: DoneFragment = DoneFragment.instance

	private lateinit var transaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
		_binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

		transaction = supportFragmentManager.beginTransaction()

		setDefaultMenu()

		printBill = PrintBill(this)

		_binding.mainMenu.menuOrder.setOnClickListener { setMenu(it, onProcessFragment) }
		_binding.mainMenu.menuNotPay.setOnClickListener { setMenu(it, notPayFragment) }
		_binding.mainMenu.menuDone.setOnClickListener { setMenu(it, doneFragment) }

		_binding.fabMainNewOrder.setOnClickListener { checkReturn(getProduct(), 75000) }
    }

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		printBill.onSetSocket()
	}

	private fun checkReturn(detailOrders: ArrayList<DetailOrder>, pay: Int){
		var total = 0
		for(order in detailOrders){
			total += (order.amount * order.product.price)
		}
		return if (pay < total){
			Toast.makeText(this, "Uang bayar kurang", Toast.LENGTH_LONG).show()
		}else{
			printBill.doPrint(getProduct(), pay)
		}
	}

	private fun getProduct(): ArrayList<DetailOrder>{
		val items: ArrayList<DetailOrder> = ArrayList()
		items.add(DetailOrder(Product("5412", "Angsio Ceker Ayam", 15000), 1))
		items.add(DetailOrder(Product("815", "Siomay Udang", 14000), 2))
		items.add(DetailOrder(Product("356", "Kulit Tahu Udang", 14000), 2))
		return items
	}

	override fun onDestroy() {
		super.onDestroy()
		printBill.onDestroy()
	}

	private fun setDefaultMenu(){
		transaction.add(_binding.fmMain.id, onProcessFragment).commit()
		_binding.mainMenu.menuOrder.setBackgroundColor(primaryColor)
	}

	private fun setMenu(v: View, fragment: Fragment){
		_binding.mainMenu.menuOrder.setBackgroundColor(white)
		_binding.mainMenu.menuNotPay.setBackgroundColor(white)
		_binding.mainMenu.menuDone.setBackgroundColor(white)
		_binding.mainMenu.menuOutMoney.setBackgroundColor(white)
		_binding.mainMenu.menuHistory.setBackgroundColor(white)
		v.setBackgroundColor(primaryColor)
		transaction.replace(_binding.fmMain.id, fragment).commit()
	}
}
