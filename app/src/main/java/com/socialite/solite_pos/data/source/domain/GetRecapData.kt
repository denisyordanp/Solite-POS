package com.socialite.solite_pos.data.source.domain

import com.socialite.solite_pos.data.source.local.entity.helper.RecapData
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import kotlinx.coroutines.flow.Flow

fun interface GetRecapData {
    operator fun invoke(parameters: ReportParameter): Flow<RecapData>
}
