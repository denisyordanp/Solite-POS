package com.socialite.solite_pos.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.socialite.solite_pos.data.source.local.entity.room.bridge.*
import com.socialite.solite_pos.data.source.local.entity.room.master.*

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
            User::class,
            OrderDetail::class,
            OrderPayment::class,
            OrderProductVariant::class,
            OrderProductVariantMix::class,
            OrderMixProductVariant::class,
            VariantMix::class,
            VariantProduct::class,
            VariantOption::class],
        version = 2,
        exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun soliteDao(): SoliteDao
    abstract fun paymentsDao(): PaymentsDao
    abstract fun suppliersDao(): SuppliersDao
    abstract fun customersDao(): CustomersDao
    abstract fun variantsDao(): VariantsDao
    abstract fun variantOptionsDao(): VariantOptionsDao
    abstract fun categoriesDao(): CategoriesDao

    companion object {

        const val DB_NAME = "solite_db"
        const val UPLOAD = "upload"
        const val MAIN = "main"

        private var INSTANCE: AppDatabase? = null

        private var migration_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE '${Product.DB_NAME}' ADD COLUMN ${Product.MIX} INTEGER NOT NULL DEFAULT 0")
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
