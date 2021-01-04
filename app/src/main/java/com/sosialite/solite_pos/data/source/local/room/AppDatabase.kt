package com.sosialite.solite_pos.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sosialite.solite_pos.data.source.local.entity.main.Category
import com.sosialite.solite_pos.data.source.local.entity.main.Customer
import com.sosialite.solite_pos.data.source.local.entity.main.Product
import com.sosialite.solite_pos.utils.tools.helper.KeyString

@Database(
		entities = [
			Product::class,
			Customer::class,
			Category::class
				   ],
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
							KeyString.Database.DB_NAME
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
