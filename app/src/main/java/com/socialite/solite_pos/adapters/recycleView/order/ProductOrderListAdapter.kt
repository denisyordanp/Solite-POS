package com.socialite.solite_pos.adapters.recycleView.order

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.socialite.solite_pos.databinding.RvFirstColumnItemOrderListBinding
import com.socialite.solite_pos.databinding.RvItemOrderListBinding
import com.socialite.solite_pos.databinding.RvTotalItemOrderListBinding
import com.socialite.solite_pos.utils.config.ProductUtils
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah
import com.socialite.solite_pos.utils.tools.RecycleViewDiffUtils
import com.socialite.solite_pos.view.main.menu.master.dialog.DetailOrderProductFragment
import com.socialite.solite_pos.view.main.menu.order.SelectMixVariantOrderActivity

class ProductOrderListAdapter(
	private val activity: FragmentActivity
) : RecyclerView.Adapter<ProductOrderListAdapter.BaseViewHolder<ProductOrderDetail>>() {

	var buttonEnableCallback: ((Boolean) -> Unit)? = null

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
				field = value
				setProductsOrder(value.products)
				buttonEnableCallback?.invoke(false)
			}
		}

	val newOrder: OrderWithProduct?
		get() {
			return if (order != null) {
				val order = OrderWithProduct(
					order!!.order,
					sortedProductsOrder
				)
				order
			} else {
				null
			}
		}

	private val sortedProductsOrder: ArrayList<ProductOrderDetail>
		get() {
			val items: ArrayList<ProductOrderDetail> = this.productsOrder
			when (order?.order?.order?.status) {
				Order.ON_PROCESS, Order.NEED_PAY -> {
					items.remove(ProductOrderDetail.grand)
				}
				Order.DONE -> {
					items.remove(ProductOrderDetail.grand)
					items.remove(ProductOrderDetail.payment)
					items.remove(ProductOrderDetail.payReturn)
				}
			}
			return items
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

	fun addItem(detail: ProductOrderDetail) {
		if (productsOrder.isEmpty()) {
			if (detail.amount != 0) {
				configureList()
				add(detail)
			}
		} else {
			add(detail)
		}
	}

	private fun configureList() {
		when (order?.order?.order?.status) {
			Order.ON_PROCESS, Order.NEED_PAY -> {
				productsOrder.add(ProductOrderDetail.grand)
			}
			Order.DONE -> {
				productsOrder.add(ProductOrderDetail.grand)
				productsOrder.add(ProductOrderDetail.payment)
				if (order?.order?.payment!!.isCash) {
					productsOrder.add(ProductOrderDetail.payReturn)
				}
			}
		}
	}

	private fun add(detail: ProductOrderDetail) {
		val pos = ProductUtils.find(productsOrder, detail)
		if (pos != null) {
			if (detail.amount != 0) {
				val oldAmount = productsOrder[pos].amount
				productsOrder[pos].amount = oldAmount + detail.amount
				notifyItemChanged(pos)
				buttonEnableCallback?.invoke(true)
			}
		} else {
			if (detail.amount != 0) {
				productsOrder.add(0, detail)
				notifyItemInserted(0)
				buttonEnableCallback?.invoke(true)
			} else {
				buttonEnableCallback?.invoke(false)
			}
		}
		notifyItemChanged(productsOrder.size - 1)
	}

	private fun delItem(pos: Int) {
		if (productsOrder.size == 2) {
			productsOrder.clear()
			notifyDataSetChanged()
			buttonEnableCallback?.invoke(false)
		} else {
			productsOrder.removeAt(pos)
			notifyItemRemoved(pos)
			notifyItemChanged(productsOrder.size - 1)
			buttonEnableCallback?.invoke(true)
		}
	}

	companion object{
		private const val FIRST_COLUMN = 0
		private const val VALUE_COLUMN = 1
		private const val TOTAL_COLUMN = 2
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ProductOrderDetail> {
		val inflater = LayoutInflater.from(parent.context)
		return when (viewType){
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
				ValueOrderColumnViewHolder(
					RvItemOrderListBinding.inflate(
						inflater,
						parent,
						false
					)
				)
			}
			TOTAL_COLUMN -> {
				TotalOrderColumnViewHolder(
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

	inner class ValueOrderColumnViewHolder(private val binding: RvItemOrderListBinding) :
		BaseViewHolder<ProductOrderDetail>(binding.root) {
		override fun bind(detail: ProductOrderDetail) {
			setDataToView(detail)
			setMainVariants(detail.variants)
			setMix(detail)
			setBtnDelete()
			setViewClickListener(detail)
		}

		private fun setDataToView(detail: ProductOrderDetail) {
			val no = "$adapterPosition."
			val amount = "${detail.amount}x"

			binding.tvIoNo.text = no
			binding.tvIoAmount.text = amount
			binding.tvIoName.text = detail.product?.name
			binding.tvIoPrice.text = toRupiah(detail.product?.sellPrice)
			binding.tvIoTotal.text = toRupiah(getTotal(detail))
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

		private fun getTotal(detail: ProductOrderDetail): Long {
			var total = 0L
			if (detail.product != null) {
				val product: Product = detail.product!!
				total = product.sellPrice * detail.amount
			}
			return total
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

		private fun setBtnDelete() {
			if (order?.order?.order?.status == Order.NEED_PAY) {
				binding.btnOlDelete.visibility = View.INVISIBLE
			} else {
				binding.btnOlDelete.setOnClickListener {
					delItem(adapterPosition)
				}
			}
		}

		private fun setViewClickListener(detail: ProductOrderDetail) {
			binding.root.setOnClickListener {
				if (detail.product != null) {
					if (detail.product!!.isMix) {
						val intent = Intent(activity, SelectMixVariantOrderActivity::class.java)
						intent.putExtra(SelectMixVariantOrderActivity.PRODUCT_ORDER_DETAIL, detail)
						activity.startActivityForResult(
							intent,
							SelectMixVariantOrderActivity.RC_MIX
                        )
                    } else {
                        DetailOrderProductFragment(
                                DetailOrderProductFragment.ORDER,
                                detail.product!!
                        ) { changeItem(it) }.show(activity.supportFragmentManager, "detail-order-product")
                    }
                }
            }
        }

        private fun changeItem(product: ProductOrderDetail) {
			val item = productsOrder[adapterPosition]
            if (product.amount != 0) item.amount = product.amount
            item.variants
			productsOrder[adapterPosition] = item
			notifyItemChanged(adapterPosition)
			notifyItemChanged(productsOrder.size - 1)
        }
    }

	inner class TotalOrderColumnViewHolder(private val binding: RvTotalItemOrderListBinding) : BaseViewHolder<ProductOrderDetail>(binding.root) {
		override fun bind(detail: ProductOrderDetail) {
			binding.tvIoName.text = "Total : "
			binding.tvIoTotal.text = toRupiah(grandTotal)
		}
	}

	abstract class BaseViewHolder<ProductOrderDetail>(itemView: View) : RecyclerView.ViewHolder(itemView){
		abstract fun bind(detail: ProductOrderDetail)
	}
}
