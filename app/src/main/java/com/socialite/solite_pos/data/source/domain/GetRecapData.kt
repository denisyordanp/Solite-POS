package com.socialite.solite_pos.data.source.domain

import com.socialite.solite_pos.data.source.local.entity.helper.RecapData
import kotlinx.coroutines.flow.Flow

interface GetRecapData {
    operator fun invoke(status: Int, date: String): Flow<RecapData>
}
