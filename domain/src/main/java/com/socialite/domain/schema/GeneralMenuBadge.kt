package com.socialite.domain.schema

import com.socialite.schema.menu.GeneralMenus

data class GeneralMenuBadge(
    val menu: GeneralMenus,
    val badge: Int?
)
