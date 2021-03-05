package com.socialite.solite_pos.view.main.menu.master

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.Supplier
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.databinding.ActivityListMasterBinding
import com.socialite.solite_pos.view.viewmodel.MainViewModel.Companion.getViewModel
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.view.main.menu.adapter.master.PaymentMasterAdapter
import com.socialite.solite_pos.view.main.menu.adapter.master.SupplierMasterAdapter
import com.socialite.solite_pos.view.main.menu.adapter.master.category.CategoryMasterAdapter
import com.socialite.solite_pos.view.main.menu.adapter.master.category.CategoryProductMasterAdapter
import com.socialite.solite_pos.view.main.menu.adapter.master.variant.VariantMasterAdapter
import com.socialite.solite_pos.view.main.menu.adapter.master.variant.VariantProductMasterAdapter
import com.socialite.solite_pos.view.main.menu.master.bottom.CategoryMasterFragment
import com.socialite.solite_pos.view.main.menu.master.bottom.PaymentMasterFragment
import com.socialite.solite_pos.view.main.menu.master.bottom.SupplierMasterFragment
import com.socialite.solite_pos.view.main.menu.master.bottom.VariantMasterFragment
import com.socialite.solite_pos.view.viewmodel.MainViewModel
import com.socialite.solite_pos.vo.Status

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
		const val SUPPLIER = 2
		const val VARIANT = 3
		const val PAYMENT = 4
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
			SUPPLIER -> setSupplier()
			else -> throw IllegalArgumentException("Invalid type of master type")
		}
	}

	private fun setSupplier(){
		_binding.fabLmNewData.text = "Tambah Supplier"
		_binding.tvLmTitle.text = "Supplier"

		val adapter = SupplierMasterAdapter(supportFragmentManager)
		_binding.rvListMaster.adapter = adapter

		getSuppliers { adapter.items = it }

		if (code == RQ_CODE){
			_binding.fabLmNewData.hide()
		}else{
			_binding.fabLmNewData.setOnClickListener {
				SupplierMasterFragment(null).show(supportFragmentManager, "detail-supplier")
			}
		}
	}

	private fun getSuppliers(callback: (ArrayList<Supplier>) -> Unit){
		viewModel.suppliers.observe(this) {
			when(it.status){
				Status.LOADING-> {}
				Status.SUCCESS -> {
					callback.invoke(ArrayList(it.data))
				}
				Status.ERROR -> {}
			}
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
			when(it.status){
				Status.SUCCESS -> {
					callback.invoke(ArrayList(it.data))
				}
				else -> {}
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

				getVariants(product!!.isMix) { adapter.items = it }
			}
		}else{
			_binding.fabLmNewData.setOnClickListener {
				VariantMasterFragment(null).show(supportFragmentManager, "detail-variant")
			}

			val adapter = VariantMasterAdapter(this)
			_binding.rvListMaster.adapter = adapter

			getVariants(null) { adapter.items = it }
		}
	}

	private fun getVariants(isMix: Boolean?, callback: ((ArrayList<Variant>) -> Unit)){
		viewModel.variants.observe(this){
			when(it.status){
				Status.LOADING -> {}
				Status.SUCCESS -> {
					if (!it.data.isNullOrEmpty()){
						val variants: ArrayList<Variant> = ArrayList()
						for (item in it.data){
							if (isMix != null){
								if (item.isMix == isMix){
									variants.add(item)
								}
							}else{
								variants.add(item)
							}
						}
						callback.invoke(variants)
					}
				}
				Status.ERROR -> {}
			}
		}
	}

	private fun setPayment(){
		_binding.fabLmNewData.text = "Tambah Pembayaran"
		_binding.tvLmTitle.text = "Pembayaran"
		_binding.fabLmNewData.setOnClickListener {
			PaymentMasterFragment(null).show(supportFragmentManager, "detail-variant")
		}

		val adapter = PaymentMasterAdapter(viewModel, supportFragmentManager)
		_binding.rvListMaster.adapter = adapter

		viewModel.payments.observe(this) {
			when(it.status){
				Status.LOADING -> {}
				Status.SUCCESS -> {
					adapter.items = ArrayList(it.data)
				}
				Status.ERROR -> {}
			}
		}
	}
}
