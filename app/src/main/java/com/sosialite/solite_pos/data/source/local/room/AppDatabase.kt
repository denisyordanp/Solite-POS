package com.sosialite.solite_pos.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sosialite.solite_pos.data.source.local.entity.bridge.ProductVariant
import com.sosialite.solite_pos.data.source.local.entity.main.Category
import com.sosialite.solite_pos.data.source.local.entity.main.Customer
import com.sosialite.solite_pos.data.source.local.entity.main.Product
import com.sosialite.solite_pos.data.source.local.entity.main.Variant

@Database(
		entities = [
			Product::class,
			Customer::class,
			Category::class,
			Variant::class,
			ProductVariant::class
				   ],
		version = 1,
		exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
	abstract fun soliteDao(): SoliteDao

	companion object {

		private const val DB_NAME = "solite_db"

		const val TBL_PRODUCT_VARIANT = "product_variant"
		const val TBL_CATEGORY = "category"
		const val TBL_CUSTOMER = "customer"
		const val TBL_PRODUCT = "product"
		const val TBL_VARIANT = "variant"

		private var INSTANCE: AppDatabase? = null

		fun getInstance(context: Context): AppDatabase {
			if (INSTANCE == null) {
				synchronized(this) {
					INSTANCE = Room.databaseBuilder(
							context.applicationContext,
							AppDatabase::class.java,
							DB_NAME
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
