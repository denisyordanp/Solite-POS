package com.socialite.domain.helper

import com.socialite.schema.database.helper.OrderData
import com.socialite.schema.database.helper.ProductWithCategory
import com.socialite.schema.database.master.User
import com.socialite.schema.database.new_bridge.OrderPayment
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
import com.socialite.schema.ui.utility.toAuthority
import com.socialite.schema.ui.helper.OrderData as DomainOrderData
import com.socialite.schema.ui.main.Category as DomainCategory
import com.socialite.schema.ui.main.Product as DomainProduct
import com.socialite.schema.ui.main.Variant as DomainVariant
import com.socialite.schema.ui.main.VariantOption as DomainVariantOption

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

fun Order.toDomain() = com.socialite.schema.ui.main.Order(
    id = id,
    orderNo = orderNo,
    customer = customer,
    orderTime = orderTime,
    isTakeAway = isTakeAway,
    status = status,
    store = store,
    isUploaded = isUploaded,
    user = user
)

fun Store.toDomain() = com.socialite.schema.ui.main.Store(
    id = id,
    name = name,
    address = address,
    isUploaded = isUploaded
)

fun Customer.toDomain() = com.socialite.schema.ui.main.Customer(
    id = id,
    name = name,
    isUploaded = isUploaded
)

fun OrderPayment.toDomain() = com.socialite.schema.ui.main.OrderPayment(
    id = id,
    order = order,
    payment = payment,
    pay = pay,
    isUpload = isUpload
)

fun Payment.toDomain() = com.socialite.schema.ui.main.Payment(
    id = id,
    name = name,
    desc = desc,
    tax = tax,
    isCash = isCash,
    isActive = isActive,
    isUploaded = isUploaded
)

fun OrderPromo.toDomain() = com.socialite.schema.ui.main.OrderPromo(
    id = id,
    order = order,
    promo = promo,
    totalPromo = totalPromo,
    isUpload = isUpload
)

fun Promo.toDomain() = com.socialite.schema.ui.main.Promo(
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

fun ProductWithCategory.toDomain() = com.socialite.schema.ui.helper.ProductWithCategory(
    product = product.toDomain(),
    category = category.toDomain(),
    hasVariant = hasVariant
)

fun Outcome.toDomain() = com.socialite.schema.ui.helper.Outcome(
    id = id,
    name = name,
    desc = desc,
    price = price,
    amount = amount,
    date = date,
    store = store,
    isUploaded = isUploaded,
    user = user
)

fun VariantProduct.toDomain() = com.socialite.schema.ui.main.VariantProduct(
    id = id,
    variant = variant,
    variantOption = variantOption,
    product = product,
    isUploaded = isUploaded,
    isDeleted = isDeleted
)

fun User.toDomain() = com.socialite.schema.ui.main.User(
    id = id,
    name = name,
    email = email,
    authority = authority.toAuthority(),
    isUserActive = active,
)