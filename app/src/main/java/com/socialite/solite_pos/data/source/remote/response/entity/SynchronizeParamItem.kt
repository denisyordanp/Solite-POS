package com.socialite.solite_pos.data.source.remote.response.entity

data class SynchronizeParamItem<T>(
    val deletedItems: List<String>,
    val items: List<T>
)
