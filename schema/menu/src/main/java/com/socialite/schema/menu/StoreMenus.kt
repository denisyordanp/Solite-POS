package com.socialite.schema.menu

import androidx.annotation.StringRes

enum class StoreMenus(@StringRes val title: Int) {
    SALES_RECAP(R.string.sales_recap_menu_title),
    OUTCOMES(R.string.outcome_menu_title),
    PAYMENT(R.string.payment_menu_title),
    PROMO(R.string.promo_menu_title),
    MASTERS(R.string.products_menu_title),
    STORE(R.string.stores_menu_title),
    STORE_USER(R.string.store_users_menu_title)
}