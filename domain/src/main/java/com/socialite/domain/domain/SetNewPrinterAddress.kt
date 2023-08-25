package com.socialite.domain.domain

fun interface SetNewPrinterAddress {
    operator fun invoke(address: String)
}