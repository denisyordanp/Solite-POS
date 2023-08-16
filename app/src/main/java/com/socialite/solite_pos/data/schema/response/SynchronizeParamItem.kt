package com.socialite.solite_pos.data.schema.response

import com.socialite.data.schema.response.SynchronizeParamItem as DataSyncItem

data class SynchronizeParamItem<T>(
    val deletedItems: List<String>,
    val items: List<T>
) {

    fun <E> toDataResponse(mapper: (T) -> E): DataSyncItem<E> {
        val maps = items.map { mapper(it) }
        return DataSyncItem(
            deletedItems, maps
        )
    }
}
