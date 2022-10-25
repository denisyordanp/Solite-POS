package com.socialite.solite_pos.adapters.recycleView.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.databinding.RvProductMasterBinding
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah
import com.socialite.solite_pos.utils.tools.RecycleViewDiffUtils
import com.socialite.solite_pos.view.main.menu.master.bottom.ProductMasterFragment
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import kotlinx.coroutines.launch

class ProductMasterAdapter(
    private val activity: FragmentActivity,
) : RecyclerView.Adapter<ProductMasterAdapter.ListViewHolder>() {

    private var viewModel = ProductViewModel.getMainViewModel(activity)

    private var products: ArrayList<ProductWithCategory> = ArrayList()
    fun setProducts(products: List<ProductWithCategory>) {
        val productDiffUtil = RecycleViewDiffUtils(this.products, products)
        val diffUtilResult = DiffUtil.calculateDiff(productDiffUtil)
        this.products = ArrayList(products)
        diffUtilResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvProductMasterBinding.inflate(inflater, parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.setDataToView(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ListViewHolder(var binding: RvProductMasterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setDataToView(product: ProductWithCategory) {
            setTextView(product.product)
            setClickListener(product)
            setVariants(product.product.id)
        }

        private fun setTextView(product: Product) {
            binding.tvRvPmName.text = product.name
            binding.tvRvPmBuyPrice.text = toRupiah(product.buyPrice)
            binding.tvRvPmSellPrice.text = toRupiah(product.sellPrice)
            binding.tvRvPmDesc.text = product.desc
            binding.swProduct.isChecked = product.isActive
        }

        private fun setClickListener(product: ProductWithCategory) {
            binding.root.setOnClickListener {
                ProductMasterFragment(product).show(
                    activity.supportFragmentManager,
                    "detail-product"
                )
            }
            binding.swProduct.setOnCheckedChangeListener { v, _ ->
                run {
                    viewModel.updateProduct(
                        product.product.copy(
                            isActive = v.isChecked
                        )
                    )
                }
            }
        }

        private fun setVariants(idProduct: Long) {
            activity.lifecycleScope.launch {
                viewModel.getProductVariantOptions(idProduct)
                    .collect {
                        if (it != null) {
                            var count = 0
                            for (item in it) {
                                count += item.options.size
                            }
                            binding.tvRvPmVariant.text = "Terdapat $count Varian"
                        }
                    }
            }
        }
    }
}
