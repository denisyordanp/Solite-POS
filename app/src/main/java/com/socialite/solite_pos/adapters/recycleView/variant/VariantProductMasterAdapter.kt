package com.socialite.solite_pos.adapters.recycleView.variant

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.socialite.solite_pos.databinding.RvVariantProductMasterBinding
import com.socialite.solite_pos.view.main.menu.master.detail.VariantMasterMixOptionActivity
import com.socialite.solite_pos.view.main.menu.master.detail.VariantOptionActivity
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class VariantProductMasterAdapter(
    private val product: Product,
    private val activity: FragmentActivity
) : RecyclerView.Adapter<VariantProductMasterAdapter.ListViewHolder>() {

    private var viewModel = ProductViewModel.getMainViewModel(activity)

    var items: ArrayList<Variant> = ArrayList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            if (field.isNotEmpty()) {
                field.clear()
            }
            field.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            RvVariantProductMasterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val v = items[position]

        holder.binding.tvRvVrName.text = v.name
        holder.setData(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ListViewHolder(var binding: RvVariantProductMasterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(v: Variant) {
            if (v.isMix) {
                binding.cbVmMix.visibility = View.VISIBLE
                binding.btnVmOptions.visibility = View.INVISIBLE
                binding.tvRvVrOption.visibility = View.INVISIBLE

                activity.lifecycleScope.launch {
                    viewModel.getVariantProductById(product.id)
                        .collect {
                            binding.cbVmMix.isChecked = it != null
                            binding.cbVmMix.setOnCheckedChangeListener { _, _ ->
                                if (it == null) {
                                    viewModel.insertVariantProduct(
                                        VariantProduct(
                                            v.id,
                                            1,
                                            product.id
                                        )
                                    )
                                } else {
                                    viewModel.removeVariantProduct(it)
                                }
                            }
                        }
                }
            } else {
                activity.lifecycleScope.launch {
                    val query = VariantOption.getFilter(v.id, VariantOption.ACTIVE)
                    viewModel.getVariantOptions(query)
                        .collect {
                            binding.tvRvVrOption.text = "Terdapat ${it.size} pilihan"
                        }
                }
            }

            setIntent(v.isMix, v)
            checkType(v.type)
        }

        private fun setIntent(isMix: Boolean, variant: Variant) {
            val intent = if (isMix) {
                Intent(activity, VariantMasterMixOptionActivity::class.java)
                    .putExtra(VariantMasterMixOptionActivity.VARIANT, variant)
            } else {
                Intent(activity, VariantOptionActivity::class.java)
                    .putExtra(VariantOptionActivity.VARIANT, variant)
                    .putExtra(VariantOptionActivity.PRODUCT, product)
            }
            binding.btnVmOptions.setOnClickListener { activity.startActivity(intent) }
        }

        private fun checkType(type: Int) {
            when (type) {
                Variant.ONE_OPTION -> binding.tvRvVrType.text = "Hanya pilih satu"
                Variant.MULTIPLE_OPTION -> binding.tvRvVrType.text = "Dapat pilih banyak"
            }
        }

    }
}
