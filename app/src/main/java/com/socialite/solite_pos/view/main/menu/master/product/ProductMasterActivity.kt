package com.socialite.solite_pos.view.main.menu.master.product

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.databinding.ActivityProductMasterBinding
import com.socialite.solite_pos.view.viewModel.MainViewModel.Companion.getMainViewModel
import com.socialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter
import com.socialite.solite_pos.view.main.menu.master.bottom.ProductMasterFragment
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.vo.Status


class ProductMasterActivity : SocialiteActivity() {

	private lateinit var _binding: ActivityProductMasterBinding
	private lateinit var adapter: ViewPagerAdapter
	private lateinit var viewModel: MainViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityProductMasterBinding.inflate(layoutInflater)
		setContentView(_binding.root)

		viewModel = getMainViewModel(this)

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
			when(it.status){
				Status.LOADING -> {}
				Status.SUCCESS -> {
					if (!it.data.isNullOrEmpty()){
						setAdapter(ArrayList(it.data))
					}
				}
				Status.ERROR -> {}
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
