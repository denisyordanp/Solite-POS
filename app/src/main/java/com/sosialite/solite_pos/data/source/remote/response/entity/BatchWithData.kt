package com.sosialite.solite_pos.data.source.remote.response.entity

import com.google.firebase.firestore.DocumentReference

data class BatchWithData (
        var doc: DocumentReference,
        var hashMap: HashMap<String, Any?>
        )