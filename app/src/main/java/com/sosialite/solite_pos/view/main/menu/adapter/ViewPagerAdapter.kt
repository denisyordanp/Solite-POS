package com.sosialite.solite_pos.view.main.menu.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sosialite.solite_pos.utils.tools.helper.FragmentWithTitle

class ViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

	private var fragments: ArrayList<FragmentWithTitle> = ArrayList()

	fun setData(fragments: ArrayList<FragmentWithTitle>) {
		if (this.fragments.isNotEmpty()) {
			this.fragments.clear()
		}
		this.fragments.addAll(fragments)
		notifyDataSetChanged()
	}

	override fun getCount(): Int {
		return fragments.size
	}

	override fun getItem(position: Int): Fragment {
		return if (fragments.isNotEmpty()) {
			fragments[position].fragment
		} else {
			Fragment()
		}
	}

	override fun getPageTitle(position: Int): CharSequence {
		return fragments[position].title
	}
}
