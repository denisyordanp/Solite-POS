package com.socialite.data.schema.response

data class SynchronizeResponse(
    val customer: List<CustomerResponse>,
    val store: List<StoreResponse>,
    val category: List<CategoryResponse>,
    val promo: List<PromoResponse>,
    val payment: List<PaymentResponse>,
    val order: List<OrderResponse>,
    val outcome: List<OutcomeResponse>,
    val product: List<ProductResponse>,
    val variant: List<VariantResponse>,
    val orderDetail: List<OrderDetailResponse>,
    val orderPayment: List<OrderPaymentResponse>,
    val orderPromo: List<OrderPromoResponse>,
    val variantOption: List<VariantOptionResponse>,
    val orderProductVariant: List<OrderProductVariantResponse>,
    val variantProduct: List<VariantProductResponse>,
    val user: List<UserStoreResponse>
)
