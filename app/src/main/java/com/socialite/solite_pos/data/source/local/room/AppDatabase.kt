package com.socialite.solite_pos.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderMixProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariantMix
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.Outcome
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.Purchase
import com.socialite.solite_pos.data.source.local.entity.room.master.PurchaseProduct
import com.socialite.solite_pos.data.source.local.entity.room.master.Store
import com.socialite.solite_pos.data.source.local.entity.room.master.Supplier
import com.socialite.solite_pos.data.source.local.entity.room.master.User
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption

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
            Store::class],
        version = 3,
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
    abstract fun outcomesDao(): OutcomesDao
    abstract fun productsDao(): ProductsDao
    abstract fun productVariantsDao(): ProductVariantsDao
    abstract fun purchasesDao(): PurchasesDao
    abstract fun ordersDao(): OrdersDao
    abstract fun variantMixesDao(): VariantMixesDao
    abstract fun storeDao(): StoreDao

    companion object {

        const val DB_NAME = "solite_db"
        const val UPLOAD = "upload"
        const val MAIN = "main"

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
//							.fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}
