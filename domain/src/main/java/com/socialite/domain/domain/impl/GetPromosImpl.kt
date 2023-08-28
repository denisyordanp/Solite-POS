package com.socialite.domain.domain.impl

import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.data.repository.PromosRepository
import com.socialite.domain.domain.GetPromos
import javax.inject.Inject

class GetPromosImpl @Inject constructor(
    private val promosRepository: PromosRepository
) : GetPromos {
    override fun invoke(query: SupportSQLiteQuery) = promosRepository.getPromos(query)
}