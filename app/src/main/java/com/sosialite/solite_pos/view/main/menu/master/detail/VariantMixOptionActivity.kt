package com.sosialite.solite_pos.view.main.menu.master.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.sosialite.solite_pos.data.source.local.entity.room.master.Category
import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import com.sosialite.solite_pos.data.source.local.entity.room.master.Variant
import com.sosialite.solite_pos.databinding.ActivityVariantMixOptionBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.sosialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.sosialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class VariantMixOptionActivity : SocialiteActivity() {

	private lateinit var _binding: ActivityVariantMixOptionBinding
	private lateinit var adapter: ViewPagerAdapter
	private lateinit var viewModel: MainViewModel

	private var product: Product? = null
	private var variant: Variant? = null

	companion object{
		const val PRODUCT = "product"
		const val VARIANT = "variant"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityVariantMixOptionBinding.inflate(layoutInflater)
		setContentView(_binding.root)

		viewModel = getViewModel(this)

		product = intent.getSerializableExtra(PRODUCT) as Product?
		variant = intent.getSerializableExtra(VARIANT) as Variant?

		adapter = ViewPagerAdapter(this)
		_binding.vpVmo.adapter = adapter

		setPageAdapter()

		_binding.btnVmoBack.setOnClickListener { onBackPressed() }
	}

	private fun setPageAdapter(){
		viewModel.getCategories(Category.getFilter(Category.ACTIVE)).observe(this, {
			if (!it.isNullOrEmpty()){
				val fragments: ArrayList<FragmentWithTitle> = ArrayList()
				for (ctg in it){
					if (ctg.isStock){
						val fragment = ProductMixVariantFragment(variant, ctg)
						fragments.add(FragmentWithTitle(ctg.name, fragment))
					}
				}
				setData(fragments)
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
