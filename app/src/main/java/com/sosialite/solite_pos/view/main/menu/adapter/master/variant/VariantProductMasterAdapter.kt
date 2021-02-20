package com.sosialite.solite_pos.view.main.menu.adapter.master.variant

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import com.sosialite.solite_pos.data.source.local.entity.room.master.Variant
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.sosialite.solite_pos.databinding.RvVariantProductMasterBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.getViewModel
import com.sosialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.sosialite.solite_pos.view.main.menu.master.detail.VariantMixOptionActivity
import com.sosialite.solite_pos.view.main.menu.master.detail.VariantOptionActivity
import com.sosialite.solite_pos.view.viewmodel.MainViewModel

class VariantProductMasterAdapter(
	private val product: Product,
	private val activity: SocialiteActivity
	) : RecyclerView.Adapter<VariantProductMasterAdapter.ListViewHolder>() {

	var items: ArrayList<Variant> = ArrayList()
		set(value) {
			if (field.isNotEmpty()){
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvVariantProductMasterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val v = items[position]

		holder.binding.tvRvVrName.text = v.name
		holder.setData(v)
	}

	override fun getItemCount(): Int {
		return items.size
	}

	inner class ListViewHolder(var binding: RvVariantProductMasterBinding) : RecyclerView.ViewHolder(binding.root){

		val viewModel: MainViewModel = getViewModel(activity)

		fun setData(v: Variant){
			if(v.isMix){
				binding.cbVmMix.visibility = View.VISIBLE
				binding.btnVmOptions.visibility = View.INVISIBLE
				binding.tvRvVrOption.visibility = View.INVISIBLE

				binding.cbVmMix.isChecked = viewModel.getVariantProductById(product.id) != null
				binding.cbVmMix.setOnCheckedChangeListener{ _, b ->
					if (b){
						viewModel.insertVariantProduct(VariantProduct(v.id, 0, product.id))
					}else{
						viewModel.removeVariantProduct(VariantProduct(v.id, 0, product.id))
					}
				}
			}else{
				viewModel.getVariantOptions(VariantOption.getFilter(v.id, VariantOption.ACTIVE)).observe(activity, {
					val count = "Terdapat ${it.size} pilihan"
					binding.tvRvVrOption.text = count
				})
			}

			setIntent(v.isMix, v)
			checkType(v.type)
		}

		private fun setIntent(isMix: Boolean, variant: Variant){
			val intent = if (isMix){
				Intent(activity, VariantMixOptionActivity::class.java)
						.putExtra(VariantMixOptionActivity.VARIANT, variant)
			}else{
				Intent(activity, VariantOptionActivity::class.java)
						.putExtra(VariantOptionActivity.VARIANT, variant)
						.putExtra(VariantOptionActivity.PRODUCT, product)
			}
			binding.btnVmOptions.setOnClickListener { activity.startActivity(intent) }
		}

		private fun checkType(type: Int){
			when(type){
				Variant.ONE_OPTION -> binding.tvRvVrType.text = "Hanya pilih satu"
				Variant.MULTIPLE_OPTION -> binding.tvRvVrType.text = "Dapat pilih banyak"
			}
		}

	}
}
