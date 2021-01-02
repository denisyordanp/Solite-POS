package com.sosialite.solite_pos.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sosialite.solite_pos.data.source.local.entity.Product
import com.sosialite.solite_pos.utils.tools.KeyString

@Database(
		entities = [Product::class],
		version = 1,
		exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
	abstract fun soliteDao(): SoliteDao

	companion object {
		private var INSTANCE: AppDatabase? = null

		fun getInstance(context: Context): AppDatabase {
			if (INSTANCE == null) {
				synchronized(this) {
					INSTANCE = Room.databaseBuilder(
							context.applicationContext,
							AppDatabase::class.java,
							KeyString.BaseKey.DB_NAME
					)
							.allowMainThreadQueries()
							.fallbackToDestructiveMigration()
							.build()
				}
			}
			return INSTANCE!!
		}
	}
}
