package com.socialite.solite_pos.view.main.menu.master.detail

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.databinding.ActivityMasterVariantMixOptionBinding
import com.socialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.adapters.viewPager.ViewPagerAdapter
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import com.socialite.solite_pos.vo.Status

class VariantMasterMixOptionActivity : SocialiteActivity() {

	private lateinit var _binding: ActivityMasterVariantMixOptionBinding
	private lateinit var adapter: ViewPagerAdapter
	private lateinit var viewModel: ProductViewModel

	private var product: Product? = null
	private var variant: Variant? = null

	companion object{
		const val PRODUCT = "product"
		const val VARIANT = "variant"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityMasterVariantMixOptionBinding.inflate(layoutInflater)
		setContentView(_binding.root)

		viewModel = ProductViewModel.getMainViewModel(this)

		product = intent.getSerializableExtra(PRODUCT) as Product?
		variant = intent.getSerializableExtra(VARIANT) as Variant?

		adapter = ViewPagerAdapter(this)
		_binding.vpVmo.adapter = adapter

		setPageAdapter()

		_binding.btnVmoBack.setOnClickListener { onBackPressed() }
	}

	private fun setPageAdapter(){
		viewModel.getCategories(Category.getFilter(Category.ACTIVE)).observe(this, {
			when(it.status){
				Status.SUCCESS -> {
					if (!it.data.isNullOrEmpty()){
						val fragments: ArrayList<FragmentWithTitle> = ArrayList()
						for (ctg in it.data){
							if (ctg.isStock){
								val fragment = ProductMasterMixVariantFragment(variant, ctg)
								fragments.add(FragmentWithTitle(ctg.name, fragment))
							}
						}
						setData(fragments)
					}
				}
				else -> {}
			}
		})
	}

	private fun setData(fragments: ArrayList<FragmentWithTitle>){
		adapter.setData(fragments)
		_binding.vpVmo.offscreenPageLimit = adapter.itemCount
		TabLayoutMediator(_binding.tabVmo, _binding.vpVmo) { tab, position ->
			tab.text = fragments[position].title
			_binding.vpVmo.setCurrentItem(tab.position, true)
		}.attach()
	}
}
