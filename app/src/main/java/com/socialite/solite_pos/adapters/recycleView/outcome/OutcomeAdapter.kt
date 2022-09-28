package com.socialite.solite_pos.adapters.recycleView.outcome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.room.master.Outcome
import com.socialite.solite_pos.databinding.RvOutcomeBinding
import com.socialite.solite_pos.utils.config.DateUtils.Companion.convertDateFromDb
import com.socialite.solite_pos.utils.config.DateUtils.Companion.currentDateTime
import com.socialite.solite_pos.utils.config.DateUtils.Companion.dateWithDayFormat
import com.socialite.solite_pos.utils.config.RupiahUtils.Companion.toRupiah
import com.socialite.solite_pos.utils.tools.RecycleViewDiffUtils
import com.socialite.solite_pos.view.main.menu.bottom.DetailOutcomeFragment

class OutcomeAdapter(private val activity: FragmentActivity) :
    RecyclerView.Adapter<OutcomeAdapter.ListViewHolder>() {

    private var outcomes: ArrayList<Outcome> = ArrayList()
    fun setOutcomes(outcomes: List<Outcome>) {
        val outcomeDiffUtil = RecycleViewDiffUtils(this.outcomes, outcomes)
        val diffUtilResult = DiffUtil.calculateDiff(outcomeDiffUtil)
        this.outcomes = ArrayList(outcomes)
        diffUtilResult.dispatchUpdatesTo(this)
    }

    fun addItem(outcome: Outcome) {
        outcomes.add(outcome)
        notifyItemInserted(0)
    }

    fun editItem(outcome: Outcome) {
        for ((i, item) in outcomes.withIndex()) {
            if (item.id == outcome.id) {
                outcomes[i] = outcome
                notifyItemChanged(i)
            }
        }
    }

    private val grandTotal: Long
        get() {
            var total = 0L
            for (item in outcomes) {
                total += item.total
            }
            return total
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvOutcomeBinding.inflate(inflater, parent, false)
        return ListViewHolder(binding, viewType == TITLE_ADAPTER_VIEW)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.setDataToView(if (position == 0) null else outcomes[position - 1])
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TITLE_ADAPTER_VIEW else ITEM_ADAPTER_VIEW
    }

    override fun getItemCount(): Int {
        return outcomes.size + 1
    }

    inner class ListViewHolder(var binding: RvOutcomeBinding, private val isTitle: Boolean) :
        RecyclerView.ViewHolder(binding.root) {
        fun setDataToView(outcome: Outcome?) {
            if (isTitle) {
                setTitleView()
            } else {
                outcome?.let {
                    setTextView(it)
                    setClickListener(it)
                }
            }
        }

        private fun setClickListener(outcome: Outcome) {
            binding.root.setOnClickListener {
                DetailOutcomeFragment(outcome).show(
                    activity.supportFragmentManager,
                    "detail-outcome"
                )
            }
        }

        private fun setTextView(outcome: Outcome) {
            val name = "${outcome.amount}x ${outcome.name}"

            binding.tvRvOcName.text = name
            binding.tvRvOcDesc.text = outcome.desc
            binding.tvRvOcTotal.text = toRupiah(outcome.total)
        }

        private fun setTitleView() {
            binding.tvRvOcName.text = "Total pengeluaran : "
            binding.tvRvOcDesc.text = convertDateFromDb(currentDateTime, dateWithDayFormat)
            binding.tvRvOcTotal.text = toRupiah(grandTotal)
        }
    }

    companion object {
        const val TITLE_ADAPTER_VIEW = 0
        const val ITEM_ADAPTER_VIEW = 1
    }
}
