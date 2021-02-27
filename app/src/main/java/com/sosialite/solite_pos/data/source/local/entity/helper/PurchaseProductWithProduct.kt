package com.sosialite.solite_pos.data.source.local.entity.helper

import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
import com.sosialite.solite_pos.data.source.local.entity.room.master.PurchaseProduct
import java.io.Serializable

data class PurchaseProductWithProduct(
        var purchaseProduct: PurchaseProduct?,
        var product: Product?,
        var type: Int?
): Serializable{

    constructor(purchaseProduct: PurchaseProduct?, product: Product?): this(purchaseProduct, product, null)
    constructor(type: Int?): this(null, null, type)
    companion object{

        private const val TITLE = 1
        private const val GRAND = 2

        val title: PurchaseProductWithProduct
        get() {
            return PurchaseProductWithProduct(TITLE)
        }

        val grand: PurchaseProductWithProduct
            get() {
                return PurchaseProductWithProduct(GRAND)
            }
    }
}