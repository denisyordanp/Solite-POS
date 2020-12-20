package com.sosialite.solite_pos.data.source.local.room

class LocalDataSource private constructor(private val soliteDao: SoliteDao) {

	companion object {
		private var INSTANCE: LocalDataSource? = null


		fun getInstance(soliteDao: SoliteDao): LocalDataSource {
			if (INSTANCE == null) {
				INSTANCE = LocalDataSource(soliteDao)
			}
			return INSTANCE!!
		}
	}
}
