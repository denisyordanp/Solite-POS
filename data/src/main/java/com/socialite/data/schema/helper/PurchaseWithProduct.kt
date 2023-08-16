package com.socialite.data.schema.helper

import com.socialite.data.schema.room.master.Purchase
import com.socialite.data.schema.room.master.PurchaseProduct
import com.socialite.data.schema.room.master.Supplier
import java.io.Serializable

data class PurchaseWithProduct(
    var purchase: Purchase,
    var supplier: Supplier,
    var products: ArrayList<PurchaseProductWithProduct>
) : Serializable {

    val purchaseProduct: List<PurchaseProduct>
        get() {
            val array: ArrayList<PurchaseProduct> = ArrayList()
            for (item in products) {
                if (item.purchaseProduct != null) {
                    array.add(item.purchaseProduct!!)
                }
            }
            return array
        }
}
