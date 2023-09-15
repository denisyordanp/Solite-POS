package com.socialite.domain.helper

import com.socialite.domain.schema.Outcome
import com.socialite.domain.schema.main.Category
import com.socialite.domain.schema.main.Customer
import com.socialite.domain.schema.main.Order
import com.socialite.domain.schema.main.OrderPayment
import com.socialite.domain.schema.main.OrderPromo
import com.socialite.domain.schema.main.Payment
import com.socialite.domain.schema.main.Product
import com.socialite.domain.schema.main.Promo
import com.socialite.domain.schema.main.Store
import com.socialite.domain.schema.main.User
import com.socialite.domain.schema.main.Variant
import com.socialite.domain.schema.main.VariantOption
import com.socialite.domain.schema.main.VariantProduct

fun Order.toData() = com.socialite.data.schema.room.new_master.Order(
    id = id,
    orderNo = orderNo,
    customer = customer,
    orderTime = orderTime,
    isTakeAway = isTakeAway,
    status = status,
    store = store,
    isUploaded = isUploaded
)

fun Outcome.toData() = com.socialite.data.schema.room.new_master.Outcome(
    id = id,
    name = name,
    desc = desc,
    price = price,
    amount = amount,
    date = date,
    store = store,
    isUploaded = isUploaded
)

fun Customer.toData() = com.socialite.data.schema.room.new_master.Customer(
    id = id,
    name = name,
    isUploaded = isUploaded
)

fun Category.toData() = com.socialite.data.schema.room.new_master.Category(
    id = id,
    name = name,
    desc = desc,
    isActive = isActive,
    isUploaded = isUploaded
)

fun OrderPromo.toData() = com.socialite.data.schema.room.new_bridge.OrderPromo(
    id = id,
    order = order,
    promo = promo,
    totalPromo = totalPromo,
    isUpload = isUpload
)

fun OrderPayment.toData() = com.socialite.data.schema.room.new_bridge.OrderPayment(
    id = id,
    order = order,
    payment = payment,
    pay = pay,
    isUpload = isUpload
)

fun Payment.toData() = com.socialite.data.schema.room.new_master.Payment(
    id = id,
    name = name,
    desc = desc,
    tax = tax,
    isCash = isCash,
    isActive = isActive,
    isUploaded = isUploaded
)

fun Product.toData() = com.socialite.data.schema.room.new_master.Product(
    id = id,
    name = name,
    category = category,
    image = image,
    desc = desc,
    price = price,
    isActive = isActive,
    isUploaded = isUploaded
)

fun Promo.toData() = com.socialite.data.schema.room.new_master.Promo(
    id = id,
    name = name,
    desc = desc,
    isCash = isCash,
    value = value,
    isActive = isActive,
    isUploaded = isUploaded
)

fun Store.toData() = com.socialite.data.schema.room.new_master.Store(
    id = id,
    name = name,
    address = address,
    isUploaded = isUploaded
)

fun Variant.toData() = com.socialite.data.schema.room.new_master.Variant(
    id = id,
    name = name,
    type = type,
    isMust = isMust,
    isUploaded = isUploaded
)

fun VariantOption.toData() = com.socialite.data.schema.room.new_master.VariantOption(
    id = id,
    variant = variant,
    name = name,
    desc = desc,
    isActive = isActive,
    isUploaded = isUploaded
)

fun VariantProduct.toData() = com.socialite.data.schema.room.new_bridge.VariantProduct(
    id = id,
    variant = variant,
    variantOption = variantOption,
    product = product,
    isUploaded = isUploaded,
    isDeleted = isDeleted
)

fun User.toNewUserData(newId: String) = com.socialite.data.schema.room.master.User(
    id = newId,
    name = name,
    email = email,
    authority = authority.name
)

fun User.toData() = com.socialite.data.schema.room.master.User(
    id = id,
    name = name,
    email = email,
    authority = authority.name
)