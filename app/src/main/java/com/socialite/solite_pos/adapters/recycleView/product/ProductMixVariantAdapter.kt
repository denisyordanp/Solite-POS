package com.socialite.solite_pos.adapters.recycleView.product

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.R
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.databinding.RvProductBinding
import com.socialite.solite_pos.utils.tools.RecycleViewDiffUtils
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import kotlinx.coroutines.launch

class ProductMixVariantAdapter(
    private val variant: Variant?,
    private val activity: FragmentActivity
) : RecyclerView.Adapter<ProductMixVariantAdapter.ListViewHolder>() {

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
        val binding = RvProductBinding.inflate(inflater, parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        holder.setDataToView(products[position].product)

    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ListViewHolder(val binding: RvProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var isChecked: Boolean = false

        fun setDataToView(product: Product) {
            setTextView(product)
            setContentView()
            setVariant(product)
        }

        private fun setTextView(product: Product) {
            binding.tvPmvName.text = product.name
            binding.tvPmvStock.visibility = View.GONE
        }

        private fun setContentView() {
            binding.contPmvCount.visibility = View.GONE
            binding.cbPmvSelected.visibility = View.VISIBLE
            binding.cbPmvSelected.isEnabled = false
        }

        private fun setVariant(product: Product) {
            variant?.let {
                activity.lifecycleScope.launch {
                    viewModel.getVariantMixProductById(variant.id, product.id)
                        .collect {
                            if (it != null) {
                                isSelected(true)
                                binding.root.setOnClickListener { _ ->
                                    delMix(it)
                                }
                            } else {
                                isSelected(false)
                                binding.root.setOnClickListener {
                                    addMix(product)
                                }
                            }
                        }
                }
            }
        }

        private fun addMix(product: Product) {
            viewModel.insertVariantMix(VariantMix(variant!!.id, product.id))
        }

        private fun delMix(mix: VariantMix) {
            viewModel.removeVariantMix(mix)
        }

        private fun isSelected(state: Boolean) {
            if (state) {
                isChecked = true
                binding.cbPmvSelected.isChecked = true
                binding.contPmv.setCardBackgroundColor(
                    ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            binding.root.resources,
                            R.color.yellow,
                            null
                        )
                    )
                )
            } else {
                isChecked = false
                binding.cbPmvSelected.isChecked = false
                binding.contPmv.setCardBackgroundColor(
                    ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            binding.root.resources,
                            R.color.white,
                            null
                        )
                    )
                )
            }
        }
    }
}
