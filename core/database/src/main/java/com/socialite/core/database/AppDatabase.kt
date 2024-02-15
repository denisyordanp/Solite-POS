package com.socialite.core.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.socialite.core.database.dao.CategoriesDao
import com.socialite.core.database.dao.CustomersDao
import com.socialite.core.database.dao.OrderDetailsDao
import com.socialite.core.database.dao.OrderPaymentsDao
import com.socialite.core.database.dao.OrderProductVariantsDao
import com.socialite.core.database.dao.OrderPromosDao
import com.socialite.core.database.dao.OrdersDao
import com.socialite.core.database.dao.OutcomesDao
import com.socialite.core.database.dao.PaymentsDao
import com.socialite.core.database.dao.ProductVariantsDao
import com.socialite.core.database.dao.ProductsDao
import com.socialite.core.database.dao.PromosDao
import com.socialite.core.database.dao.PurchasesDao
import com.socialite.core.database.dao.StoreDao
import com.socialite.core.database.dao.SuppliersDao
import com.socialite.core.database.dao.UserDao
import com.socialite.core.database.dao.VariantMixesDao
import com.socialite.core.database.dao.VariantOptionsDao
import com.socialite.core.database.dao.VariantsDao
import com.socialite.schema.database.new_bridge.OrderDetail as NewOrderDetail
import com.socialite.schema.database.new_bridge.OrderPayment as NewOrderPayment
import com.socialite.schema.database.new_bridge.OrderProductVariant as NewOrderProductVariant
import com.socialite.schema.database.new_bridge.OrderPromo as NewOrderPromo
import com.socialite.schema.database.new_bridge.VariantProduct as NewVariantProduct
import com.socialite.schema.database.new_master.Category as NewCategory
import com.socialite.schema.database.new_master.Customer as NewCustomer
import com.socialite.schema.database.new_master.Order as NewOrder
import com.socialite.schema.database.new_master.Outcome as NewOutcome
import com.socialite.schema.database.new_master.Payment as NewPayment
import com.socialite.schema.database.new_master.Product as NewProduct
import com.socialite.schema.database.new_master.Promo as NewPromo
import com.socialite.schema.database.new_master.Store as NewStore
import com.socialite.schema.database.new_master.Variant as NewVariant
import com.socialite.schema.database.new_master.VariantOption as NewVariantOption

@Database(
    entities = [
        com.socialite.schema.database.master.Category::class,
        com.socialite.schema.database.master.Customer::class,
        com.socialite.schema.database.master.Order::class,
        com.socialite.schema.database.master.Purchase::class,
        com.socialite.schema.database.master.PurchaseProduct::class,
        com.socialite.schema.database.master.Payment::class,
        com.socialite.schema.database.master.Product::class,
        com.socialite.schema.database.master.Variant::class,
        com.socialite.schema.database.master.Outcome::class,
        com.socialite.schema.database.master.Supplier::class,
        com.socialite.schema.database.master.User::class,
        com.socialite.schema.database.bridge.OrderDetail::class,
        com.socialite.schema.database.bridge.OrderPayment::class,
        com.socialite.schema.database.bridge.OrderProductVariant::class,
        com.socialite.schema.database.bridge.OrderProductVariantMix::class,
        com.socialite.schema.database.bridge.OrderMixProductVariant::class,
        com.socialite.schema.database.bridge.VariantMix::class,
        com.socialite.schema.database.bridge.VariantProduct::class,
        com.socialite.schema.database.master.VariantOption::class,
        com.socialite.schema.database.master.Store::class,
        com.socialite.schema.database.master.Promo::class,
        com.socialite.schema.database.bridge.OrderPromo::class,
        NewCategory::class,
        NewVariantOption::class,
        NewVariant::class,
        NewStore::class,
        NewPromo::class,
        NewProduct::class,
        NewPayment::class,
        NewOutcome::class,
        NewOrder::class,
        NewCustomer::class,
        NewVariantProduct::class,
        NewOrderPromo::class,
        NewOrderProductVariant::class,
        NewOrderPayment::class,
        NewOrderDetail::class,
    ],
    version = 10,
    autoMigrations = [
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9),
        AutoMigration(from = 9, to = 10),
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun paymentsDao(): PaymentsDao
    abstract fun suppliersDao(): SuppliersDao
    abstract fun customersDao(): CustomersDao
    abstract fun variantsDao(): VariantsDao
    abstract fun variantOptionsDao(): VariantOptionsDao
    abstract fun categoriesDao(): CategoriesDao
    abstract fun outcomesDao(): OutcomesDao
    abstract fun productsDao(): ProductsDao
    abstract fun productVariantsDao(): ProductVariantsDao
    abstract fun purchasesDao(): PurchasesDao
    abstract fun ordersDao(): OrdersDao
    abstract fun orderDetailsDao(): OrderDetailsDao
    abstract fun orderPaymentsDao(): OrderPaymentsDao
    abstract fun orderPromosDao(): OrderPromosDao
    abstract fun orderProductVariantsDao(): OrderProductVariantsDao
    abstract fun variantMixesDao(): VariantMixesDao
    abstract fun storeDao(): StoreDao
    abstract fun promoDao(): PromosDao
    abstract fun userDao(): UserDao

    companion object {

        const val DB_NAME = "solite_db"

        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `${com.socialite.schema.database.master.Promo.DB_NAME}` (`${com.socialite.schema.database.master.Promo.ID}` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `${com.socialite.schema.database.master.Promo.NAME}` TEXT NOT NULL, " +
                            "`${com.socialite.schema.database.master.Promo.DESC}` TEXT NOT NULL, `${com.socialite.schema.database.master.Promo.CASH}` INTEGER DEFAULT 0 NOT NULL, `${com.socialite.schema.database.master.Promo.VALUE}` INTEGER," +
                            "`${com.socialite.schema.database.master.Promo.STATUS}` INTEGER DEFAULT 0 NOT NULL, `${com.socialite.schema.database.master.Promo.UPLOAD}` INTEGER DEFAULT 0 NOT NULL)"
                )
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_promo_id_promo` ON `${com.socialite.schema.database.master.Promo.DB_NAME}` (`${com.socialite.schema.database.master.Promo.ID}`)")
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
                        .addMigrations(MIGRATION_4_5)
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}

