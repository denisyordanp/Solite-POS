package com.socialite.solite_pos.utils.config

object CashAmounts {

    private const val BANK_NOTES = 5000L
    private const val HUNDRED_AMOUNT = 100000L
    private const val TWO_HUNDRED_AMOUNT = 200000L
    private const val THREE_HUNDRED_AMOUNT = 300000L
    private const val MILLION_AMOUNT = 1000000L

    fun generateCash(total: Long): List<Long> {
        return when {
            total <= HUNDRED_AMOUNT -> underHundredKSuggestions()
            total <= TWO_HUNDRED_AMOUNT-> moreOneHundredKSuggestions()
            total <= THREE_HUNDRED_AMOUNT-> moreTwoHundredKSuggestions()
            else -> moreThreeHundredKSuggestions()
        }
    }

    private fun underHundredKSuggestions(): List<Long> {
        var currentAmount = BANK_NOTES
        val lists = mutableListOf<Long>()
        do {
            lists.add(currentAmount)
            currentAmount += BANK_NOTES
        } while (currentAmount < HUNDRED_AMOUNT)

        return lists
    }

    private fun moreOneHundredKSuggestions(): List<Long> {
        var currentAmount = HUNDRED_AMOUNT
        val lists = mutableListOf<Long>()
        do {
            lists.add(currentAmount)
            currentAmount += BANK_NOTES
        } while (currentAmount < TWO_HUNDRED_AMOUNT)

        return lists
    }

    private fun moreTwoHundredKSuggestions(): List<Long> {
        var currentAmount = TWO_HUNDRED_AMOUNT
        val lists = mutableListOf<Long>()
        do {
            lists.add(currentAmount)
            currentAmount += BANK_NOTES
        } while (currentAmount < THREE_HUNDRED_AMOUNT)

        return lists
    }

    private fun moreThreeHundredKSuggestions(): List<Long> {
        var currentAmount = THREE_HUNDRED_AMOUNT
        val lists = mutableListOf<Long>()
        do {
            lists.add(currentAmount)
            currentAmount += BANK_NOTES
        } while (currentAmount < MILLION_AMOUNT)

        return lists
    }
}
