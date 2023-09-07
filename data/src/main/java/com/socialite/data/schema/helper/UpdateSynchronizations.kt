package com.socialite.data.schema.helper

import com.socialite.data.repository.SyncRepository
import com.socialite.data.schema.room.EntityData

open class UpdateSynchronizations(
    private val repository: SyncRepository<EntityData>
) {
    suspend fun updates(missingItems: List<EntityData>?) {
        if (!missingItems.isNullOrEmpty()) {
            val existIds = repository.getItems().map {
                it.id
            }
            val itemsUpdate = missingItems.filter {
                it.id in existIds
            }
            val itemsInsert = missingItems.filterNot {
                it.id in existIds
            }
            if (itemsUpdate.isNotEmpty()) {
                repository.updateItems(itemsUpdate)
            }
            if (itemsInsert.isNotEmpty()) {
                repository.insertItems(itemsInsert)
            }
        }
    }
}
