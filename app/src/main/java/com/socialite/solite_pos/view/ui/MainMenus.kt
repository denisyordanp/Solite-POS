package com.socialite.solite_pos.view.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.socialite.solite_pos.R

enum class MainMenus {
    ORDER, NOT_PAY, DONE, CANCELED, DAILY_RECAP, PURCHASE, HISTORY, MASTER, SETTING;
}

enum class OrderMenus(
    @StringRes val title: Int,
    val status: Int,
) {
    CURRENT_ORDER(R.string.orders, 0),
    NOT_PAY_YET(R.string.not_pay_yet, 1),
    CANCELED(R.string.canceled, 2),
    DONE(R.string.done, 3),
}

enum class GeneralMenus(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
) {
    NEW_ORDER(R.string.new_order, R.drawable.ic_masters),
    ORDERS(R.string.orders, R.drawable.ic_cook),
    STORE(R.string.store, R.drawable.ic_store),
    SETTING(R.string.setting, R.drawable.ic_settings),
}

enum class StoreMenus(@StringRes val title: Int) {
    SALES_RECAP(R.string.sales_recap),
    OUTCOMES(R.string.outcomes),
//    PURCHASE(R.string.purchase),
    PAYMENT(R.string.payment),
    MASTERS(R.string.products),
    STORE(R.string.stores),
}

enum class MasterMenus(@StringRes val title: Int) {
    PRODUCT(R.string.product),
    CATEGORY(R.string.category),
    VARIANT(R.string.variant),
//    SUPPLIER(R.string.supplier)
}