package com.socialite.solite_pos.utils.database

import android.content.Context
import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors private constructor(
		private val diskIO: Executor,
		private val networkIO: Executor,
		private val mainThread: Executor
) {
	var context: Context? = null
		private set

	constructor(context: Context?) : this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(THREAD_COUNT),
			MainThreadExecutor()) {
		this.context = context
	}

	fun diskIO(): Executor {
		return diskIO
	}

	fun mainThread(): Executor {
		return mainThread
	}

//	fun networkIO(): Executor {
//		return networkIO
//	}

	private class MainThreadExecutor : Executor {
		private val mainThreadHandler = Handler(Looper.getMainLooper())
		override fun execute(command: Runnable) {
			mainThreadHandler.post(command)
		}
	}

	companion object {
		private const val THREAD_COUNT = 3
	}
}
