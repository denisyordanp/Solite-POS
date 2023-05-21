package com.socialite.solite_pos.utils.config

import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import java.util.*

class ProductUtils {
    companion object{
        fun find(array: ArrayList<ProductOrderDetail>, detail: ProductOrderDetail?): Int? {
            for ((i, v) in array.withIndex()) {
                if (v.product == null) continue
                if (
                        isEqual(v.product, detail?.product)
                        &&
                        isEqual(v.variants, detail?.variants)
                ) {
                    return i
                }
            }
            return null
        }

        private fun isEqual(any1: Any?, any2: Any?): Boolean {
            return any1 == any2
        }
    }
}