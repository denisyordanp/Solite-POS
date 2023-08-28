package com.socialite.domain.domain

import com.socialite.data.schema.helper.ReportParameter
import com.socialite.data.schema.room.new_master.Outcome
import kotlinx.coroutines.flow.Flow

fun interface GetOutcomes {
    operator fun invoke(parameters: ReportParameter): Flow<List<Outcome>>
}