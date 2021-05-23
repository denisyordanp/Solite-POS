package com.socialite.solite_pos.adapters.recycleView.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.socialite.solite_pos.databinding.RvDetailItemOrderListBinding
import com.socialite.solite_pos.databinding.RvFirstColumnItemOrderListBinding
import com.socialite.solite_pos.databinding.RvTotalDetailItemOrderListBinding
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah
import com.socialite.solite_pos.utils.tools.RecycleViewDiffUtils

class ProductDetailOrderAdapter :
	RecyclerView.Adapter<ProductDetailOrderAdapter.BaseViewHolder<ProductOrderDetail>>() {

	companion object {
		private const val FIRST_COLUMN = 0
		private const val VALUE_COLUMN = 1
		private const val TOTAL_COLUMN = 2
	}

	private var productsOrder: ArrayList<ProductOrderDetail> = ArrayList()

	private fun setProductsOrder(productsOrder: List<ProductOrderDetail>) {
		val productsOrderDiffUtil = RecycleViewDiffUtils(this.productsOrder, productsOrder)
		val diffUtilResult = DiffUtil.calculateDiff(productsOrderDiffUtil)
		this.productsOrder = ArrayList(productsOrder)
		diffUtilResult.dispatchUpdatesTo(this)
	}

	var order: OrderWithProduct? = null
		set(value) {
			if (value != null) {
				value.products = configureList(value.products)
				field = value
				setProductsOrder(value.products)
			}
		}

	private fun configureList(productsOrder: List<ProductOrderDetail>): ArrayList<ProductOrderDetail> {
		val configuresList = ArrayList(productsOrder)
		configuresList.add(0, ProductOrderDetail.title)
		when (order?.order?.order?.status) {
			Order.ON_PROCESS, Order.NEED_PAY -> {
				configuresList.add(ProductOrderDetail.grand)
			}
			Order.DONE -> {
				configuresList.add(ProductOrderDetail.grand)
				configuresList.add(ProductOrderDetail.payment)
				if (order?.order?.payment!!.isCash) {
					configuresList.add(ProductOrderDetail.payReturn)
				}
			}
		}
		return configuresList
	}

	private val grandTotal: Long
		get() {
			var total = 0L
			for (item in productsOrder) {
				if (item.product != null) {
					total += item.product!!.sellPrice * item.amount
				}
			}
			return total
		}

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): BaseViewHolder<ProductOrderDetail> {
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
				ValueDetailColumnViewHolder(
					RvDetailItemOrderListBinding.inflate(
						inflater,
						parent,
						false
					)
				)
			}
			TOTAL_COLUMN -> {
				TotalDetailColumnViewHolder(
					RvTotalDetailItemOrderListBinding.inflate(
						inflater,
						parent,
						false
					)
				)
			}
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
			ProductOrderDetail.TITLE -> FIRST_COLUMN
			else -> TOTAL_COLUMN
		}
	}

	override fun getItemCount(): Int {
		return productsOrder.size
	}

	inner class FirstColumnViewHolder(binding: RvFirstColumnItemOrderListBinding) :
		BaseViewHolder<ProductOrderDetail>(binding.root) {
		override fun bind(detail: ProductOrderDetail) {}
	}

	inner class ValueDetailColumnViewHolder(private val binding: RvDetailItemOrderListBinding) :
		BaseViewHolder<ProductOrderDetail>(binding.root) {
		override fun bind(detail: ProductOrderDetail) {
			setDataToView(detail)
			setMainVariants(detail.variants)
			setMix(detail)
		}

		private fun setDataToView(detail: ProductOrderDetail) {
			val no = "$adapterPosition."
			val amount = "${detail.amount}x"
			val total = detail.product!!.sellPrice * detail.amount

			binding.tvIoNo.text = no
			binding.tvIoAmount.text = amount
			binding.tvIoName.text = detail.product?.name
			binding.tvIoPrice.text = toRupiah(detail.product?.sellPrice)
			binding.tvIoTotal.text = toRupiah(total)
		}

		private fun setMainVariants(variants: ArrayList<VariantOption>) {
			if (variants.isNotEmpty()) {
				val variantText = StringBuilder()
				for (variant in variants) {
					if (variantText.isNotEmpty()) {
						variantText.append(", ")
					}
					variantText.append(variant.name)
				}
				binding.tvIoVariant.visibility = View.VISIBLE
				binding.tvIoVariant.text = variantText
			}
		}

		private fun setMix(detail: ProductOrderDetail) {
			if (detail.product != null) {
				if (detail.product!!.isMix) {
					binding.tvIoVariant.visibility = View.GONE
					if (detail.mixProducts.isNotEmpty()) {
						binding.contIoMixVariant.removeAllViews()
						for (item in detail.mixProducts) {
							val txt = "${item.product.name} x${item.amount}"
							val tvProduct = TextView(binding.root.context)
							tvProduct.text = txt
							binding.contIoMixVariant.addView(tvProduct)

							setMixVariants(item.variants)
						}
					}
				}
			}
		}

		private fun setMixVariants(variants: ArrayList<VariantOption>) {
			if (variants.isNotEmpty()) {
				val variantText = StringBuilder()
				for (variant in variants) {
					if (variantText.isNotEmpty()) {
						variantText.append(", ")
					}
					variantText.append(variant.name)
				}
				val tvVariant = TextView(binding.root.context)
				tvVariant.text = variantText
				binding.contIoMixVariant.addView(tvVariant)
			}
		}
	}

	inner class TotalDetailColumnViewHolder(private val binding: RvTotalDetailItemOrderListBinding) :
		BaseViewHolder<ProductOrderDetail>(binding.root) {
		override fun bind(detail: ProductOrderDetail) {
			var title = ""
			var amount = ""
			when (detail.type) {
				ProductOrderDetail.GRAND_TOTAL -> {
					title = "Total : "
					amount = toRupiah(grandTotal)
				}
				ProductOrderDetail.PAYMENT -> {
					title = "Bayar : "
					amount = if (order?.order?.payment!!.isCash) {
						toRupiah(order?.order?.orderPayment?.pay)
					} else {
						order?.order?.payment!!.name
					}
				}
				ProductOrderDetail.RETURN -> {
					title = "Kembalian : "
					amount = toRupiah(order?.order?.orderPayment?.inReturn(order!!.grandTotal))
				}
			}
			binding.tvIoPrice.text = title
			binding.tvIoTotal.text = amount
		}

	}

	abstract class BaseViewHolder<ProductOrderDetail>(itemView: View) :
		RecyclerView.ViewHolder(itemView) {
		abstract fun bind(detail: ProductOrderDetail)
	}
}
