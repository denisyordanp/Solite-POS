package com.socialite.solite_pos.utils.config

object CashAmounts {

    private const val BANK_NOTES = 5000L
    private const val MAX_AMOUNT = 100000L

    fun generateCash(): List<Long> {
        var currentAmount = BANK_NOTES
        val lists = mutableListOf<Long>()
        do {
            lists.add(currentAmount)
            currentAmount += BANK_NOTES
        } while (currentAmount < MAX_AMOUNT)

        return lists
    }
}
