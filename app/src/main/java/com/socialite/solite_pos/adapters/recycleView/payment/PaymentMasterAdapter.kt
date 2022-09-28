package com.socialite.solite_pos.adapters.recycleView.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import com.socialite.solite_pos.databinding.RvCategoryMasterBinding
import com.socialite.solite_pos.utils.tools.RecycleViewDiffUtils
import com.socialite.solite_pos.view.main.menu.master.bottom.PaymentMasterFragment
import com.socialite.solite_pos.view.viewModel.MainViewModel

class PaymentMasterAdapter(
    private val activity: FragmentActivity
) : RecyclerView.Adapter<PaymentMasterAdapter.ListViewHolder>() {

    private val viewModel = MainViewModel.getMainViewModel(activity)

    private var payments: ArrayList<Payment> = ArrayList()
    fun setPayments(payments: List<Payment>) {
        val paymentDiffUtil = RecycleViewDiffUtils(this.payments, payments)
        val diffUtilResult = DiffUtil.calculateDiff(paymentDiffUtil)
        this.payments = ArrayList(payments)
        diffUtilResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvCategoryMasterBinding.inflate(inflater, parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.setDataToView(payments[position])
    }

    override fun getItemCount(): Int {
        return payments.size
    }

    inner class ListViewHolder(var binding: RvCategoryMasterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setDataToView(payment: Payment) {
            setTextView(payment)
            setClickListener(payment)
            setOnCheckedListener(payment)
        }

        private fun setOnCheckedListener(payment: Payment) {
            binding.swCmStatus.setOnCheckedChangeListener { v, _ ->
                run {
                    payment.isActive = v.isChecked
                    viewModel.updatePayment(payment)
                }
            }
        }

        private fun setClickListener(payment: Payment) {
            binding.root.setOnClickListener {
                PaymentMasterFragment(payment).show(
                    activity.supportFragmentManager,
                    "detail-payment"
                )
            }
        }

        private fun setTextView(payment: Payment) {
            val title = when (payment.tax) {
                0f -> payment.name
                else -> "${payment.name}, Pajak ${payment.tax}%"
            }
            binding.tvRvCtName.text = title
            binding.tvRvCtDesc.text = payment.desc
            binding.swCmStatus.isChecked = payment.isActive
        }
    }
}
