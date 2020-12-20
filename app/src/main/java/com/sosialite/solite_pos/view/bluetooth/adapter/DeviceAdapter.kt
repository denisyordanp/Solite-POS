package com.sosialite.solite_pos.view.bluetooth.adapter

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.databinding.RvDeviceBluetoothListBinding

class DeviceAdapter(private val callback: (BluetoothDevice) -> Unit) : RecyclerView.Adapter<DeviceAdapter.ListViewHolder>(){

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
		return ListViewHolder(RvDeviceBluetoothListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val device = items[position]
		val text = "${device.name} \n ${device.address}".trimIndent()

		holder.binding.tvRvDvBtName.text = text
		holder.itemView.setOnClickListener { callback.invoke(device) }
	}

	override fun getItemCount(): Int {
		return items.size
	}

	class ListViewHolder(val binding: RvDeviceBluetoothListBinding) : RecyclerView.ViewHolder(binding.root)
}
