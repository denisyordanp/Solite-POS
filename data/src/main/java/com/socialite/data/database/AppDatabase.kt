package com.socialite.data.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.socialite.data.schema.room.bridge.*
import com.socialite.data.schema.room.master.*
import com.socialite.data.database.dao.CategoriesDao
import com.socialite.data.database.dao.CustomersDao
import com.socialite.data.database.dao.OrderDetailsDao
import com.socialite.data.database.dao.OrderPaymentsDao
import com.socialite.data.database.dao.OrderProductVariantsDao
import com.socialite.data.database.dao.OrderPromosDao
import com.socialite.data.database.dao.OrdersDao
import com.socialite.data.database.dao.OutcomesDao
import com.socialite.data.database.dao.PaymentsDao
import com.socialite.data.database.dao.ProductVariantsDao
import com.socialite.data.database.dao.ProductsDao
import com.socialite.data.database.dao.PromosDao
import com.socialite.data.database.dao.PurchasesDao
import com.socialite.data.database.dao.StoreDao
import com.socialite.data.database.dao.SuppliersDao
import com.socialite.data.database.dao.VariantMixesDao
import com.socialite.data.database.dao.VariantOptionsDao
import com.socialite.data.database.dao.VariantsDao
import com.socialite.data.schema.room.new_bridge.OrderDetail as NewOrderDetail
import com.socialite.data.schema.room.new_bridge.OrderPayment as NewOrderPayment
import com.socialite.data.schema.room.new_bridge.OrderProductVariant as NewOrderProductVariant
import com.socialite.data.schema.room.new_bridge.OrderPromo as NewOrderPromo
import com.socialite.data.schema.room.new_bridge.VariantProduct as NewVariantProduct
import com.socialite.data.schema.room.new_master.Category as NewCategory
import com.socialite.data.schema.room.new_master.Customer as NewCustomer
import com.socialite.data.schema.room.new_master.Order as NewOrder
import com.socialite.data.schema.room.new_master.Outcome as NewOutcome
import com.socialite.data.schema.room.new_master.Payment as NewPayment
import com.socialite.data.schema.room.new_master.Product as NewProduct
import com.socialite.data.schema.room.new_master.Promo as NewPromo
import com.socialite.data.schema.room.new_master.Store as NewStore
import com.socialite.data.schema.room.new_master.Variant as NewVariant
import com.socialite.data.schema.room.new_master.VariantOption as NewVariantOption

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
        VariantOption::class,
        Store::class,
        Promo::class,
        OrderPromo::class,
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
    version = 9,
    autoMigrations = [
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9),
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

    companion object {

        const val DB_NAME = "solite_db"
        const val UPLOAD = "upload"
        const val REPLACED_UUID = "replaced_uuid"

        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `${Promo.DB_NAME}` (`${Promo.ID}` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `${Promo.NAME}` TEXT NOT NULL, " +
                            "`${Promo.DESC}` TEXT NOT NULL, `${Promo.CASH}` INTEGER DEFAULT 0 NOT NULL, `${Promo.VALUE}` INTEGER," +
                            "`${Promo.STATUS}` INTEGER DEFAULT 0 NOT NULL, `$UPLOAD` INTEGER DEFAULT 0 NOT NULL)"
                )
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_promo_id_promo` ON `${Promo.DB_NAME}` (`${Promo.ID}`)")
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

