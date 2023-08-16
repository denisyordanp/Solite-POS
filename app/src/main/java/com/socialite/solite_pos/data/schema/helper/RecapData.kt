package com.socialite.solite_pos.data.schema.helper

import com.socialite.data.schema.room.new_master.Outcome

data class RecapData(
    val incomes: List<Income>,
    val outcomes: List<Outcome>,
) {

    val totalCash = incomes
        .filter {
            it.isCash
        }.sumOf {
            it.total
        }

    val totalNonCash = incomes
        .filter {
            !it.isCash
        }.sumOf {
            it.total
        }

    val totalOutcomes = outcomes
        .sumOf {
            it.total
        }

    val totalIncomes = incomes
        .sumOf {
            it.total
        }

    val grossIncome = totalIncomes - totalOutcomes
    companion object {
        fun empty() = RecapData(
            incomes = emptyList(),
            outcomes = emptyList()
        )
    }
}
