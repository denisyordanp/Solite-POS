package com.sosialite.solite_pos.utils.tools

import com.google.firebase.firestore.QuerySnapshot

interface RemoteUtils<T> {
    fun convertToHashMap(data: T): HashMap<String, Any?>
    fun convertToListClass(result: QuerySnapshot): List<T>
}