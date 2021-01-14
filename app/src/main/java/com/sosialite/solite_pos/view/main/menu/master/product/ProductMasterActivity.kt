package com.sosialite.solite_pos.view.main.menu.master.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

		adapter = ViewPagerAdapter(supportFragmentManager)
		_binding.vpProductMaster.adapter = adapter
		_binding.tabProductMaster.setupWithViewPager(_binding.vpProductMaster)

		getCategories()

		_binding.btnPmBack.setOnClickListener { onBackPressed() }
		_binding.fabPmNewProduct.setOnClickListener {
			ProductMasterFragment().show(supportFragmentManager, "detail-product")
		}
	}

	private fun getCategories(){
		viewModel.getCategories(Category.getFilter(Category.ALL)).observe(this, {
			if (!it.isNullOrEmpty()){
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

		adapter.setData(arrayList)
		_binding.vpProductMaster.offscreenPageLimit = adapter.count-1
		_binding.btnPmBack.setOnClickListener { onBackPressed() }
	}
}
