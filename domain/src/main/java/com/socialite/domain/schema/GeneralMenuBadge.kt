package com.socialite.domain.schema

import com.socialite.domain.menu.GeneralMenus

data class GeneralMenuBadge(
    val menu: GeneralMenus,
    val badge: Int?
)
