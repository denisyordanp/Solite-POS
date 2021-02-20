package com.sosialite.solite_pos.view.main.menu.master.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.sqlite.db.SimpleSQLiteQuery
import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import com.sosialite.solite_pos.data.source.local.entity.room.master.Variant
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.sosialite.solite_pos.databinding.ActivityVariantOptionBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.sosialite.solite_pos.view.main.menu.adapter.master.variant.VariantOptionMasterAdapter
import com.sosialite.solite_pos.view.main.menu.master.bottom.VariantOptionFragment
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class VariantOptionActivity : SocialiteActivity() {

	private lateinit var _binding: ActivityVariantOptionBinding
	private lateinit var adapter: VariantOptionMasterAdapter
	private lateinit var viewModel: MainViewModel

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

		viewModel = getViewModel(this)

		adapter = VariantOptionMasterAdapter(product, viewModel, supportFragmentManager)
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
				if (!it.isNullOrEmpty()){
					adapter.items = ArrayList(it)
				}
			})
		}
	}
}
