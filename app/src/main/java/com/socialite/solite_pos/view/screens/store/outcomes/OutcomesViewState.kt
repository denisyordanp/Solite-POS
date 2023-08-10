package com.socialite.solite_pos.view.screens.store.outcomes

import com.socialite.solite_pos.data.source.local.entity.room.new_master.Outcome
import com.socialite.solite_pos.utils.tools.helper.ReportParameter

data class OutcomesViewState(
    val parameters: ReportParameter,
    val outcomes: List<Outcome>
) {
    companion object {
        fun idle() = OutcomesViewState(
            parameters = ReportParameter.createTodayOnly(true),
            outcomes = emptyList()
        )
    }
}