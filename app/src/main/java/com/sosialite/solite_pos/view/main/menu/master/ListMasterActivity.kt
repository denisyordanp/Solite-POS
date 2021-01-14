package com.sosialite.solite_pos.view.main.menu.master

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosialite.solite_pos.data.source.local.entity.room.master.Category
import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import com.sosialite.solite_pos.data.source.local.entity.room.master.Variant
import com.sosialite.solite_pos.databinding.ActivityListMasterBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.sosialite.solite_pos.view.main.menu.adapter.master.category.CategoryMasterAdapter
import com.sosialite.solite_pos.view.main.menu.adapter.master.category.CategoryProductMasterAdapter
import com.sosialite.solite_pos.view.main.menu.adapter.master.PaymentMasterAdapter
import com.sosialite.solite_pos.view.main.menu.adapter.master.variant.VariantMasterAdapter
import com.sosialite.solite_pos.view.main.menu.adapter.master.variant.VariantProductMasterAdapter
import com.sosialite.solite_pos.view.main.menu.master.bottom.CategoryMasterFragment
import com.sosialite.solite_pos.view.main.menu.master.bottom.PaymentMasterFragment
import com.sosialite.solite_pos.view.main.menu.master.bottom.VariantMasterFragment
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class ListMasterActivity : SocialiteActivity() {

	private lateinit var _binding: ActivityListMasterBinding
	private lateinit var viewModel: MainViewModel

	private var product: Product? = null
	private var type: Int = 0
	private var code: Int = 0

	companion object{
		const val TYPE = "type"

		const val REQUEST_CODE = "rq_code"
		const val PRODUCT = "product"
		const val RQ_CODE = 999

		const val CATEGORY = 1
		const val VARIANT = 2
		const val PAYMENT = 3
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityListMasterBinding.inflate(layoutInflater)
		setContentView(_binding.root)

		viewModel = getViewModel(this)
		_binding.rvListMaster.layoutManager = LinearLayoutManager(this)

		code = intent.getIntExtra(REQUEST_CODE, 0)
		product = intent.getSerializableExtra(PRODUCT) as Product?

		checkData()

		_binding.btnLmBack.setOnClickListener { onBackPressed() }
	}

	private fun checkData(){
		type = intent.getIntExtra(TYPE, 0)
		when(type){
			CATEGORY -> setCategory()
			VARIANT -> setVariant()
			PAYMENT -> setPayment()
			else -> throw IllegalArgumentException("Invalid type of master type")
		}
	}

	private fun setCategory(){
		_binding.fabLmNewData.text = "Tambah Kategori"
		_binding.tvLmTitle.text = "Kategori"

		if (code == RQ_CODE){
			_binding.fabLmNewData.hide()

			val adapter = CategoryProductMasterAdapter{
				setCategoryResult(it)
			}
			_binding.rvListMaster.adapter = adapter

			getCategories { adapter.items = it }
		}else{
			_binding.fabLmNewData.setOnClickListener {
				CategoryMasterFragment(null).show(supportFragmentManager, "detail-category")
			}

			val adapter = CategoryMasterAdapter(viewModel, supportFragmentManager)
			_binding.rvListMaster.adapter = adapter

			getCategories { adapter.items = it }
		}
	}

	private fun getCategories(callback: (ArrayList<Category>) -> Unit){
		viewModel.getCategories(Category.getFilter(Category.ALL)).observe(this, {
			if (!it.isNullOrEmpty()){
				callback.invoke(ArrayList(it))
			}
		})
	}

	private fun setCategoryResult(category: Category){
		val intent = Intent()
		intent.putExtra(REQUEST_CODE, category)
		setResult(RESULT_OK, intent)
		finish()
	}

	private fun setVariant(){
		_binding.fabLmNewData.text = "Tambah Varian"
		_binding.tvLmTitle.text = "Varian"

		if (code == RQ_CODE){
			_binding.fabLmNewData.hide()

			if (product != null){
				val adapter = VariantProductMasterAdapter(product!!, this)
				_binding.rvListMaster.adapter = adapter

				getVariants { adapter.items = it }
			}
		}else{
			_binding.fabLmNewData.setOnClickListener {
				VariantMasterFragment(null).show(supportFragmentManager, "detail-variant")
			}

			val adapter = VariantMasterAdapter(this)
			_binding.rvListMaster.adapter = adapter

			getVariants { adapter.items = it }
		}
	}

	private fun getVariants(callback: ((ArrayList<Variant>) -> Unit)){
		viewModel.variants.observe(this, {
			if (!it.isNullOrEmpty()){
				callback.invoke(ArrayList(it))
			}
		})
	}

	private fun setPayment(){
		_binding.fabLmNewData.text = "Tambah Pembayaran"
		_binding.tvLmTitle.text = "Pembayaran"
		_binding.fabLmNewData.setOnClickListener {
			PaymentMasterFragment(null).show(supportFragmentManager, "detail-variant")
		}

		val adapter = PaymentMasterAdapter(viewModel, supportFragmentManager)
		_binding.rvListMaster.adapter = adapter

		viewModel.payments.observe(this, {
			if (!it.isNullOrEmpty()){
				adapter.items = ArrayList(it)
			}
		})
	}
}
