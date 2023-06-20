package com.socialite.solite_pos.view.order_customer.select_variant

import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.helper.VariantWithOptions

data class SelectVariantsViewState(
    val selectedProductVariantOptions: List<VariantWithOptions>?,
    val productOrderDetail: ProductOrderDetail
) {
    companion object {
        fun idle() = SelectVariantsViewState(
            selectedProductVariantOptions = null,
            productOrderDetail = ProductOrderDetail.empty()
        )
    }
}
