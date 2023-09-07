package com.socialite.data.schema.helper

import com.socialite.data.schema.room.master.Purchase
import com.socialite.data.schema.room.master.PurchaseProduct
import com.socialite.data.schema.room.master.Supplier

data class PurchaseWithProduct(
    val purchase: Purchase,
    val supplier: Supplier,
    val products: List<PurchaseProductWithProduct>
) {

    val purchaseProduct: List<PurchaseProduct>
        get() {
            val array: ArrayList<PurchaseProduct> = ArrayList()
            for (item in products) {
                if (item.purchaseProduct != null) {
                    array.add(item.purchaseProduct)
                }
            }
            return array
        }
}
