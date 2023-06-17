package com.socialite.solite_pos.view.order_customer

import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.helper.VariantWithOptions

data class OrderSelectVariantsState(
    val selectedProductVariantOptions: List<VariantWithOptions>?,
    val productOrderDetail: ProductOrderDetail
) {
    companion object {
        fun idle() = OrderSelectVariantsState(
            selectedProductVariantOptions = null,
            productOrderDetail = ProductOrderDetail.empty()
        )
    }
}
