package com.sosialite.solite_pos.view.bluetooth.adapter

import android.bluetooth.BluetoothDevice
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.databinding.RvStringListBinding

class DeviceAdapter(
		private val savedAddress: String?,
		private val callback: (BluetoothDevice) -> Unit
) : RecyclerView.Adapter<DeviceAdapter.ListViewHolder>(){

	private val items: ArrayList<BluetoothDevice> = ArrayList()

	fun setItems(items: ArrayList<BluetoothDevice>){
		this.items.addAll(items)
		notifyDataSetChanged()
	}

	fun addItem(item: BluetoothDevice){
		items.add(item)
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvStringListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val device = items[position]
		val text = "${device.name} \n ${device.address}".trimIndent()

		holder.binding.tvRvSt.text = text
		if (device.address == savedAddress){
			holder.binding.root.setCardBackgroundColor(ColorStateList.valueOf(ResourcesCompat.getColor(holder.itemView.context.resources, R.color.colorAccent, null)))
		}else{
			holder.binding.root.setOnClickListener { callback.invoke(device) }
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(val binding: RvStringListBinding) : RecyclerView.ViewHolder(binding.root)
}
