package com.socialite.domain.domain

import com.socialite.schema.ui.helper.Outcome
import com.socialite.schema.ui.helper.ReportParameter
import kotlinx.coroutines.flow.Flow

fun interface GetOutcomes {
    operator fun invoke(parameters: ReportParameter): Flow<List<Outcome>>
}