package com.socialite.solite_pos.view.main.opening.orders

import androidx.annotation.DrawableRes
import com.socialite.solite_pos.R

enum class OrderButtonType(
    @DrawableRes val icon: Int
) {
    PRINT(R.drawable.ic_print),
    QUEUE(R.drawable.ic_queue_24),
    PAYMENT(R.drawable.ic_payments),
    DONE(R.drawable.ic_done_all),
    CANCEL(R.drawable.ic_cross_50dp),
}
