package com.socialite.solite_pos.data.schema.response


data class SynchronizeParams(
    val customer: List<CustomerResponse>,
    val store: List<StoreResponse>,
    val category: List<CategoryResponse>,
    val promo: List<PromoResponse>,
    val payment: List<PaymentResponse>,
    val order: List<OrderResponse>,
    val outcome: List<OutcomeResponse>,
    val product: List<ProductResponse>,
    val variant: List<VariantResponse>,
    val orderDetail: SynchronizeParamItem<OrderDetailResponse>,
    val orderPayment: List<OrderPaymentResponse>,
    val orderPromo: List<OrderPromoResponse>,
    val variantOption: List<VariantOptionResponse>,
    val orderProductVariant: SynchronizeParamItem<OrderProductVariantResponse>,
    val variantProduct: SynchronizeParamItem<VariantProductResponse>
)
