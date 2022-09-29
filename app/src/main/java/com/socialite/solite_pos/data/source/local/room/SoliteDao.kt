package com.socialite.solite_pos.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseProductWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderMixProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariantMix
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantMix
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.socialite.solite_pos.data.source.local.entity.room.helper.DetailProductMixWithVariantOption
import com.socialite.solite_pos.data.source.local.entity.room.helper.DetailWithVariantMixOption
import com.socialite.solite_pos.data.source.local.entity.room.helper.DetailWithVariantOption
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.helper.PurchaseWithSupplier
import com.socialite.solite_pos.data.source.local.entity.room.helper.VariantProductWithOption
import com.socialite.solite_pos.data.source.local.entity.room.helper.VariantWithVariantMix
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.Purchase
import com.socialite.solite_pos.data.source.local.entity.room.master.PurchaseProduct
import com.socialite.solite_pos.data.source.local.entity.room.master.User
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption

@Dao
interface SoliteDao {

//	ORDER

    @Transaction
    @Query("SELECT * FROM '${Order.DB_NAME}' WHERE ${Order.STATUS} = :status AND date(${Order.ORDER_DATE}) = date(:date)")
    fun getOrdersByStatus(status: Int, date: String): LiveData<List<OrderData>>

    @Transaction
    @Query("SELECT * FROM '${Order.DB_NAME}' WHERE ${Order.NO} = :orderNo")
    fun getOrderByNo(orderNo: String): LiveData<OrderData>

    @Query("SELECT * FROM ${OrderDetail.DB_NAME} WHERE ${Order.NO} = :orderNo")
    fun getDetailOrdersLiveData(orderNo: String): LiveData<List<OrderDetail>>

    @Transaction
    @Query("SELECT * FROM '${Order.DB_NAME}' WHERE ${Order.NO} = :orderNo")
    fun getOrderPayment(orderNo: String): LiveData<OrderData?>

    @Transaction
    @Query("SELECT * FROM ${OrderDetail.DB_NAME} WHERE ${OrderDetail.ID} = :idDetail")
    fun getOrderVariants(idDetail: Long): DetailWithVariantOption

    @Transaction
    @Query("SELECT * FROM ${OrderProductVariantMix.DB_NAME} WHERE ${OrderProductVariantMix.ID} = :idMix")
    fun getOrderMixVariantsOption(idMix: Long): DetailProductMixWithVariantOption

    @Transaction
    @Query("SELECT * FROM ${OrderDetail.DB_NAME} WHERE ${OrderDetail.ID} = :idDetail")
    fun getOrderVariantsMix(idDetail: Long): DetailWithVariantMixOption

    @Query("SELECT * FROM ${OrderDetail.DB_NAME} WHERE ${Order.NO} = :orderNo AND ${Product.ID} = :productId")
    fun getDetailOrders(orderNo: String, productId: Long): OrderDetail

    @Query("SELECT * FROM ${OrderProductVariantMix.DB_NAME} WHERE ${OrderDetail.ID} = :detailId AND ${Product.ID} = :productId")
    fun getOrderProductVariantMix(detailId: Long, productId: Long): OrderProductVariantMix

    @Query("SELECT * FROM ${OrderMixProductVariant.DB_NAME} WHERE ${OrderProductVariantMix.ID} = :orderVariantId AND ${VariantOption.ID} = :variantOptionId")
    fun getOrderMixProductVariant(
        orderVariantId: Long,
        variantOptionId: Long
    ): OrderMixProductVariant

