package com.sosialite.solite_pos.view.main.menu.master.product

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.data.source.local.entity.room.master.Category
import com.sosialite.solite_pos.databinding.ActivityProductMasterBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.sosialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter
import com.sosialite.solite_pos.view.main.menu.master.bottom.ProductMasterFragment
import com.sosialite.solite_pos.view.viewmodel.MainViewModel


class ProductMasterActivity : AppCompatActivity() {

	private lateinit var _binding: ActivityProductMasterBinding
	private lateinit var adapter: ViewPagerAdapter
	private lateinit var viewModel: MainViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityProductMasterBinding.inflate(layoutInflater)
		setContentView(_binding.root)

		viewModel = getViewModel(this)

		adapter = ViewPagerAdapter(this)
		_binding.vpProductMaster.adapter = adapter

		getCategories()

		_binding.btnPmBack.setOnClickListener { onBackPressed() }
		_binding.fabPmNewProduct.setOnClickListener {
			ProductMasterFragment().show(supportFragmentManager, "detail-product")
		}
	}

	private fun getCategories(){
		viewModel.getCategories(Category.getFilter(Category.ALL)).observe(this, {
			if (!it.isNullOrEmpty()) {
				setAdapter(ArrayList(it))
			}
		})
	}

	private fun setAdapter(categories: ArrayList<Category>){
		val arrayList: ArrayList<FragmentWithTitle> = ArrayList()
		for (ctg in categories){
			val fragment = ListProductMasterFragment(ctg)
			arrayList.add(FragmentWithTitle(ctg.name, fragment))
		}

		TabLayoutMediator(_binding.tabProductMaster, _binding.vpProductMaster) { tab, position ->
			tab.text = arrayList[position].title
			_binding.vpProductMaster.setCurrentItem(tab.position, true)
		}.attach()
		adapter.setData(arrayList)
		_binding.vpProductMaster.offscreenPageLimit = adapter.itemCount
		_binding.btnPmBack.setOnClickListener { onBackPressed() }
	}
}
