package com.socialite.solite_pos.data.source.domain

import com.socialite.solite_pos.data.source.local.entity.helper.RecapData
import kotlinx.coroutines.flow.Flow

interface GetIncomesRecapData {
    suspend operator fun invoke(status: Int, date: String): Flow<List<RecapData>>
}
