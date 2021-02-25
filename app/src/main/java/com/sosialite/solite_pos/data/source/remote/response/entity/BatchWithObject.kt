package com.sosialite.solite_pos.data.source.remote.response.entity

data class BatchWithObject<T> (
        var data: T,
        var batch: BatchWithData
)
