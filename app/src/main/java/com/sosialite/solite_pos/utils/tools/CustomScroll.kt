package com.sosialite.solite_pos.utils.tools

import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller


class CustomScroll : Scroller {
	private var mScrollFactor = 1.0

	constructor (context: Context?) : super(context)

	constructor (context: Context?, interpolator: Interpolator?) : super(context, interpolator)

	constructor (context: Context?, interpolator: Interpolator?, flywheel: Boolean) : super(context, interpolator, flywheel)

	/**
	 * Set the factor by which the duration will change
	 */
	fun setScrollDurationFactor(scrollFactor: Double) {
		mScrollFactor = scrollFactor
	}

	override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
		super.startScroll(startX, startY, dx, dy, (duration * mScrollFactor).toInt())
	}
}
