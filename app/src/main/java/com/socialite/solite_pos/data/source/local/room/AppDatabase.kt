package com.socialite.solite_pos.data.source.local.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.socialite.solite_pos.data.source.local.entity.room.bridge.*
import com.socialite.solite_pos.data.source.local.entity.room.master.*
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderDetail as NewOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPayment as NewOrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderProductVariant as NewOrderProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPromo as NewOrderPromo
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.VariantProduct as NewVariantProduct
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Category as NewCategory
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Customer as NewCustomer
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Order as NewOrder
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Outcome as NewOutcome
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Payment as NewPayment
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Product as NewProduct
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Promo as NewPromo
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Store as NewStore
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Variant as NewVariant
import com.socialite.solite_pos.data.source.local.entity.room.new_master.VariantOption as NewVariantOption

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
                            "`${Promo.STATUS}` INTEGER DEFAULT 0 NOT NULL, `${UPLOAD}` INTEGER DEFAULT 0 NOT NULL)"
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

