package com.socialite.solite_pos.utils.config

import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import java.util.ArrayList

class FindProductOrderIndex {
    companion object{
        fun find(array: ArrayList<ProductOrderDetail>, detail: ProductOrderDetail?): Int?{
            for ((i, v) in array.withIndex()){
                if (v.product == null) continue
                if (isEquals(v.product, detail?.product) && isEquals(v.variants, detail?.variants)){
                    return i
                }
            }
            return null
        }

        private fun isEquals(any1:Any?, any2: Any?): Boolean{
            return any1 == any2
        }
    }
}