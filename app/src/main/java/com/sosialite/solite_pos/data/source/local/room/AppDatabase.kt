package com.sosialite.solite_pos.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.*
import com.sosialite.solite_pos.data.source.local.entity.room.master.*

@Database(
		entities = [
			Category::class,
			Customer::class,
			Order::class,
			Purchase::class,
			PurchaseProduct::class,
			Payment::class,
			Product::class,
			Variant::class,
			Outcome::class,
			Supplier::class,
			OrderDetail::class,
			OrderPayment::class,
			OrderProductVariant::class,
			OrderProductVariantMix::class,
			VariantMix::class,
			VariantProduct::class,
			VariantOption::class],
		version = 2,
		exportSchema = false
) abstract class AppDatabase : RoomDatabase() {
	abstract fun soliteDao(): SoliteDao

	companion object {

		private const val DB_NAME = "solite_db"

		const val TBL_ORDER_PRODUCT_VARIANT_MIX = "order_product_variant_mix"
		const val TBL_ORDER_PRODUCT_VARIANT = "order_product_variant"
		const val TBL_PURCHASE_PRODUCT = "purchase_product"
		const val TBL_VARIANT_PRODUCT = "variant_product"
		const val TBL_VARIANT_OPTION = "variant_option"
		const val TBL_ORDER_PAYMENT = "order_payment"
		const val TBL_ORDER_DETAIL = "order_detail"
		const val TBL_VARIANT_MIX = "variant_mix"
		const val TBL_CATEGORY = "category"
		const val TBL_CUSTOMER = "customer"
		const val TBL_SUPPLIER = "supplier"
		const val TBL_PURCHASE = "purchase"
		const val TBL_OUTCOME = "outcome"
		const val TBL_PAYMENT = "payment"
		const val TBL_PRODUCT = "product"
		const val TBL_VARIANT = "variant"
		const val TBL_ORDER = "order"

		private var INSTANCE: AppDatabase? = null

		private var migration_1_2: Migration = object : Migration(1, 2) {
			override fun migrate(database: SupportSQLiteDatabase) {
				database.execSQL("ALTER TABLE '$TBL_PRODUCT' ADD COLUMN ${Product.MIX} INTEGER NOT NULL DEFAULT 0")
			}
		}

		fun getInstance(context: Context): AppDatabase {
			if (INSTANCE == null) {
				synchronized(this) {
					INSTANCE = Room.databaseBuilder(
							context.applicationContext,
							AppDatabase::class.java,
							DB_NAME
					)
							.allowMainThreadQueries()
							.addMigrations(migration_1_2)
//							.fallbackToDestructiveMigration()
							.build()
				}
			}
			return INSTANCE!!
		}
	}
}
