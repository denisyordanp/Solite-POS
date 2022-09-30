package com.socialite.solite_pos.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderMixProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariantMix
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.User
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption

@Dao
interface SoliteDao {

//	ORDER

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
    fun insertDetailOrder(detail: OrderDetail): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVariantOrder(variants: OrderProductVariant): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMixVariantOrder(variants: OrderMixProductVariant): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVariantMixOrder(variants: OrderProductVariantMix): Long

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

    @Query("SELECT * FROM ${User.DB_NAME} WHERE ${User.ID} = :userId")
    fun getUser(userId: String): LiveData<User?>
}
