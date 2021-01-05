package com.sosialite.solite_pos.view.main.menu.master.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sosialite.solite_pos.databinding.ActivityProductMasterBinding
import com.sosialite.solite_pos.utils.tools.helper.DataDummy
import com.sosialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.sosialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter
import com.sosialite.solite_pos.view.main.menu.order.ProductOrderFragment

class ProductMasterActivity : AppCompatActivity() {

	private lateinit var _binding: ActivityProductMasterBinding
	private lateinit var adapter: ViewPagerAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityProductMasterBinding.inflate(layoutInflater)
		setContentView(_binding.root)

		adapter = ViewPagerAdapter(supportFragmentManager)
		_binding.vpProductMaster.adapter = adapter
		_binding.tabProductMaster.setupWithViewPager(_binding.vpProductMaster)

		setAdapter()
	}

	private fun setAdapter(){
		val arrayList: ArrayList<FragmentWithTitle> = ArrayList()
		for (ctg in DataDummy.DataCategory.allCategory){
			val fragment = ProductMasterFragment(ctg)
			arrayList.add(FragmentWithTitle(ctg.name, fragment))
		}

		adapter.setData(arrayList)
		_binding.vpProductMaster.offscreenPageLimit = adapter.count-1
		_binding.btnPmBack.setOnClickListener { onBackPressed() }
	}
}
