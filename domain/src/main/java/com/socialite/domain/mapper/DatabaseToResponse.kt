package com.socialite.domain.mapper

import com.socialite.data.schema.response.CategoryResponse
import com.socialite.data.schema.response.CustomerResponse
import com.socialite.data.schema.response.OrderDetailResponse
import com.socialite.data.schema.response.OrderPaymentResponse
import com.socialite.data.schema.response.OrderProductVariantResponse
import com.socialite.data.schema.response.OrderPromoResponse
import com.socialite.data.schema.response.OrderResponse
import com.socialite.data.schema.response.OutcomeResponse
import com.socialite.data.schema.response.PaymentResponse
import com.socialite.data.schema.response.ProductResponse
import com.socialite.data.schema.response.PromoResponse
import com.socialite.data.schema.response.StoreResponse
import com.socialite.data.schema.response.VariantOptionResponse
import com.socialite.data.schema.response.VariantProductResponse
import com.socialite.data.schema.response.VariantResponse
import com.socialite.schema.database.new_bridge.OrderDetail
import com.socialite.schema.database.new_bridge.OrderPayment
import com.socialite.schema.database.new_bridge.OrderProductVariant
import com.socialite.schema.database.new_bridge.OrderPromo
import com.socialite.schema.database.new_bridge.VariantProduct
import com.socialite.schema.database.new_master.Category
import com.socialite.schema.database.new_master.Customer
import com.socialite.schema.database.new_master.Order
import com.socialite.schema.database.new_master.Outcome
import com.socialite.schema.database.new_master.Payment
import com.socialite.schema.database.new_master.Product
import com.socialite.schema.database.new_master.Promo
import com.socialite.schema.database.new_master.Store
import com.socialite.schema.database.new_master.Variant
import com.socialite.schema.database.new_master.VariantOption

fun Customer.toCustomerResponse() = CustomerResponse(
    id = id,
    name = name,
    isUploaded = true
)

fun Store.toStoreResponse() = StoreResponse(
    id = id,
    name = name,
    address = address,
    isUploaded = true
)

fun Category.toCategoryResponse() = CategoryResponse(
    id = id,
    name = name,
    desc = desc,
    isActive = isActive,
    isUploaded = true
)

fun Promo.toPromoResponse() = PromoResponse(
    id = id,
    name = name,
    desc = desc,
    value = value,
    isCash = isCash,
    isActive = isActive,
    isUploaded = true
)

fun Payment.toPaymentResponse() = PaymentResponse(
    id = id,
    name = name,
    desc = desc,
    tax = tax,
    isCash = isCash,
    isActive = isActive,
    isUploaded = true
)

fun Order.toOrderResponse() = OrderResponse(
    id = id,
    orderNo = orderNo,
    customer = customer,
    orderTime = orderTime,
    status = status,
    store = store,
    isTakeAway = isTakeAway,
    isUploaded = true,
    user = user
)

fun Outcome.toOutcomeResponse() = OutcomeResponse(
    id = id,
    name = name,
    desc = desc,
    date = date,
    amount = amount,
    price = price.toInt(),
    store = store,
    isUploaded = true,
    user = user
)

fun Product.toProductResponse() = ProductResponse(
    id = id,
    name = name,
    desc = desc,
    category = category,
    image = image,
    price = price.toInt(),
    isActive = isActive,
    isUploaded = true
)

fun Variant.toVariantResponse() = VariantResponse(
    id = id,
    name = name,
    type = type,
    isMust = isMust ?: false,
    isUploaded = true
)

fun OrderDetail.toOrderDetailResponse() = OrderDetailResponse(
    id = id,
    amount = amount,
    order = order,
    product = product,
    isUploaded = true
)

fun OrderPayment.toOrderPaymentResponse() = OrderPaymentResponse(
    id = id,
    pay = pay.toInt(),
    order = order,
    payment = payment,
    isUploaded = true
)

fun OrderPromo.toOrderPromoResponse() = OrderPromoResponse(
    id = id,
    order = order,
    promo = promo,
    totalPromo = totalPromo.toInt(),
    isUploaded = true
)

fun VariantOption.toVariantOptionResponse() = VariantOptionResponse(
    id = id,
    name = name,
    desc = desc,
    variant = variant,
    isActive = isActive,
    isUploaded = true
)

fun OrderProductVariant.toOrderProductVariantResponse() = OrderProductVariantResponse(
    id = id,
    orderDetail = orderDetail,
    variantOption = variantOption,
    isUploaded = true
)

fun VariantProduct.toVariantProductResponse() = VariantProductResponse(
    id = id,
    product = product,
    variantOption = variantOption,
    variant = variant,
    isUploaded = true
)