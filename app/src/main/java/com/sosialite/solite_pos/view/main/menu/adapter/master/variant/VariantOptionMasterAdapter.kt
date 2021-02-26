package com.sosialite.solite_pos.view.main.menu.adapter.master.variant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.sosialite.solite_pos.databinding.RvVariantOptionMasterBinding
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.toRupiah
import com.sosialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.sosialite.solite_pos.view.main.menu.master.bottom.VariantOptionFragment
import com.sosialite.solite_pos.view.viewmodel.MainViewModel
import com.sosialite.solite_pos.vo.Status

class VariantOptionMasterAdapter(
	private val product: Product?,
	private val viewModel: MainViewModel,
	private val fragmentManager: FragmentManager,
	private val activity: SocialiteActivity
) : RecyclerView.Adapter<VariantOptionMasterAdapter.ListViewHolder>() {

	var items: ArrayList<VariantOption> = ArrayList()
		set(value) {
			if (field.isNotEmpty()){
				field.clear()
			}
			field.addAll(value)
			notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
		return ListViewHolder(RvVariantOptionMasterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val vo = items[position]

		holder.binding.tvRvVoName.text = vo.name
		holder.binding.tvRvVoDesc.text = vo.desc
		holder.binding.swVoOptions.isChecked = vo.isActive
		holder.checkOption(vo)
	}

	override fun getItemCount(): Int {
		return items.size
	}

	inner class ListViewHolder(var binding: RvVariantOptionMasterBinding) : RecyclerView.ViewHolder(binding.root){

		fun checkOption(vo: VariantOption){
			if (product != null){
				viewModel.getVariantProduct(product.id, vo.id)
						.observe(activity){
							when(it.status){
								Status.LOADING -> { }
								Status.SUCCESS -> {
									binding.swVoOptions.isChecked = !it.data.isNullOrEmpty()
								}
								Status.ERROR -> { }
							}
						}
			}
			setOption(vo)
		}

		private fun setOption(vo: VariantOption){
			if (product != null){
				binding.swVoOptions.setOnCheckedChangeListener{ v, _ ->
					val variantProduct = VariantProduct(vo.idVariant, vo.id, product.id)
					if (v.isChecked){
						viewModel.insertVariantProduct(variantProduct) {}
					}else{
						viewModel.removeVariantProduct(variantProduct) {}
					}
				}
			}else{
				binding.swVoOptions.setOnCheckedChangeListener{ v, _ ->
					run {
						vo.isActive = v.isChecked
						viewModel.updateVariantOption(vo) {}
					}
				}
				itemView.setOnClickListener {
					VariantOptionFragment(vo).show(fragmentManager, "detail-variant-option")
				}
			}
		}
	}
}
