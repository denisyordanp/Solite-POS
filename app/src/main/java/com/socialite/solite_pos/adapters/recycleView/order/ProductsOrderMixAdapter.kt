package com.socialite.solite_pos.adapters.recycleView.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.helper.ProductMixOrderDetail
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.databinding.RvItemOrderMixListBinding
import com.socialite.solite_pos.databinding.RvTotalItemOrderMixListBinding
import com.socialite.solite_pos.utils.config.ProductUtils
import com.socialite.solite_pos.utils.tools.RecycleViewDiffUtils

class ProductsOrderMixAdapter :
    RecyclerView.Adapter<ProductsOrderMixAdapter.BaseViewHolder<ProductOrderDetail>>() {

    var btnCallback: ((Boolean) -> Unit)? = null

    private var productsOrder: ArrayList<ProductOrderDetail> = ArrayList()

    fun setProductsOrder(productsOrder: List<ProductMixOrderDetail>) {
        val newProductsDetail = generateProductsOrder(productsOrder)
        val productsOrderDiffUtil = RecycleViewDiffUtils(this.productsOrder, newProductsDetail)
        val diffUtilResult = DiffUtil.calculateDiff(productsOrderDiffUtil)
        this.productsOrder = newProductsDetail
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun generateProductsOrder(productsOrder: List<ProductMixOrderDetail>): ArrayList<ProductOrderDetail> {
        val newProductsDetail: ArrayList<ProductOrderDetail> = ArrayList()
        newProductsDetail.add(ProductOrderDetail.grand)
        for (item in productsOrder) {
            newProductsDetail.add(
                ProductOrderDetail.createProduct(
                    item.product,
                    item.variants,
                    item.amount
                )
            )
        }
        return newProductsDetail
    }

    val sortedItems: ArrayList<ProductMixOrderDetail>
        get() {
            val items: ArrayList<ProductMixOrderDetail> = ArrayList()
            for (item in this.productsOrder) {
                if (item.product != null) {
                    items.add(ProductMixOrderDetail(item.product!!, item.variants, item.amount))
                }
            }
            return items
        }

    val totalItem: Int
        get() {
            var total = 0
            for (item in productsOrder) {
                if (item.type != ProductOrderDetail.GRAND_TOTAL) {
                    total += item.amount
                }
            }
            return total
        }

    fun addItem(detail: ProductOrderDetail) {
        if (productsOrder.isEmpty()) {
            if (detail.amount != 0) {
                setData()
                add(detail)
            }
        } else {
            add(detail)
        }
    }

    private fun add(detail: ProductOrderDetail) {
        val pos = ProductUtils.find(productsOrder, detail)
        if (pos != null) {
            if (detail.amount == 0) {
                delItem(pos)
                btnCallback?.invoke(false)
            } else {
                productsOrder[pos] = detail
                notifyItemChanged(pos)
                btnCallback?.invoke(true)
            }
        } else {
            if (detail.amount != 0) {
                productsOrder.add(0, detail)
                notifyItemInserted(0)
                btnCallback?.invoke(true)
            } else {
                btnCallback?.invoke(false)
            }
        }
        notifyItemChanged(productsOrder.size - 1)
    }

    private fun delItem(pos: Int) {
        if (productsOrder.size == 2) {
            productsOrder.clear()
            notifyDataSetChanged()
        } else {
            productsOrder.removeAt(pos)
            notifyItemRemoved(pos)
            notifyItemChanged(productsOrder.size - 1)
        }
    }

    companion object {
        private const val VALUE_COLUMN = 1
        private const val TOTAL_COLUMN = 2
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ProductOrderDetail> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VALUE_COLUMN -> ValueOrderColumnViewHolder(
                RvItemOrderMixListBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
            TOTAL_COLUMN -> TotalOrderColumnViewHolder(
                RvTotalItemOrderMixListBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ProductOrderDetail>, position: Int) {
        holder.bind(productsOrder[position])
    }

    override fun getItemViewType(position: Int): Int {
        val item = productsOrder[position]
        return when (item.type) {
            null -> VALUE_COLUMN
            else -> TOTAL_COLUMN
        }
    }

    override fun getItemCount(): Int {
        return productsOrder.size
    }

    private fun setData() {
        productsOrder.add(ProductOrderDetail.grand)
    }

    inner class ValueOrderColumnViewHolder(private val binding: RvItemOrderMixListBinding) :
        BaseViewHolder<ProductOrderDetail>(binding.root) {
        override fun bind(detail: ProductOrderDetail) {

            val no = "$adapterPosition."
            val amount = "${detail.amount}x"
            val variants = StringBuilder()
            for (variant in detail.variants) {
                if (variants.isNotEmpty()) {
                    variants.append(", ")
                }
                variants.append(variant.name)
            }

            binding.tvIoNo.text = no
            binding.tvIoAmount.text = amount
            binding.tvIoName.text = detail.product?.name
            binding.tvIoVariant.text = variants

            binding.btnOlDelete.setOnClickListener {
                if (productsOrder.size == 2) {
                    productsOrder.clear()
                    notifyDataSetChanged()
                } else {
                    productsOrder.remove(detail)
                    notifyItemRemoved(adapterPosition)
                    notifyItemChanged(productsOrder.size - 1)
                }
                btnCallback?.invoke(true)
            }
        }
    }

    inner class TotalOrderColumnViewHolder(private val binding: RvTotalItemOrderMixListBinding) :
        BaseViewHolder<ProductOrderDetail>(binding.root) {
        override fun bind(detail: ProductOrderDetail) {
            binding.tvIoName.text = "Total : "
            binding.tvIoTotal.text = totalItem.toString()
        }
    }

    abstract class BaseViewHolder<ProductOrderDetail>(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        abstract fun bind(detail: ProductOrderDetail)
    }
}
