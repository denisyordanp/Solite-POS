package com.socialite.domain.domain

import com.socialite.domain.schema.ReportParameter
import com.socialite.domain.schema.RecapData
import kotlinx.coroutines.flow.Flow

fun interface GetRecapData {
    operator fun invoke(parameters: ReportParameter): Flow<RecapData>
}
