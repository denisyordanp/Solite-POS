package com.socialite.domain.helper

import java.util.UUID

class IdManager {
    fun generateNewId() = UUID.randomUUID().toString()
}