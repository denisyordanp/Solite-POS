package com.socialite.domain.helper

import com.socialite.schema.ui.helper.Outcome
import com.socialite.schema.ui.main.Category
import com.socialite.schema.ui.main.Customer
import com.socialite.schema.ui.main.Order
import com.socialite.schema.ui.main.OrderPayment
import com.socialite.schema.ui.main.OrderPromo
import com.socialite.schema.ui.main.Payment
import com.socialite.schema.ui.main.Product
import com.socialite.schema.ui.main.Promo
import com.socialite.schema.ui.main.Store
import com.socialite.schema.ui.main.User
import com.socialite.schema.ui.main.Variant
import com.socialite.schema.ui.main.VariantOption
import com.socialite.schema.ui.main.VariantProduct

fun Order.toData() = com.socialite.schema.database.new_master.Order(
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

fun Outcome.toData() = com.socialite.schema.database.new_master.Outcome(
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

fun Customer.toData() = com.socialite.schema.database.new_master.Customer(
    id = id,
    name = name,
    isUploaded = isUploaded
)

fun Category.toData() = com.socialite.schema.database.new_master.Category(
    id = id,
    name = name,
    desc = desc,
    isActive = isActive,
    isUploaded = isUploaded
)

fun OrderPromo.toData() = com.socialite.schema.database.new_bridge.OrderPromo(
    id = id,
    order = order,
    promo = promo,
    totalPromo = totalPromo,
    isUpload = isUpload
)

fun OrderPayment.toData() = com.socialite.schema.database.new_bridge.OrderPayment(
    id = id,
    order = order,
    payment = payment,
    pay = pay,
    isUpload = isUpload
)

fun Payment.toData() = com.socialite.schema.database.new_master.Payment(
    id = id,
    name = name,
    desc = desc,
    tax = tax,
    isCash = isCash,
    isActive = isActive,
    isUploaded = isUploaded
)

fun Product.toData() = com.socialite.schema.database.new_master.Product(
    id = id,
    name = name,
    category = category,
    image = image,
    desc = desc,
    price = price,
    isActive = isActive,
    isUploaded = isUploaded
)

fun Promo.toData() = com.socialite.schema.database.new_master.Promo(
    id = id,
    name = name,
    desc = desc,
    isCash = isCash,
    value = value,
    isActive = isActive,
    isUploaded = isUploaded
)

fun Store.toData() = com.socialite.schema.database.new_master.Store(
    id = id,
    name = name,
    address = address,
    isUploaded = isUploaded
)

fun Variant.toData() = com.socialite.schema.database.new_master.Variant(
    id = id,
    name = name,
    type = type,
    isMust = isMust,
    isUploaded = isUploaded
)

fun VariantOption.toData() = com.socialite.schema.database.new_master.VariantOption(
    id = id,
    variant = variant,
    name = name,
    desc = desc,
    isActive = isActive,
    isUploaded = isUploaded
)

fun VariantProduct.toData() = com.socialite.schema.database.new_bridge.VariantProduct(
    id = id,
    variant = variant,
    variantOption = variantOption,
    product = product,
    isUploaded = isUploaded,
    isDeleted = isDeleted
)

fun User.toData() = com.socialite.schema.database.master.User(
    id = id,
    name = name,
    email = email,
    authority = authority.name,
    active = isUserActive
)