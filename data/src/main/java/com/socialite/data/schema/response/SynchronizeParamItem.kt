package com.socialite.data.schema.response

data class SynchronizeParamItem<T>(
    val deletedItems: List<String>,
    val items: List<T>
)
