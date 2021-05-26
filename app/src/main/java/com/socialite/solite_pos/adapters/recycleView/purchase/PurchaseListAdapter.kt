package com.socialite.solite_pos.adapters.recycleView.purchase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseProductWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.socialite.solite_pos.databinding.RvFirstColumnItemOrderListBinding
import com.socialite.solite_pos.databinding.RvItemOrderListBinding
import com.socialite.solite_pos.databinding.RvTotalItemOrderListBinding
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah
import com.socialite.solite_pos.utils.tools.RecycleViewDiffUtils

class PurchaseListAdapter :
    RecyclerView.Adapter<PurchaseListAdapter.BaseViewHolder<PurchaseProductWithProduct>>() {

    companion object {
        private const val FIRST_COLUMN = 0
        private const val VALUE_COLUMN = 1
        private const val TOTAL_COLUMN = 2
    }

    var btnCallback: ((Boolean) -> Unit)? = null

    private var purchasesProduct: ArrayList<PurchaseProductWithProduct> = ArrayList()
    private fun setPurchasesProduct(purchasesProduct: List<PurchaseProductWithProduct>) {
        val purchasesProductDiffUtil = RecycleViewDiffUtils(this.purchasesProduct, purchasesProduct)
        val diffUtilResult = DiffUtil.calculateDiff(purchasesProductDiffUtil)
        this.purchasesProduct = ArrayList(purchasesProduct)
        diffUtilResult.dispatchUpdatesTo(this)
    }

    var purchase: PurchaseWithProduct? = null
        set(value) {
            if (value != null) {
                field = value
                setPurchasesProduct(value.products)
                btnCallback?.invoke(false)
                notifyDataSetChanged()
            }
        }

    val newPurchase: PurchaseWithProduct?
        get() {
            return if (purchase != null) {
                PurchaseWithProduct(
                    purchase!!.purchase,
                    purchase!!.supplier,
                    sortedItems
                )
            } else {
                null
            }
        }

    private val sortedItems: ArrayList<PurchaseProductWithProduct>
        get() {
            val items: ArrayList<PurchaseProductWithProduct> = this.purchasesProduct
            items.remove(PurchaseProductWithProduct.grand)
            return items
        }

    private val grandTotal: Long
        get() {
            var total = 0L
            for (item in purchasesProduct) {
                if (item.product != null && item.purchaseProduct != null) {
                    total += item.product!!.buyPrice * item.purchaseProduct!!.amount
                }
            }
            return total
        }

    fun addItem(detail: PurchaseProductWithProduct) {
        if (purchasesProduct.isEmpty()) {
            if (detail.purchaseProduct?.amount != 0) {
                purchasesProduct.add(0, PurchaseProductWithProduct.grand)
                add(detail)
            }
        } else {
            add(detail)
        }
    }

    private fun add(detail: PurchaseProductWithProduct) {
        val pos = findProductPurchaseIndex(purchasesProduct, detail)
        if (pos != null) {
            if (detail.purchaseProduct?.amount == 0) {
                delItem(pos)
                btnCallback?.invoke(false)
            } else {
                purchasesProduct[pos] = detail
                notifyItemChanged(pos)
                btnCallback?.invoke(true)
            }
        } else {
            if (detail.purchaseProduct?.amount != 0) {
                purchasesProduct.add(0, detail)
                notifyItemInserted(0)
                btnCallback?.invoke(true)
            } else {
                btnCallback?.invoke(false)
            }
        }
        notifyItemChanged(purchasesProduct.size - 1)
    }

    private fun findProductPurchaseIndex(
        array: ArrayList<PurchaseProductWithProduct>,
        detail: PurchaseProductWithProduct?
    ): Int? {
        for ((i, v) in array.withIndex()) {
            if (v.product != null) {
                if (v.product == detail?.product) {
                    return i
                }
            }
        }
        return null
    }

    private fun delItem(pos: Int) {
        if (purchasesProduct.size == 2) {
            purchasesProduct.clear()
            notifyDataSetChanged()
        } else {
            purchasesProduct.removeAt(pos)
            notifyItemRemoved(pos)
            notifyItemChanged(purchasesProduct.size - 1)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<PurchaseProductWithProduct> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            FIRST_COLUMN -> {
                FirstColumnViewHolder(
                    RvFirstColumnItemOrderListBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )
            }
            VALUE_COLUMN -> {
                ValueOrderColumnViewHolder(RvItemOrderListBinding.inflate(inflater, parent, false))
            }
            TOTAL_COLUMN -> {
                TotalPurchaseColumnViewHolder(
                    RvTotalItemOrderListBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )
            }
            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<PurchaseProductWithProduct>,
        position: Int
    ) {
        holder.bind(purchasesProduct[position])
    }

    override fun getItemViewType(position: Int): Int {
        val item = purchasesProduct[position]
        return when (item.type) {
            null -> VALUE_COLUMN
            ProductOrderDetail.TITLE -> FIRST_COLUMN
            else -> TOTAL_COLUMN
        }
    }

    override fun getItemCount(): Int {
        return purchasesProduct.size
    }

    inner class FirstColumnViewHolder(binding: RvFirstColumnItemOrderListBinding) :
        BaseViewHolder<PurchaseProductWithProduct>(binding.root) {
        override fun bind(detail: PurchaseProductWithProduct) {}
    }

    inner class ValueOrderColumnViewHolder(private val binding: RvItemOrderListBinding) :
        BaseViewHolder<PurchaseProductWithProduct>(binding.root) {

        override fun bind(detail: PurchaseProductWithProduct) {
            if (detail.product != null && detail.purchaseProduct != null) {
                setTextView(detail)
                setCLickListener(detail)
            }
        }

        private fun setTextView(purchaseProduct: PurchaseProductWithProduct) {
            val no = "$adapterPosition."
            val amount = "${purchaseProduct.purchaseProduct!!.amount}x"
            val total =
                purchaseProduct.product!!.buyPrice * purchaseProduct.purchaseProduct!!.amount

            binding.tvIoNo.text = no
            binding.tvIoAmount.text = amount
            binding.tvIoName.text = purchaseProduct.product?.name
            binding.tvIoPrice.text = toRupiah(purchaseProduct.product?.buyPrice)
            binding.tvIoTotal.text = toRupiah(total)
        }

        private fun setCLickListener(purchaseProduct: PurchaseProductWithProduct) {
            binding.btnOlDelete.setOnClickListener {
                if (purchasesProduct.size == 2) {
                    purchasesProduct.clear()
                    notifyDataSetChanged()
                } else {
                    purchasesProduct.remove(purchaseProduct)
                    notifyItemRemoved(adapterPosition)
                    notifyItemChanged(purchasesProduct.size - 1)
                }
                btnCallback?.invoke(true)
            }
        }
    }

    inner class TotalPurchaseColumnViewHolder(private val binding: RvTotalItemOrderListBinding) :
        BaseViewHolder<PurchaseProductWithProduct>(binding.root) {
        override fun bind(detail: PurchaseProductWithProduct) {
            binding.tvIoPrice.text = "Total :"
            binding.tvIoTotal.text = toRupiah(grandTotal)
        }

    }

    abstract class BaseViewHolder<ProductOrderDetail>(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        abstract fun bind(detail: PurchaseProductWithProduct)
    }
}
