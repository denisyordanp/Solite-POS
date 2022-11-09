package com.socialite.solite_pos.view.main.menu.master

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialite.solite_pos.adapters.recycleView.category.CategoryMasterAdapter
import com.socialite.solite_pos.adapters.recycleView.category.CategoryProductMasterAdapter
import com.socialite.solite_pos.adapters.recycleView.payment.PaymentMasterAdapter
import com.socialite.solite_pos.adapters.recycleView.supplier.SupplierMasterAdapter
import com.socialite.solite_pos.adapters.recycleView.variant.VariantMasterAdapter
import com.socialite.solite_pos.adapters.recycleView.variant.VariantProductMasterAdapter
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.databinding.ActivityListMasterBinding
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.view.main.menu.master.bottom.CategoryMasterFragment
import com.socialite.solite_pos.view.main.menu.master.bottom.PaymentMasterFragment
import com.socialite.solite_pos.view.main.menu.master.bottom.SupplierMasterFragment
import com.socialite.solite_pos.view.main.menu.master.bottom.VariantMasterFragment
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import kotlinx.coroutines.launch

class ListMasterActivity : SocialiteActivity() {

    private lateinit var _binding: ActivityListMasterBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var mainViewModel: MainViewModel

    private var product: Product? = null
    private var type: Int = 0
    private var code: Int = 0

    companion object {
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

        mainViewModel = MainViewModel.getMainViewModel(this)
        productViewModel = ProductViewModel.getMainViewModel(this)
        _binding.rvListMaster.layoutManager = LinearLayoutManager(this)

        code = intent.getIntExtra(REQUEST_CODE, 0)
        product = intent.getSerializableExtra(PRODUCT) as Product?

        checkData()

        _binding.btnLmBack.setOnClickListener { onBackPressed() }
    }

    private fun checkData() {
        type = intent.getIntExtra(TYPE, 0)
        when (type) {
            CATEGORY -> setCategory()
            VARIANT -> setVariant()
            PAYMENT -> setPayment()
            SUPPLIER -> setSupplier()
            else -> throw IllegalArgumentException("Invalid type of master type")
        }
    }

    private fun setSupplier() {
        _binding.fabLmNewData.text = "Tambah Supplier"
        _binding.tvLmTitle.text = "Supplier"

        val adapter = SupplierMasterAdapter(supportFragmentManager)
        _binding.rvListMaster.adapter = adapter

        if (code == RQ_CODE) {
            _binding.fabLmNewData.hide()
        } else {
            _binding.fabLmNewData.setOnClickListener {
                SupplierMasterFragment(null).show(supportFragmentManager, "detail-supplier")
            }
        }

        lifecycleScope.launch {
            mainViewModel.suppliers
                .collect {
                    adapter.items = ArrayList(it)
                }
        }
    }

    private fun setCategory() {
        _binding.fabLmNewData.text = "Tambah Kategori"
        _binding.tvLmTitle.text = "Kategori"

        if (code == RQ_CODE) {
            _binding.fabLmNewData.hide()

            val adapter = CategoryProductMasterAdapter {
                setCategoryResult(it)
            }
            _binding.rvListMaster.adapter = adapter

            getCategories { adapter.setCategories(it) }
        } else {
            _binding.fabLmNewData.setOnClickListener {
                CategoryMasterFragment(null).show(supportFragmentManager, "detail-category")
            }

            val adapter = CategoryMasterAdapter(this)
            _binding.rvListMaster.adapter = adapter

            getCategories { adapter.setCategories(it) }
        }
    }

    private fun getCategories(callback: (ArrayList<Category>) -> Unit) {
        lifecycleScope.launch {
            val query = Category.getFilter(Category.ALL)
            productViewModel.getCategories(query)
                .collect {
                    callback.invoke(ArrayList(it))
                }
        }
    }

    private fun setCategoryResult(category: Category) {
        val intent = Intent()
        intent.putExtra(REQUEST_CODE, category)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun setVariant() {
        _binding.fabLmNewData.text = "Tambah Varian"
        _binding.tvLmTitle.text = "Varian"

        if (code == RQ_CODE) {
            _binding.fabLmNewData.hide()

            if (product != null) {
                val adapter = VariantProductMasterAdapter(product!!, this)
                _binding.rvListMaster.adapter = adapter

                getVariants(product!!.isMix) { adapter.items = it }
            }
        } else {
            _binding.fabLmNewData.setOnClickListener {
                VariantMasterFragment(null).show(supportFragmentManager, "detail-variant")
            }

            val adapter = VariantMasterAdapter(this)
            _binding.rvListMaster.adapter = adapter

            getVariants(null) { adapter.items = it }
        }
    }

    private fun getVariants(isMix: Boolean?, callback: ((ArrayList<Variant>) -> Unit)) {
        lifecycleScope.launch {
            productViewModel.variants
                .collect {
                    if (it.isNotEmpty()) {
                        val variants: ArrayList<Variant> = ArrayList()
                        for (item in it) {
                            if (isMix != null) {
                                if (item.isMix == isMix) {
                                    variants.add(item)
                                }
                            } else {
                                variants.add(item)
                            }
                        }
                        callback.invoke(variants)
                    }
                }
        }
    }

    private fun setPayment() {
        _binding.fabLmNewData.text = "Tambah Pembayaran"
        _binding.tvLmTitle.text = "Pembayaran"
        _binding.fabLmNewData.setOnClickListener {
            PaymentMasterFragment(null).show(supportFragmentManager, "detail-variant")
        }

        val adapter = PaymentMasterAdapter(this)
        _binding.rvListMaster.adapter = adapter

        lifecycleScope.launch {
            val query = Payment.filter(Payment.ALL)
            mainViewModel.getPayments(query).collect {
                adapter.setPayments(it)
            }
        }
    }
}
