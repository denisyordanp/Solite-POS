package com.socialite.domain.domain.impl

import com.socialite.data.repository.OutcomesRepository
import com.socialite.domain.domain.GetOutcomes
import com.socialite.domain.helper.toData
import com.socialite.domain.helper.toDomain
import com.socialite.domain.schema.ReportParameter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetOutcomesImpl @Inject constructor(
    private val outcomesRepository: OutcomesRepository,
) : GetOutcomes {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(parameters: ReportParameter) =
        outcomesRepository.getOutcomes(parameters.toData())
            .mapLatest { outcomes -> outcomes.map { it.toDomain() } }
}