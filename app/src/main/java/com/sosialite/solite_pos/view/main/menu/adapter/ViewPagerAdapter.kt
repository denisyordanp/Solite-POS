package com.sosialite.solite_pos.view.main.menu.adapter

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sosialite.solite_pos.R
import com.sosialite.solite_pos.utils.tools.helper.FragmentWithTitle


class ViewPagerAdapter(private val fa: FragmentActivity) : FragmentStateAdapter(fa) {

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

//	fun getTabView(position: Int): View {
//		val v: View = LayoutInflater.from(fa).inflate(R.layout.tab_custom, null)
//		val tv = v.findViewById<View>(R.id.tv_tab_name) as TextView
//		tv.text = fragments[position].title
//		return v
//	}
}
