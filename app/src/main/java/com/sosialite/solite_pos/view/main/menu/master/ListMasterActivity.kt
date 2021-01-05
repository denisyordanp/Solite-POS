package com.sosialite.solite_pos.view.main.menu.master

import android.os.Bundle
import com.sosialite.solite_pos.databinding.ActivityListMasterBinding
import com.sosialite.solite_pos.utils.tools.helper.DataDummy
import com.sosialite.solite_pos.utils.tools.helper.FragmentWithTitle
import com.sosialite.solite_pos.utils.tools.helper.SocialiteActivity
import com.sosialite.solite_pos.view.main.menu.adapter.ViewPagerAdapter
import com.sosialite.solite_pos.view.main.menu.master.product.ProductMasterFragment

class ListMasterActivity : SocialiteActivity() {

	private lateinit var _binding: ActivityListMasterBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityListMasterBinding.inflate(layoutInflater)
		setContentView(_binding.root)


	}
}
