package com.socialite.solite_pos.adapters.recycleView.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import com.socialite.solite_pos.databinding.RvPaymentsListBinding
import com.socialite.solite_pos.utils.tools.RecycleViewDiffUtils

class PaymentsOrderAdapter(private val callback: (Payment) -> Unit) :
    RecyclerView.Adapter<PaymentsOrderAdapter.ListViewHolder>() {

    private var payments: ArrayList<Payment> = ArrayList()
    fun setPayments(payments: List<Payment>) {
        val paymentDiffUtil = RecycleViewDiffUtils(this.payments, payments)
        val diffUtilResult = DiffUtil.calculateDiff(paymentDiffUtil)
        this.payments = ArrayList(payments)
        diffUtilResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvPaymentsListBinding.inflate(inflater, parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.setDataToView(payments[position])
    }

    override fun getItemCount(): Int {
        return payments.size
    }

    inner class ListViewHolder(var binding: RvPaymentsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setDataToView(payment: Payment) {
            setTextView(payment)
            setClickListener(payment)
        }

        private fun setTextView(payment: Payment) {
            val tax = "Pajak +${payment.tax}%"
            binding.tvRvPmsName.text = payment.name
            binding.tvRvPmsDesc.text = payment.desc
            binding.tvRvPmsTax.text = tax
        }

        private fun setClickListener(payment: Payment) {
            binding.root.setOnClickListener {
                callback.invoke(payment)
            }
        }
    }
}
