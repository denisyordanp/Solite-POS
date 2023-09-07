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
import com.socialite.domain.schema.main.Variant
import com.socialite.domain.schema.main.VariantOption
import com.socialite.domain.schema.main.VariantProduct

fun Order.toData() = com.socialite.data.schema.room.new_master.Order(
    this.id,
    this.orderNo,
    this.customer,
    this.orderTime,
    this.isTakeAway,
    this.status,
    this.store,
    this.isUploaded
)

fun Outcome.toData() = com.socialite.data.schema.room.new_master.Outcome(
    this.id,
    this.name,
    this.desc,
    this.price,
    this.amount,
    this.date,
    this.store,
    this.isUploaded
)

fun Customer.toData() = com.socialite.data.schema.room.new_master.Customer(
    this.id,
    this.name,
    this.isUploaded
)

fun Category.toData() = com.socialite.data.schema.room.new_master.Category(
    this.id,
    this.name,
    this.desc,
    this.isActive,
    this.isUploaded
)

fun OrderPromo.toData() = com.socialite.data.schema.room.new_bridge.OrderPromo(
    this.id,
    this.order,
    this.promo,
    this.totalPromo,
    this.isUpload
)

fun OrderPayment.toData() = com.socialite.data.schema.room.new_bridge.OrderPayment(
    this.id,
    this.order,
    this.payment,
    this.pay,
    this.isUpload
)

fun Payment.toData() = com.socialite.data.schema.room.new_master.Payment(
    this.id,
    this.name,
    this.desc,
    this.tax,
    this.isCash,
    this.isActive,
    this.isUploaded
)

fun Product.toData() = com.socialite.data.schema.room.new_master.Product(
    this.id,
    this.name,
    this.category,
    this.image,
    this.desc,
    this.price,
    this.isActive,
    this.isUploaded
)

fun Promo.toData() = com.socialite.data.schema.room.new_master.Promo(
    this.id,
    this.name,
    this.desc,
    this.isCash,
    this.value,
    this.isActive,
    this.isUploaded
)

fun Store.toData() = com.socialite.data.schema.room.new_master.Store(
    this.id,
    this.name,
    this.address,
    this.isUploaded
)

fun Variant.toData() = com.socialite.data.schema.room.new_master.Variant(
    this.id,
    this.name,
    this.type,
    this.isMust,
    this.isUploaded
)

fun VariantOption.toData() = com.socialite.data.schema.room.new_master.VariantOption(
    this.id,
    this.variant,
    this.name,
    this.desc,
    this.isActive,
    this.isUploaded
)

fun VariantProduct.toData() = com.socialite.data.schema.room.new_bridge.VariantProduct(
    this.id,
    this.variant,
    this.variantOption,
    this.product,
    this.isUploaded,
    this.isDeleted
)