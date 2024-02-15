package com.socialite.schema.menu

enum class OrderMenus(
    val status: Int,
) {
    CURRENT_ORDER(0),
    NOT_PAY_YET(1),
    CANCELED(2),
    DONE(3),
}