package com.socialite.solite_pos.view.main.menu.adapter.master.variant

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.socialite.solite_pos.databinding.RvVariantOptionMasterBinding
import com.socialite.solite_pos.view.main.menu.master.bottom.VariantOptionFragment
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import com.socialite.solite_pos.vo.Status

class VariantOptionMasterAdapter(
	private val product: Product?,
	private val activity: FragmentActivity
) : RecyclerView.Adapter<VariantOptionMasterAdapter.ListViewHolder>() {

	private var viewModel = ProductViewModel.getMainViewModel(activity)

	var items: ArrayList<VariantOption> = ArrayList()
		@SuppressLint("NotifyDataSetChanged")
		set(value) {
			if (field.isNotEmpty()) {
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
									binding.swVoOptions.isChecked = it.data != null
									binding.swVoOptions.setOnCheckedChangeListener{ _, _ ->
										if (it.data == null){
											val variantProduct = VariantProduct(vo.idVariant, vo.id, product.id)
											viewModel.insertVariantProduct(variantProduct) {}
										}else{
											viewModel.removeVariantProduct(it.data) {}
										}
									}
								}
								Status.ERROR -> { }
							}
						}
			}else{
				binding.swVoOptions.isChecked = vo.isActive
				binding.swVoOptions.setOnCheckedChangeListener{ v, _ ->
					run {
						vo.isActive = v.isChecked
						viewModel.updateVariantOption(vo) {}
					}
				}
				itemView.setOnClickListener {
					VariantOptionFragment(vo).show(activity.supportFragmentManager, "detail-variant-option")
				}
			}
		}
	}
}
