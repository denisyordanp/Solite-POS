package com.socialite.domain.domain

import com.socialite.schema.ui.helper.ReportParameter
import com.socialite.schema.ui.helper.RecapData
import kotlinx.coroutines.flow.Flow

fun interface GetRecapData {
    operator fun invoke(parameters: ReportParameter): Flow<RecapData>
}
