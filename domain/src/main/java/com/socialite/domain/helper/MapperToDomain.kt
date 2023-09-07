package com.socialite.domain.helper

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
import com.socialite.domain.schema.main.VariantOption as DomainVariantOption
import com.socialite.domain.schema.main.Variant as DomainVariant
import com.socialite.domain.schema.main.Product as DomainProduct

fun Product.toDomain() = DomainProduct(
    this.id,
    this.name,
    this.category,
    this.image,
    this.desc,
    this.price,
    this.isActive,
    this.isUploaded
)

fun Category.toDomain() = DomainCategory(
    this.id,
    this.name,
    this.desc,
    this.isActive,
    this.isUploaded
)

fun Variant.toDomain() = DomainVariant(
    this.id,
    this.name,
    this.type,
    this.isMust,
    this.isUploaded
)

fun VariantOption.toDomain() = DomainVariantOption(
    this.id,
    this.variant,
    this.name,
    this.desc,
    this.isActive,
    this.isUploaded
)

fun Order.toDomain() = com.socialite.domain.schema.main.Order(
    this.id,
    this.orderNo,
    this.customer,
    this.orderTime,
    this.isTakeAway,
    this.status,
    this.store,
    this.isUploaded
)

fun Store.toDomain() = com.socialite.domain.schema.main.Store(
    this.id,
    this.name,
    this.address,
    this.isUploaded
)

fun Customer.toDomain() = com.socialite.domain.schema.main.Customer(
    this.id,
    this.name,
    this.isUploaded
)

fun OrderPayment.toDomain() = com.socialite.domain.schema.main.OrderPayment(
    this.id,
    this.order,
    this.payment,
    this.pay,
    this.isUpload
)

fun Payment.toDomain() = com.socialite.domain.schema.main.Payment(
    this.id,
    this.name,
    this.desc,
    this.tax,
    this.isCash,
    this.isActive,
    this.isUploaded
)

fun OrderPromo.toDomain() = com.socialite.domain.schema.main.OrderPromo(
    this.id,
    this.order,
    this.promo,
    this.totalPromo,
    this.isUpload
)

fun Promo.toDomain() = com.socialite.domain.schema.main.Promo(
    this.id,
    this.name,
    this.desc,
    this.isCash,
    this.value,
    this.isActive,
    this.isUploaded
)

fun OrderData.toDomain() = DomainOrderData(
    this.order.toDomain(),
    this.store.toDomain(),
    this.customer.toDomain(),
    this.orderPayment?.toDomain(),
    this.payment?.toDomain(),
    this.orderPromo?.toDomain(),
    this.promo?.toDomain()
)

fun ProductWithCategory.toDomain() = com.socialite.domain.schema.ProductWithCategory(
    this.product.toDomain(),
    this.category.toDomain(),
    this.hasVariant
)

fun Outcome.toDomain() = com.socialite.domain.schema.Outcome(
    this.id,
    this.name,
    this.desc,
    this.price,
    this.amount,
    this.date,
    this.store,
    this.isUploaded
)

fun VariantProduct.toDomain() = com.socialite.domain.schema.main.VariantProduct(
    this.id,
    this.variant,
    this.variantOption,
    this.product,
    this.isUploaded,
    this.isDeleted
)