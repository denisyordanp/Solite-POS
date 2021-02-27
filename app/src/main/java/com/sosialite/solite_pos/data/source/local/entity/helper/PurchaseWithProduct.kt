package com.sosialite.solite_pos.data.source.local.entity.helper

import com.sosialite.solite_pos.data.source.local.entity.room.master.Purchase
import com.sosialite.solite_pos.data.source.local.entity.room.master.PurchaseProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.Supplier
import java.io.Serializable

data class PurchaseWithProduct(
        var purchase: Purchase,
        var supplier: Supplier,
        var products: ArrayList<PurchaseProductWithProduct>
): Serializable{

    constructor(purchase: Purchase, supplier: Supplier): this(purchase, supplier, ArrayList())

    val purchaseProduct: List<PurchaseProduct>
    get() {
        val array: ArrayList<PurchaseProduct> = ArrayList()
        for (item in products){
            if (item.purchaseProduct != null){
                array.add(item.purchaseProduct!!)
            }
        }
        return array
    }

    val totalPurchase: Long
    get() {
        var total = 0L
        for (item in products){
            if (item.purchaseProduct != null && item.product != null){
               total += item.purchaseProduct!!.amount * item.product!!.buyPrice
            }
        }
        return total
    }
}