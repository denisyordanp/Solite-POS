package com.socialite.domain.domain.impl

import com.socialite.data.repository.OutcomesRepository
import com.socialite.data.schema.helper.ReportParameter
import com.socialite.domain.domain.GetOutcomes
import javax.inject.Inject

class GetOutcomesImpl @Inject constructor(
    private val outcomesRepository: OutcomesRepository,
) : GetOutcomes {
    override fun invoke(parameters: ReportParameter) = outcomesRepository.getOutcomes(parameters)
}