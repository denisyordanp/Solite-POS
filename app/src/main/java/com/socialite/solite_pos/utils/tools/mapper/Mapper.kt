package com.socialite.solite_pos.utils.tools.mapper

import com.socialite.domain.schema.main.Category
import com.socialite.domain.schema.main.Payment
import com.socialite.domain.schema.main.Promo
import com.socialite.domain.schema.main.Store
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import com.socialite.solite_pos.schema.Category as UiCategory

fun Category.toUi() = UiCategory(
    this.id,
    this.name,
    this.desc,
    this.isActive,
    this.isUploaded
)

fun ReportParameter.toDomain() = com.socialite.domain.schema.ReportParameter(
    start, end, storeId
)

fun Store.toUi() = com.socialite.solite_pos.schema.Store(
    this.id,
    this.name,
    this.address,
    this.isUploaded
)

fun Promo.toUi() = com.socialite.solite_pos.schema.Promo(
    this.id,
    this.name,
    this.desc,
    this.isCash,
    this.value,
    this.isActive,
    this.isUploaded
)

fun Payment.toUi() = com.socialite.solite_pos.schema.Payment(
    this.id,
    this.name,
    this.desc,
    this.tax,
    this.isCash,
    this.isActive,
    this.isUploaded
)