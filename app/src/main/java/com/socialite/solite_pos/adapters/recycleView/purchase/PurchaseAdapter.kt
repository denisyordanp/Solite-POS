package com.socialite.solite_pos.adapters.recycleView.purchase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseProductWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.helper.PurchaseWithSupplier
import com.socialite.solite_pos.databinding.RvPurchaseBinding
import com.socialite.solite_pos.utils.config.DateUtils.Companion.convertDateFromDb
import com.socialite.solite_pos.utils.config.DateUtils.Companion.dateWithTimeFormat
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah
import com.socialite.solite_pos.utils.tools.RecycleViewDiffUtils
import com.socialite.solite_pos.view.viewModel.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PurchaseAdapter(
    private val activity: FragmentActivity
) : RecyclerView.Adapter<PurchaseAdapter.ListViewHolder>() {

    private val viewModel = MainViewModel.getMainViewModel(activity)

    private var purchases: ArrayList<PurchaseWithSupplier> = ArrayList()
    fun setPurchases(purchases: List<PurchaseWithSupplier>) {
        val purchaseDiffUtil = RecycleViewDiffUtils(this.purchases, purchases)
        val diffUtilResult = DiffUtil.calculateDiff(purchaseDiffUtil)
        this.purchases = ArrayList(purchases)
        diffUtilResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvPurchaseBinding.inflate(inflater, parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.setDataToView(purchases[position])
    }

    override fun getItemCount(): Int {
        return purchases.size
    }

    inner class ListViewHolder(var binding: RvPurchaseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setDataToView(purchase: PurchaseWithSupplier) {
            setTextView(purchase)
            getPurchaseProducts(purchase.purchase.purchaseNo)
        }

        private fun setTextView(purchase: PurchaseWithSupplier) {
            binding.tvRvPcDate.text =
                convertDateFromDb(purchase.purchase.purchaseTime, dateWithTimeFormat)
            binding.tvRvPcSupplier.text = purchase.supplier.name
        }

        private fun getPurchaseProducts(purchaseNo: String) {
            activity.lifecycleScope.launch {
                viewModel.getPurchaseProducts(purchaseNo)
                    .collect {
                        if (it.isNotEmpty()) {
                            setPurchaseProducts(it)
                        }
                    }
            }
        }

        private fun setPurchaseProducts(products: List<PurchaseProductWithProduct>) {
            binding.tvRvPcTotal.text = toRupiah(getTotalPurchases(products))
            binding.root.setOnClickListener {

            }
        }

        private fun getTotalPurchases(products: List<PurchaseProductWithProduct>): Long {
            var total = 0L
            for (item in products) {
                if (item.purchaseProduct != null && item.product != null) {
                    total += item.purchaseProduct!!.amount * item.product!!.buyPrice
                }
            }
            return total
        }
    }
}
