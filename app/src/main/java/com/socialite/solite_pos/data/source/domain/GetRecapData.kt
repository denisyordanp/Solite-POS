package com.socialite.solite_pos.data.source.domain

import com.socialite.solite_pos.data.source.local.entity.helper.RecapData
import kotlinx.coroutines.flow.Flow

interface GetRecapData {
    operator fun invoke(from: String, until: String, store: Long): Flow<RecapData>
}