    @Query("SELECT * FROM ${OrderProductVariant.DB_NAME} WHERE ${OrderDetail.ID} = :orderDetailId AND ${VariantOption.ID} = :variantOptionId")
    fun getVariantOrder(orderDetailId: Long, variantOptionId: Long): OrderProductVariant

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrder(order: Order): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrders(order: List<Order>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDetailOrder(detail: OrderDetail): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDetailOrders(detail: List<OrderDetail>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVariantOrder(variants: OrderProductVariant): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVariantOrders(variants: List<OrderProductVariant>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMixVariantOrder(variants: OrderMixProductVariant): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMixVariantOrders(variants: List<OrderMixProductVariant>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVariantMixOrder(variants: OrderProductVariantMix): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVariantMixOrders(variants: List<OrderProductVariantMix>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPaymentOrders(payment: List<OrderPayment>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPaymentOrder(payment: OrderPayment): Long

    @Update
    fun updatePaymentOrder(order: OrderPayment)

    @Update
    fun updateOrder(order: Order)

    @Delete
    fun deleteOrderDetail(orderDetail: OrderDetail)

    @Delete
    fun deleteOrderProductVariantMix(variantMix: OrderProductVariantMix)

    @Delete
    fun deleteOrderMixProductVariant(orderMix: OrderMixProductVariant)

    @Delete
    fun deleteOrderVariant(orderVariant: OrderProductVariant)

//	PURCHASE

    @Query("SELECT * FROM ${Purchase.DB_NAME}")
    fun getPurchases(): LiveData<List<PurchaseWithSupplier>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPurchases(data: List<Purchase>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPurchase(data: Purchase)

    @Update
    fun updatePurchase(data: Purchase)

    @Query("SELECT * FROM ${PurchaseProduct.DB_NAME} WHERE ${Purchase.NO} = :purchaseNo")
    fun getPurchasesProduct(purchaseNo: String): LiveData<List<PurchaseProductWithProduct>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPurchaseProducts(data: List<PurchaseProduct>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPurchaseProduct(data: PurchaseProduct)

    @Update
    fun updatePurchaseProduct(data: PurchaseProduct)

//	PRODUCT

    @Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct")
    fun getProduct(idProduct: Long): Product

    @Query("UPDATE ${Product.DB_NAME} SET ${Product.STOCK} = ((SELECT ${Product.STOCK} FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct) + :amount) WHERE ${Product.ID} = :idProduct")
    fun increaseProductStock(idProduct: Long, amount: Int)

    @Query("UPDATE ${Product.DB_NAME} SET ${Product.STOCK} = ((SELECT ${Product.STOCK} FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct) - :amount) WHERE ${Product.ID} = :idProduct")
    fun decreaseProductStock(idProduct: Long, amount: Int)

    @Transaction
    fun decreaseAndGetProduct(idProduct: Long, amount: Int): Product {
        decreaseProductStock(idProduct, amount)
        return getProduct(idProduct)
    }

    @Transaction
    fun increaseAndGetProduct(idProduct: Long, amount: Int): Product {
        increaseProductStock(idProduct, amount)
        return getProduct(idProduct)
    }

//	VARIANT PRODUCT

    @Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Category.ID} = :idCategory")
    fun getProducts(idCategory: Long): LiveData<List<ProductWithCategory>>

    @Transaction
    @Query("SELECT * FROM ${VariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct ORDER BY ${Variant.ID}")
    fun getVariantProducts(idProduct: Long): LiveData<List<VariantProductWithOption>?>

//	VARIANT MIX

    @Transaction
    @Query("SELECT * FROM ${Variant.DB_NAME} WHERE ${Variant.ID} = :idVariant")
    fun getVariantMixProduct(idVariant: Long): LiveData<VariantWithVariantMix>

    @Transaction
    @Query("SELECT * FROM ${VariantMix.DB_NAME} WHERE ${Variant.ID} = :idVariant AND ${Product.ID} = :idProduct")
    fun getVariantMixProductById(idVariant: Long, idProduct: Long): LiveData<VariantMix?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVariantMixes(data: List<VariantMix>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVariantMix(data: VariantMix): Long

    @Update
    fun updateVariantMix(data: VariantMix)

    @Query("DELETE FROM ${VariantMix.DB_NAME} WHERE ${VariantMix.ID} = :idVariantMix")
    fun removeVariantMix(idVariantMix: Long)

//	CATEGORY

	@Query("SELECT * FROM ${Category.DB_NAME} WHERE ${Category.ID} = :idCategory")
	fun getCategoryById(idCategory: Long): Category

    @Query("SELECT * FROM ${User.DB_NAME} WHERE ${User.ID} = :userId")
    fun getUser(userId: String): LiveData<User?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUsers(data: List<User>)
}
