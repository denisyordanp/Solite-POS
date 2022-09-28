package com.socialite.solite_pos.adapters.recycleView.recap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.helper.RecapData
import com.socialite.solite_pos.databinding.RvRecapBinding
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah
import com.socialite.solite_pos.utils.tools.RecycleViewDiffUtils

class RecapAdapter : RecyclerView.Adapter<RecapAdapter.ListViewHolder>() {

    private var recaps: ArrayList<RecapData> = ArrayList()
    fun setRecaps(recaps: List<RecapData>) {
        val recapDiffUtils = RecycleViewDiffUtils(this.recaps, recaps)
        val diffUtilResult = DiffUtil.calculateDiff(recapDiffUtils)
        this.recaps = ArrayList(recaps)
        diffUtilResult.dispatchUpdatesTo(this)
    }

    val grandTotal: Long
        get() {
            var total = 0L
            for (item in recaps) {
                total += item.total
            }
            return total
        }

    fun getIncome(isCash: Boolean): Long {
        var total = 0L
        for (item in recaps) {
            if (item.isCash != null) {
                if (item.isCash!! == isCash) total += item.total
            }
        }
        return total
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvRecapBinding.inflate(inflater, parent, false)
        return ListViewHolder(binding, viewType == TITLE_ADAPTER_VIEW)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.setDataToView(if (position == recaps.size) null else recaps[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == recaps.size) TITLE_ADAPTER_VIEW else ITEM_ADAPTER_VIEW
    }

    override fun getItemCount(): Int {
        return recaps.size + 1
    }

    inner class ListViewHolder(var binding: RvRecapBinding, private val isTitle: Boolean) :
        RecyclerView.ViewHolder(binding.root) {
        fun setDataToView(recap: RecapData?) {
            if (isTitle) {
                setTitleView()
            } else {
                recap?.let {
                    setTitleLine()
                    setTextView(it)
                }
            }
        }

        private fun setTitleView() {
            binding.tvRvRcName.text = "Grand Total : "
            binding.tvRvRcDesc.text = ""
            binding.tvRvRcTotal.text = toRupiah(grandTotal)
        }

        private fun setTitleLine() {
            if (adapterPosition == 0) {
                binding.vRvRcLine.visibility = View.INVISIBLE
            }
        }

        private fun setTextView(recap: RecapData) {
            binding.tvRvRcName.text = recap.name
            binding.tvRvRcDesc.text = recap.desc
            binding.tvRvRcTotal.text = toRupiah(recap.total)
        }
    }

    companion object {
        const val TITLE_ADAPTER_VIEW = 0
        const val ITEM_ADAPTER_VIEW = 1
    }
}
