package com.sosialite.solite_pos.view.main.menu.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sosialite.solite_pos.utils.tools.helper.FragmentWithTitle


class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

	private var fragments: ArrayList<FragmentWithTitle> = ArrayList()

	fun setData(fragments: ArrayList<FragmentWithTitle>) {
		if (this.fragments.isNotEmpty()) {
			this.fragments.clear()
		}
		this.fragments.addAll(fragments)
		notifyDataSetChanged()
	}

	override fun getItemCount(): Int = fragments.size

	override fun createFragment(position: Int): Fragment = fragments[position].fragment
}
