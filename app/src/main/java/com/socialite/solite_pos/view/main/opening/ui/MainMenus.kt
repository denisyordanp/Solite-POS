package com.socialite.solite_pos.view.main.opening.ui

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
    DONE(R.string.done, 2),
    CANCELED(R.string.canceled, 3),;

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
    DAILY_RECAP(R.string.daily_recap),
    PURCHASE(R.string.purchase),
    HISTORY(R.string.history),
    MASTERS(R.string.masters),
}

enum class MasterMenus(@StringRes val title: Int) {
    PRODUCT(R.string.product),
    CATEGORY(R.string.category),
    VARIANT(R.string.variant),
    PAYMENT(R.string.payment),
    SUPPLIER(R.string.supplier)
}
