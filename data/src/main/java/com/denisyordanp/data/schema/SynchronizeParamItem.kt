package com.denisyordanp.data.schema

data class SynchronizeParamItem<T>(
    val deletedItems: List<String>,
    val items: List<T>
)
