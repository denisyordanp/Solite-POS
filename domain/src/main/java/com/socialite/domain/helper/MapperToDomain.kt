package com.socialite.domain.helper

import com.socialite.data.schema.response.UserStoreResponse
import com.socialite.data.schema.room.helper.OrderData
import com.socialite.data.schema.room.helper.ProductWithCategory
import com.socialite.data.schema.room.new_bridge.OrderPayment
import com.socialite.data.schema.room.new_bridge.OrderPromo
import com.socialite.data.schema.room.new_bridge.VariantProduct
import com.socialite.domain.schema.OrderData as DomainOrderData
import com.socialite.data.schema.room.new_master.Category
import com.socialite.data.schema.room.new_master.Customer
import com.socialite.data.schema.room.new_master.Order
import com.socialite.data.schema.room.new_master.Outcome
import com.socialite.data.schema.room.new_master.Payment
import com.socialite.domain.schema.main.Category as DomainCategory
import com.socialite.data.schema.room.new_master.Product
import com.socialite.data.schema.room.new_master.Promo
import com.socialite.data.schema.room.new_master.Store
import com.socialite.data.schema.room.new_master.Variant
import com.socialite.data.schema.room.new_master.VariantOption
import com.socialite.domain.menu.toAuthority
import com.socialite.domain.schema.main.VariantOption as DomainVariantOption
import com.socialite.domain.schema.main.Variant as DomainVariant
import com.socialite.domain.schema.main.Product as DomainProduct

fun Product.toDomain() = DomainProduct(
    id = id,
    name = name,
    category = category,
    image = image,
    desc = desc,
    price = price,
    isActive = isActive,
    isUploaded = isUploaded
)

fun Category.toDomain() = DomainCategory(
    id = id,
    name = name,
    desc = desc,
    isActive = isActive,
    isUploaded = isUploaded
)

fun Variant.toDomain() = DomainVariant(
    id = id,
    name = name,
    type = type,
    isMust = isMust,
    isUploaded = isUploaded
)

fun VariantOption.toDomain() = DomainVariantOption(
    id = id,
    variant = variant,
    name = name,
    desc = desc,
    isActive = isActive,
    isUploaded = isUploaded
)

fun Order.toDomain() = com.socialite.domain.schema.main.Order(
    id = id,
    orderNo = orderNo,
    customer = customer,
    orderTime = orderTime,
    isTakeAway = isTakeAway,
    status = status,
    store = store,
    isUploaded = isUploaded
)

fun Store.toDomain() = com.socialite.domain.schema.main.Store(
    id = id,
    name = name,
    address = address,
    isUploaded = isUploaded
)

fun Customer.toDomain() = com.socialite.domain.schema.main.Customer(
    id = id,
    name = name,
    isUploaded = isUploaded
)

fun OrderPayment.toDomain() = com.socialite.domain.schema.main.OrderPayment(
    id = id,
    order = order,
    payment = payment,
    pay = pay,
    isUpload = isUpload
)

fun Payment.toDomain() = com.socialite.domain.schema.main.Payment(
    id = id,
    name = name,
    desc = desc,
    tax = tax,
    isCash = isCash,
    isActive = isActive,
    isUploaded = isUploaded
)

fun OrderPromo.toDomain() = com.socialite.domain.schema.main.OrderPromo(
    id = id,
    order = order,
    promo = promo,
    totalPromo = totalPromo,
    isUpload = isUpload
)

fun Promo.toDomain() = com.socialite.domain.schema.main.Promo(
    id = id,
    name = name,
    desc = desc,
    isCash = isCash,
    value = value,
    isActive = isActive,
    isUploaded = isUploaded
)

fun OrderData.toDomain() = DomainOrderData(
    order = order.toDomain(),
    store = store.toDomain(),
    customer = customer.toDomain(),
    orderPayment = orderPayment?.toDomain(),
    payment = payment?.toDomain(),
    orderPromo = orderPromo?.toDomain(),
    promo = promo?.toDomain()
)

fun ProductWithCategory.toDomain() = com.socialite.domain.schema.ProductWithCategory(
    product = product.toDomain(),
    category = category.toDomain(),
    hasVariant = hasVariant
)

fun Outcome.toDomain() = com.socialite.domain.schema.Outcome(
    id = id,
    name = name,
    desc = desc,
    price = price,
    amount = amount,
    date = date,
    store = store,
    isUploaded = isUploaded
)

fun VariantProduct.toDomain() = com.socialite.domain.schema.main.VariantProduct(
    id = id,
    variant = variant,
    variantOption = variantOption,
    product = product,
    isUploaded = isUploaded,
    isDeleted = isDeleted
)

fun UserStoreResponse.toDomain() = com.socialite.domain.schema.main.User(
    id = id.toString(),
    name = user.name,
    email = user.email,
    authority = user.authority.toAuthority(),
    isUserActive = isActive,
)