package com.socialite.common.utility.helper

import java.util.UUID

class IdManager {
    fun generateNewId() = UUID.randomUUID().toString()
}