package com.sosialite.solite_pos.view.main.menu.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sosialite.solite_pos.databinding.FragmentMasterBinding
import com.sosialite.solite_pos.view.main.menu.master.ListMasterActivity
import com.sosialite.solite_pos.view.main.menu.master.product.ProductMasterActivity

class MasterFragment : Fragment() {

	private lateinit var _binding: FragmentMasterBinding

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View {
		_binding = FragmentMasterBinding.inflate(inflater, container, false)
		return _binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (activity != null){

			_binding.btnMtProduct.setOnClickListener { startActivity(Intent(activity, ProductMasterActivity::class.java)) }
			_binding.btnMtCategory.setOnClickListener {
				startActivity(
						Intent(activity, ListMasterActivity::class.java)
								.putExtra(ListMasterActivity.TYPE, ListMasterActivity.CATEGORY)
				)
			}
			_binding.btnMtVariant.setOnClickListener {
				startActivity(
						Intent(activity, ListMasterActivity::class.java)
								.putExtra(ListMasterActivity.TYPE, ListMasterActivity.VARIANT)
				)
			}
			_binding.btnMtPayment.setOnClickListener {
				startActivity(
					Intent(activity, ListMasterActivity::class.java)
						.putExtra(ListMasterActivity.TYPE, ListMasterActivity.PAYMENT)
				)
			}
			_binding.btnMtSupplier.setOnClickListener {
				startActivity(
					Intent(activity, ListMasterActivity::class.java)
						.putExtra(ListMasterActivity.TYPE, ListMasterActivity.SUPPLIER)
				)
			}
		}
	}
}
