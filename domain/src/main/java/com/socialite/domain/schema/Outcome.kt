package com.socialite.domain.schema

import com.socialite.domain.helper.DateUtils
import com.socialite.data.schema.room.new_master.Outcome as DataOutcome

data class Outcome(
    val id: String,
    val name: String,
    val desc: String,
    val price: Long,
    val amount: Int,
    val date: String,
    val store: String,
    val isUploaded: Boolean
) {


    fun dateString() =
        DateUtils.convertDateFromDb(date, DateUtils.DATE_WITH_DAY_WITHOUT_YEAR_FORMAT)

    val total: Long
        get() = price * amount

    companion object {
        fun fromData(data: DataOutcome) : Outcome {
            return Outcome(
                data.id,
                data.name,
                data.desc,
                data.price,
                data.amount,
                data.date,
                data.store,
                data.isUploaded
            )
        }
    }
}
