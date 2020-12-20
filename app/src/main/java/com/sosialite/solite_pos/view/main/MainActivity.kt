package com.sosialite.solite_pos.view.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sosialite.solite_pos.data.source.local.entity.Product
import com.sosialite.solite_pos.data.source.local.entity.helper.DetailOrder
import com.sosialite.solite_pos.databinding.ActivityMainBinding
import com.sosialite.solite_pos.databinding.MainMenuBinding
import com.sosialite.solite_pos.utils.printer.PrintBill
import com.sosialite.solite_pos.view.main.menu.OnProcessFragment

class MainActivity : AppCompatActivity() {

	private lateinit var _binding: ActivityMainBinding
	private lateinit var printBill: PrintBill

	private lateinit var onProcess: OnProcessFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
		_binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

		onProcess = OnProcessFragment.instance

		printBill = PrintBill(this)

		_binding.mainMenu.menuOrder.setOnClickListener {
			supportFragmentManager.beginTransaction().add(_binding.fmMain.id, onProcess).commit()
		}

//		binding.btnPrint.setOnClickListener{
//			val pay = binding.edtPay.text.toString().toInt()
//			if (checkReturn(getProduct(), pay)){
//				printBill.doPrint(getProduct(), pay)
//			}
//		}
    }

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		printBill.onSetSocket()
	}

	private fun checkReturn(detailOrders: ArrayList<DetailOrder>, pay: Int): Boolean{
		var total = 0
		for(order in detailOrders){
			total += (order.amount * order.product.price)
		}
		return if (pay < total){
			Toast.makeText(this, "Uang bayar kurang", Toast.LENGTH_LONG).show()
			false
		}else{
			true
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
}
