package com.sosialite.solite_pos.data.source.remote

class RemoteDataSource {

	companion object {
		private var INSTANCE: RemoteDataSource? = null


		fun connect(): RemoteDataSource {
			if (INSTANCE == null) {
				INSTANCE = RemoteDataSource()
			}
			return INSTANCE!!
		}
	}
}
