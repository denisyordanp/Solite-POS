package com.socialite.solite_pos.view.main.menu.master.detail

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.socialite.solite_pos.databinding.ActivityVariantOptionBinding
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.adapters.recycleView.variant.VariantOptionMasterAdapter
import com.socialite.solite_pos.view.main.menu.master.bottom.VariantOptionFragment
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import com.socialite.solite_pos.vo.Status

class VariantOptionActivity : SocialiteActivity() {

	private lateinit var _binding: ActivityVariantOptionBinding
	private lateinit var adapter: VariantOptionMasterAdapter
	private lateinit var viewModel: ProductViewModel

	private var product: Product? = null
	private var variant: Variant? = null

	companion object{
		const val VARIANT = "variant"
		const val PRODUCT = "product"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityVariantOptionBinding.inflate(layoutInflater)
		setContentView(_binding.root)

		product = intent.getSerializableExtra(PRODUCT) as Product?
		variant = intent.getSerializableExtra(VARIANT) as Variant?

		viewModel = ProductViewModel.getMainViewModel(this)

		adapter = VariantOptionMasterAdapter(product, this)
		_binding.rvVoOptions.layoutManager = LinearLayoutManager(this)
		_binding.rvVoOptions.adapter = adapter

		setData()

		_binding.btnVoBack.setOnClickListener { onBackPressed() }
		_binding.fabVoNewOption.setOnClickListener {
			VariantOptionFragment(variant?.id).show(supportFragmentManager, "detail-variant-option")
		}
	}

	private fun setData(){
		if (variant != null){
			val title = "Pilihan ${variant?.name}"
			_binding.tvVoTitle.text = title

			val query: SimpleSQLiteQuery = if (product != null){
				_binding.fabVoNewOption.hide()
				VariantOption.getFilter(variant!!.id, VariantOption.ACTIVE)
			}else{
				_binding.fabVoNewOption.show()
				VariantOption.getFilter(variant!!.id, VariantOption.ALL)
			}

			viewModel.getVariantOptions(query).observe(this, {
				when(it.status){
					Status.SUCCESS -> adapter.items = ArrayList(it.data)
					else -> {}
				}
			})
		}
	}
}
