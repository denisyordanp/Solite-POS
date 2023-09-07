package com.socialite.domain.domain

import com.socialite.domain.schema.Outcome
import com.socialite.domain.schema.ReportParameter
import kotlinx.coroutines.flow.Flow

fun interface GetOutcomes {
    operator fun invoke(parameters: ReportParameter): Flow<List<Outcome>>
}