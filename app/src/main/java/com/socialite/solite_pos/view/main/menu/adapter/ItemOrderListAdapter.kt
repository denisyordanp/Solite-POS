package com.socialite.solite_pos.view.main.menu.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.socialite.solite_pos.databinding.*
import com.socialite.solite_pos.utils.config.ProductUtils
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah
import com.socialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.socialite.solite_pos.view.main.menu.master.dialog.DetailOrderProductFragment
import com.socialite.solite_pos.view.main.menu.order.SelectMixVariantOrderActivity

class ItemOrderListAdapter(
        private val activity: SocialiteActivity,
        private val type: Int,
) : RecyclerView.Adapter<ItemOrderListAdapter.BaseViewHolder<ProductOrderDetail>>() {

    var btnCallback: ((Boolean) -> Unit)? = null

    var order: OrderWithProduct? = null
        set(value) {
            if (value != null) {
                field = value
                if (items.isNotEmpty()) {
                    items.clear()
                }
			items.addAll(value.products)
			if (type == DETAIL){
				setData()
			}
			btnCallback?.invoke(false)
			notifyDataSetChanged()
		}
	}

	val newOrder: OrderWithProduct?
	get() {
		return if (order != null){
			val order = OrderWithProduct(
					order!!.order,
					sortedItems
			)
			order
		}else{
			null
		}
	}

	private val items: ArrayList<ProductOrderDetail> = ArrayList()
	private val sortedItems: ArrayList<ProductOrderDetail>
	get() {
		val items: ArrayList<ProductOrderDetail> = this.items
		if (type == DETAIL){
			items.remove(ProductOrderDetail.title)
		}
		when(order?.order?.order?.status){
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
		for (item in items){
			if (item.product != null){
				total += item.product!!.sellPrice * item.amount
			}
		}
		return total
	}

	fun addItem(detail: ProductOrderDetail){
		if (items.isEmpty()){
			if (detail.amount != 0){
				setData()
				add(detail)
			}
		}else{
			add(detail)
		}
	}

	private fun add(detail: ProductOrderDetail){
		val pos = ProductUtils.find(items, detail)
		if (pos != null){
			if (detail.amount == 0){
				delItem(pos)
				btnCallback?.invoke(false)
			}else{
				items[pos] = detail
				notifyItemChanged(pos)
				btnCallback?.invoke(true)
			}
		}else{
			if (detail.amount != 0){
				items.add(0, detail)
				notifyItemInserted(0)
				btnCallback?.invoke(true)
			}else{
				btnCallback?.invoke(false)
			}
		}
		notifyItemChanged(items.size-1)
	}

	private fun delItem(pos: Int){
		if (items.size == 2){
			items.clear()
			notifyDataSetChanged()
		}else{
			items.removeAt(pos)
			notifyItemRemoved(pos)
			notifyItemChanged(items.size-1)
		}
	}

	companion object{
		private const val FIRST_COLUMN = 0
		private const val VALUE_COLUMN = 1
		private const val TOTAL_COLUMN = 2

		const val DETAIL = 1
		const val ORDER = 2
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ProductOrderDetail> {
		return when (viewType){
			FIRST_COLUMN -> {
				FirstColumnViewHolder(RvFirstColumnItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
			}
			VALUE_COLUMN -> {
				when(type){
					DETAIL -> ValueDetailColumnViewHolder(RvDetailItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
					ORDER -> ValueOrderColumnViewHolder(RvItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
					else -> throw IllegalArgumentException("Invalid adapterType")
				}
			}
			TOTAL_COLUMN -> {
				when(type){
					DETAIL -> TotalDetailColumnViewHolder(RvTotalDetailItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
					ORDER -> TotalOrderColumnViewHolder(RvTotalItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
					else -> throw IllegalArgumentException("Invalid adapterType")
				}
			}
			else -> throw IllegalArgumentException("Invalid viewType")
		}
	}

	override fun onBindViewHolder(holder: BaseViewHolder<ProductOrderDetail>, position: Int) {
		holder.bind(items[position])
	}

	override fun getItemViewType(position: Int): Int {
		val item = items[position]
		return when(item.type){
			null -> VALUE_COLUMN
			ProductOrderDetail.TITLE -> FIRST_COLUMN
			else -> TOTAL_COLUMN
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	private fun setData(){
		if (type == DETAIL){
			items.add(0, ProductOrderDetail.title)
		}
		when(order?.order?.order?.status){
			Order.ON_PROCESS, Order.NEED_PAY -> {
				items.add(ProductOrderDetail.grand)
			}
			Order.DONE -> {
				items.add(ProductOrderDetail.grand)
				items.add(ProductOrderDetail.payment)
				if (order?.order?.payment!!.isCash){
					items.add(ProductOrderDetail.payReturn)
				}
			}
		}
	}

	inner class FirstColumnViewHolder(binding: RvFirstColumnItemOrderListBinding) : BaseViewHolder<ProductOrderDetail>(binding.root) {
		override fun bind(detail: ProductOrderDetail) {}
	}

	inner class ValueDetailColumnViewHolder(private val binding: RvDetailItemOrderListBinding) : BaseViewHolder<ProductOrderDetail>(binding.root) {
		override fun bind(detail: ProductOrderDetail) {
			if (detail.product != null){
				val no = "$adapterPosition."
				val amount = "${detail.amount}x"
				val total = detail.product!!.sellPrice * detail.amount

				binding.tvIoNo.text = no
				binding.tvIoAmount.text = amount
				binding.tvIoName.text = detail.product!!.name
				binding.tvIoPrice.text = toRupiah(detail.product!!.sellPrice)
				binding.tvIoTotal.text = toRupiah(total)
			}
		}
	}

	inner class ValueOrderColumnViewHolder(private val binding: RvItemOrderListBinding) : BaseViewHolder<ProductOrderDetail>(binding.root) {
		override fun bind(detail: ProductOrderDetail) {

            val no = "$adapterPosition."
            val amount = "${detail.amount}x"

            binding.tvIoNo.text = no
            binding.tvIoAmount.text = amount
            binding.tvIoName.text = detail.product?.name
            binding.tvIoPrice.text = toRupiah(detail.product?.sellPrice)
            binding.tvIoTotal.text = toRupiah(getTotal(detail))

            setMainVariants(detail.variants)
            setMix(detail)
            setBtnDelete(detail)
            setViewClickListener(detail)
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

		private fun getTotal(detail: ProductOrderDetail): Long{
			var total = 0L
			if (detail.product != null){
				val product: Product = detail.product!!
				total = product.sellPrice * detail.amount
			}
			return total
		}

		private fun setMix(detail: ProductOrderDetail){
			if (detail.product != null){
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

        private fun setBtnDelete(detail: ProductOrderDetail) {
            if (order?.order?.order?.status == Order.NEED_PAY) {
                binding.btnOlDelete.visibility = View.INVISIBLE
            } else {
                binding.btnOlDelete.setOnClickListener {
                    if (items.size == 2) {
                        items.clear()
                        notifyDataSetChanged()
                    } else {
                        items.remove(detail)
                        notifyItemRemoved(adapterPosition)
                        notifyItemChanged(items.size - 1)
                    }
                    btnCallback?.invoke(true)
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
            val item = items[adapterPosition]
            if (product.amount != 0) item.amount = product.amount
            item.variants
            items[adapterPosition] = item
            notifyItemChanged(adapterPosition)
            notifyItemChanged(items.size - 1)
        }
    }

	inner class TotalDetailColumnViewHolder(private val binding: RvTotalDetailItemOrderListBinding) : BaseViewHolder<ProductOrderDetail>(binding.root) {
		override fun bind(detail: ProductOrderDetail) {
			var title = ""
			var amount = ""
			when(detail.type){
				ProductOrderDetail.GRAND_TOTAL -> {
					title = "Total : "
					amount = toRupiah(grandTotal)
				}
				ProductOrderDetail.PAYMENT -> {
					title = "Bayar : "
					amount = if (order?.order?.payment!!.isCash){
						toRupiah(order?.order?.orderPayment?.pay)
					}else{
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
