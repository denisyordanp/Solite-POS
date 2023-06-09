package com.socialite.solite_pos.utils.tools

import com.socialite.solite_pos.data.source.local.entity.helper.EntityData
import com.socialite.solite_pos.data.source.repository.SyncRepository

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