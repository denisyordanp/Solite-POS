package com.socialite.solite_pos.data.source.domain

import com.socialite.solite_pos.data.source.local.entity.helper.RecapData
import com.socialite.solite_pos.utils.tools.helper.ReportsParameter
import kotlinx.coroutines.flow.Flow

interface GetRecapData {
    operator fun invoke(parameters: ReportsParameter): Flow<RecapData>
}
