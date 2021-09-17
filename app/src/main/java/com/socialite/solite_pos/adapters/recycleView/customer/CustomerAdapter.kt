package com.socialite.solite_pos.adapters.recycleView.customer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.R
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.databinding.RvStringListBinding
import com.socialite.solite_pos.utils.tools.RecycleViewDiffUtils

class CustomerAdapter(private val callback: (Customer) -> Unit) :
	RecyclerView.Adapter<CustomerAdapter.ListViewHolder>() {

	private var customers: ArrayList<Customer> = ArrayList()

	fun setCustomers(customers: ArrayList<Customer>) {
		val customersDiffUtil = RecycleViewDiffUtils(this.customers, customers)
		val diffUtilResult = DiffUtil.calculateDiff(customersDiffUtil)
		this.customers = customers
		diffUtilResult.dispatchUpdatesTo(this)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = RvStringListBinding.inflate(
			inflater,
			parent,
			false
		)
		return ListViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		holder.setDataToView(customers[position])
	}

	override fun getItemCount(): Int {
		return customers.size
	}

	inner class ListViewHolder(val binding: RvStringListBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun setDataToView(customer: Customer) {
			setView(customer)
			setAddButton(customer)
		}

		private fun setView(customer: Customer) {
			binding.tvRvSt.text = customer.name
			binding.root.setOnClickListener { callback.invoke(customer) }
		}

		private fun setAddButton(customer: Customer) {
			if (customer.id == Customer.ID_ADD) {
				val add = ContextCompat.getDrawable(binding.root.context, R.drawable.ic_add_circle)
				binding.tvRvSt.setCompoundDrawablesRelativeWithIntrinsicBounds(
					add,
					null,
					null,
					null
				)
			} else {
				binding.tvRvSt.setCompoundDrawablesRelativeWithIntrinsicBounds(
					null,
					null,
					null,
					null
				)
			}
		}
	}
}
