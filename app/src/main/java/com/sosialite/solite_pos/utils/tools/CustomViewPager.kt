package com.sosialite.solite_pos.utils.tools

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Interpolator
import androidx.viewpager.widget.ViewPager
import java.lang.reflect.Field

class CustomViewPager : ViewPager {
	constructor(context: Context) : super(context) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
		init()
	}

	private var mScroller: CustomScroll? = null

	private fun init() {
		// The majority of the magic happens here
		setPageTransformer(true, VerticalPageTransformer())
		// The easiest way to get rid of the overscroll drawing that happens on the left and right
		overScrollMode = OVER_SCROLL_NEVER

//		set scroll duration
		try {
			val viewpager: Class<*> = ViewPager::class.java
			val scroller: Field = viewpager.getDeclaredField("mScroller")
			scroller.isAccessible = true
			val interpolator: Field = viewpager.getDeclaredField("sInterpolator")
			interpolator.isAccessible = true
			mScroller = CustomScroll(context,
					interpolator.get(null) as Interpolator)
			scroller.set(this, mScroller)
			mScroller?.setScrollDurationFactor(4.0)
		} catch (e: Exception) {
		}
	}

	private inner class VerticalPageTransformer : PageTransformer {
		override fun transformPage(view: View, position: Float) {
			when {
				position < -1 -> { // [-Infinity,-1)
					// This page is way off-screen to the left.
					view.alpha = 0f
				}
				position <= 1 -> { // [-1,1]
					view.alpha = 1f

					// Counteract the default slide transition
					view.translationX = view.width * -position

					//set Y position to swipe in from top
					val yPosition = position * view.height
					view.translationY = yPosition
				}
				else -> { // (1,+Infinity]
					// This page is way off-screen to the right.
					view.alpha = 0f
				}
			}
		}
	}

	/**
	 * Swaps the X and Y coordinates of your touch event.
	 */
	private fun swapXY(ev: MotionEvent): MotionEvent {
		val width = width.toFloat()
		val height = height.toFloat()
		val newX = ev.y / height * width
		val newY = ev.x / width * height
		ev.setLocation(newX, newY)
		return ev
	}

	override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
		val intercepted = super.onInterceptTouchEvent(swapXY(ev))
		swapXY(ev) // return touch coordinates to original reference frame for any child views
		return intercepted
	}

	override fun onTouchEvent(ev: MotionEvent): Boolean {
//		return super.onTouchEvent(swapXY(ev))
		return false
	}
}
